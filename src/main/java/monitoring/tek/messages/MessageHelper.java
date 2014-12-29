package monitoring.tek.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

public class MessageHelper {
	public ByteBuffer preprocess(IoBuffer in, int length) throws Exception {
		byte[] bytes = MessageUtilities.escapeIn(in, length);

		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.order(ByteOrder.BIG_ENDIAN);

		return buffer;
	}

	public short detectMessageId(ByteBuffer buffer) {
		short messageId = buffer.getShort();

		buffer.position(buffer.position() - 2);

		return messageId;
	}
}
