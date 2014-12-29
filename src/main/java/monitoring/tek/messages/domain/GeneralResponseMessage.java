package monitoring.tek.messages.domain;

import java.nio.ByteBuffer;

public abstract class GeneralResponseMessage extends ResponseMessage {

	@Override
	public int getResponseBodySize() {
		return 5;
	}

	@Override
	public short getResponseMessageId() {
		return (short) 0xb003;
	}

	@Override
	public void initResponseBody(ByteBuffer bb) {
		// message body
		bb.putShort(getSerialNumber());
		bb.putShort(getMessageId());
		bb.put((byte) 0);
	}

}
