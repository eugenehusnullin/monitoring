package monitoring.terminal.tek;

import monitoring.terminal.tek.messages.MessageParser;
import monitoring.terminal.tek.messages.MessageUtilities;
import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.utils.ByteUtilities;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

class Decoder extends CumulativeProtocolDecoder {

	private static final Logger logger = LoggerFactory.getLogger(Decoder.class);
	private static final Logger streamLogger = LoggerFactory.getLogger("Stream");
	private static final int MIN_MESS_SIZE = 15;
	public static final String KEY_TERMINAL_ID = "TERMINAL_ID";

	private MessageParser messageParser;

	public void setMessageParser(MessageParser messageParser) {
		this.messageParser = messageParser;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		int startPosition = in.position();
		TekMessage message = process(in);

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

	public TekMessage process(IoBuffer in) {
		TekMessage message = null;

		if (logger.isDebugEnabled()) {
			int firstPosition = in.position();
			String allBufferData = ByteUtilities.ioBufferToHex(in);
			in.position(firstPosition);
			logger.debug("BYTES :: " + allBufferData);
		}

		int startIndex = ByteUtilities.searchInBuffer(in, MessageUtilities.MARKER_BYTE);
		if (startIndex != -1) {

			int endIndex = ByteUtilities.searchInBuffer(in, MessageUtilities.MARKER_BYTE);
			if (endIndex == -1) {
				in.position(startIndex);

			} else {
				if (endIndex + 1 - startIndex >= MIN_MESS_SIZE) {
					try {
						in.position(startIndex + 1);
						message = messageParser.parse(in, endIndex + 1 - startIndex - 2);

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
