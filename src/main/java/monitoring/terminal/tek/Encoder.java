package monitoring.terminal.tek;

import monitoring.terminal.tek.messages.MessageUtilities;
import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.terminal.tek.responses.ResponseCreator;
import monitoring.terminal.tek.responses.ResponseCreatorDetector;
import monitoring.utils.ByteUtilities;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

class Encoder implements ProtocolEncoder {
	private static final Logger streamLogger = LoggerFactory.getLogger("Stream");

	private ResponseCreatorDetector responseCreatorDetector;

	public void setResponseCreatorDetector(ResponseCreatorDetector responseCreatorDetector) {
		this.responseCreatorDetector = responseCreatorDetector;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		TekMessage messageImpl = (TekMessage) message;

		ResponseCreator responseCreator = responseCreatorDetector.getResponseCreator(messageImpl.getMessageType());
		if (responseCreator != null) {
			byte[] bytes = responseCreator.create(messageImpl);

			if (streamLogger.isDebugEnabled()) {
				String stream = ByteUtilities.bytesToHex(bytes);

				MDC.put(Decoder.KEY_TERMINAL_ID, Long.toString((long) session.getAttribute(Decoder.KEY_TERMINAL_ID)));
				streamLogger.debug("out - " + stream);
			}

			IoBuffer buf = IoBuffer.allocate(bytes.length + 2).setAutoExpand(true);
			buf.put(MessageUtilities.MARKER_BYTE);
			buf.put(bytes);
			buf.put(MessageUtilities.MARKER_BYTE);

			buf.flip();
			out.write(buf);
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {

	}

}
