package monitoring.tekobd2;

import monitoring.tekobd2.messages.IHasResponse;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Encoder implements ProtocolEncoder {
	private static final Logger streamLogger = LoggerFactory.getLogger("Stream");

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IHasResponse response = (IHasResponse) message;
		byte[] bytes = response.makeResponse();
		
		if (streamLogger.isDebugEnabled()) {
			String stream = ByteUtilities.bytesToHex(bytes);
			
			MDC.put(Decoder.KEY_TERMINAL_ID, Long.toString((long) session.getAttribute(Decoder.KEY_TERMINAL_ID)));
			streamLogger.debug("out - " + stream);
		}

		IoBuffer buf = IoBuffer.allocate(bytes.length + 2).setAutoExpand(true);
		buf.put(Decoder.MARKER_BYTE);
		buf.put(bytes);
		buf.put(Decoder.MARKER_BYTE);
		
		buf.flip();
		out.write(buf);
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub

	}

}
