package monitoring.tekobd2;

import monitoring.tekobd2.messages.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Decoder extends CumulativeProtocolDecoder {

	private static final Logger logger = LoggerFactory.getLogger(Decoder.class);
	private static final Logger streamLogger = LoggerFactory.getLogger("Stream");
	private static final int MIN_MESS_SIZE = 15;
	public static final byte MARKER_BYTE = (byte) 0x7e;
	public static final byte ESCAPE_BYTE = (byte) 0x7d;
	public static final String KEY_TERMINAL_ID = "TERMINAL_ID";

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		int startPosition = in.position();

		Message message = process(in);
		if (message != null) {

			session.setAttribute(KEY_TERMINAL_ID, message.getTerminalId());
			if (streamLogger.isDebugEnabled()) {
				int endPosition = in.position();
				in.position(startPosition);
				String stream = ByteUtilities.ioBufferToHex(in);
				in.position(endPosition);

				MDC.put(KEY_TERMINAL_ID, Long.toString(message.getTerminalId()));
				streamLogger.debug("in - " + stream);
			}

			out.write(message);
			return true;
		} else {
			return false;
		}
	}

	public static Message process(IoBuffer in) {
		Message message = null;

		int firstPosition = in.position();

		if (logger.isDebugEnabled()) {
			String allBufferData = ByteUtilities.ioBufferToHex(in);
			in.position(firstPosition);
			logger.debug("BYTES :: " + allBufferData);
		}

		int startIndex = ByteUtilities.searchInBuffer(in, MARKER_BYTE);
		if (startIndex != -1) {

			int endIndex = ByteUtilities.searchInBuffer(in, MARKER_BYTE);
			if (endIndex == -1) {
				in.position(startIndex);

			} else {
				if (endIndex + 1 - startIndex >= MIN_MESS_SIZE) {
					try {
						in.position(startIndex + 1);
						message = Message.parseMessage(in, endIndex + 1 - startIndex - 2);

					} catch (Exception e) {
						in.position(startIndex);
						String str = ByteUtilities.ioBufferToHex(in, endIndex + 1 - startIndex);
						logger.warn("parseMessage exception :: " + str);
					}
				}

				in.position(endIndex);
			}
		}

		return message;
	}
}
