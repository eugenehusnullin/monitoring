package monitoring.tekobd2;

import monitoring.tekobd2.messages.IHasResponse;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class Encoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IHasResponse response = (IHasResponse) message;
		byte[] bytes = response.makeResponse();

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
