package monitoring.tek.responses;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import monitoring.tek.ByteUtilities;
import monitoring.tek.messages.MessageUtilities;
import monitoring.tek.messages.domain.MessageImpl;

public abstract class ResponseCreator {
	private static final int BLUNK_MESSAGE_SIZE = 13;

	public byte[] create(MessageImpl message) {
		ByteBuffer buffer = initHeader(message);
		initResponseBody(message, buffer);
		initBottom(buffer);

		return postProcess(buffer);
	}

	abstract int getResponseBodySize();

	abstract short getResponseMessageId();
	
	abstract void initResponseBody(MessageImpl message, ByteBuffer buffer);

	private ByteBuffer initHeader(MessageImpl message) {
		ByteBuffer buffer = ByteBuffer.allocate(BLUNK_MESSAGE_SIZE + getResponseBodySize());
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.position(0);

		// message header
		buffer.putShort(getResponseMessageId());
		buffer.putShort((short) getResponseBodySize());
		buffer.put(ByteUtilities.decimalToBcd(message.getTerminalId()));
		buffer.putShort(message.getSerialNumber());

		return buffer;
	}

	private void initBottom(ByteBuffer buffer) {
		// check code
		buffer.position(0);
		buffer.put(MessageUtilities.createCheckCode(buffer, buffer.limit() - 1));

	}

	private byte[] postProcess(ByteBuffer buffer) {
		// escape
		buffer.position(0);
		byte[] outBytes = MessageUtilities.escapeOut(buffer);
		return outBytes;
	}

}
