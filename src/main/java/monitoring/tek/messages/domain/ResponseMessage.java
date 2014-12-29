package monitoring.tek.messages.domain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import monitoring.tek.ByteUtilities;
import monitoring.tek.messages.MessageUtilities;

public abstract class ResponseMessage extends TekMessageImpl implements HasResponse {
	private static final int BLUNK_MESSAGE_SIZE = 13;

	@Override
	public byte[] makeResponse() {
		ByteBuffer bb = initHeader(getResponseBodySize(), getResponseMessageId());
		initResponseBody(bb);
		byte[] outBytes = initBottom(bb);
		return outBytes;
	}
	
	private ByteBuffer initHeader(int messageBodyLength, short messageId) {
		ByteBuffer bb = ByteBuffer.allocate(BLUNK_MESSAGE_SIZE + messageBodyLength);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.position(0);

		// message header
		bb.putShort(messageId);
		bb.putShort((short) messageBodyLength);
		bb.put(ByteUtilities.decimalToBcd(getTerminalId()));
		bb.putShort(getSerialNumber());

		return bb;
	}

	private byte[] initBottom(ByteBuffer bb) {
		// check code
		bb.position(0);
		bb.put(MessageUtilities.createCheckCode(bb, bb.limit() - 1));

		// escape
		bb.position(0);
		byte[] outBytes = MessageUtilities.escapeOut(bb);
		return outBytes;
	}

}
