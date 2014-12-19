package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class RegistrationMessage extends Message {

	@Override
	public void parseMessageBody(ByteBuffer bb) {
		// just move position
		// value in registration body don't need
		byte[] messageBody = new byte[getBodyLength()];
		bb.get(messageBody);
	}

	public static final String AUTH_KEY = "SCO";
	private static byte[] AUTH_KEY_BYTES;

	static {
		AUTH_KEY_BYTES = AUTH_KEY.getBytes(Charset.forName("GBK"));
	}

	@Override
	protected int getResponseBodySize() {
		return 3 + AUTH_KEY_BYTES.length;
	}

	@Override
	protected short getResponseMessageId() {
		return (short) 0xb000;
	}

	@Override
	protected void initResponseBody(ByteBuffer bb) {
		// message body
		bb.putShort(getSerialNumber());
		bb.put((byte) 0);
		bb.put(AUTH_KEY_BYTES);
	}

}
