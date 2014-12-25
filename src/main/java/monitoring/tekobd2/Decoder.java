package monitoring.tekobd2;

import monitoring.tekobd2.messages.Message;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Decoder extends CumulativeProtocolDecoder {

	private static final Logger logger = LoggerFactory.getLogger(Decoder.class);
	private static final int MIN_MESS_SIZE = 15;
	public static final byte MARKER_BYTE = (byte) 0x7e;
	public static final byte ESCAPE_BYTE = (byte) 0x7d;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		
		Message message = process(in);
		if (message != null) {
			out.write(message);
			return true;
		} else {
			return false;
		}
	}
	
	public static Message process(IoBuffer in) {
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
				if (endIndex + 1 - startIndex < MIN_MESS_SIZE) {
					in.position(endIndex);

				} else {
					try {
						in.position(startIndex + 1);
						Message message = Message.parseMessage(in, endIndex + 1 - startIndex - 2);
						return message;
					} catch (Exception e) {
						in.position(startIndex);
						String str = ByteUtilities.ioBufferToHex(in, endIndex + 1 - startIndex);
						logger.warn("parseMessage exception :: " + str);
					}
				}
			}
		}

		return null;
	}
}
