package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;

public abstract class GeneralResponseMessage extends Message {

	@Override
	protected int getResponseBodySize() {
		return 5;
	}

	@Override
	protected short getResponseMessageId() {
		return (short) 0xb003;
	}

	@Override
	protected void initResponseBody(ByteBuffer bb) {
		// message body
		bb.putShort(getSerialNumber());
		bb.putShort(getMessageId());
		bb.put((byte) 0);
	}

}
