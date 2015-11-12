package monitoring.handler.wialon;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class SimpleDecoder extends ProtocolDecoderAdapter {

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		byte[] bytes = new byte[in.remaining()];
		in.get(bytes);
		out.write(new String(bytes, "ASCII"));		
	}

}
