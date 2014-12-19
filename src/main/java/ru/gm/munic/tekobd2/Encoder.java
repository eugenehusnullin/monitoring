package ru.gm.munic.tekobd2;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import ru.gm.munic.tekobd2.messages.Message;

public class Encoder implements ProtocolEncoder {

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		Message m = (Message) message;
		byte[] bytes = m.makeResponse();

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
