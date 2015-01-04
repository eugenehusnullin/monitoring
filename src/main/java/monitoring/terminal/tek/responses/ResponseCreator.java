package monitoring.terminal.tek.responses;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import monitoring.terminal.tek.messages.MessageUtilities;
import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.utils.ByteUtilities;

public abstract class ResponseCreator {
	private static final int BLUNK_MESSAGE_SIZE = 13;

	public byte[] create(TekMessage message) {
		ByteBuffer buffer = initHeader(message);
		initResponseBody(message, buffer);
		initBottom(buffer);

		return postProcess(buffer);
	}

	abstract int getResponseBodySize();

	abstract short getResponseMessageId();
	
	abstract void initResponseBody(TekMessage message, ByteBuffer buffer);

	private ByteBuffer initHeader(TekMessage message) {
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
		byte checkCode = MessageUtilities.createCheckCode(buffer, buffer.limit() - 1);
		buffer.put(checkCode);
	}

	private byte[] postProcess(ByteBuffer buffer) {
		// escape
		buffer.position(0);
		byte[] outBytes = MessageUtilities.escapeOut(buffer);
		return outBytes;
	}

}
