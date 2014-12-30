package monitoring.tek.responses;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import monitoring.tek.messages.domain.MessageImpl;

public class RegistrationResponseCreator extends ResponseCreator {

	public static final String AUTH_KEY = "SCO";
	private static byte[] AUTH_KEY_BYTES;

	static {
		AUTH_KEY_BYTES = AUTH_KEY.getBytes(Charset.forName("GBK"));
	}

	@Override
	int getResponseBodySize() {
		return 3 + AUTH_KEY_BYTES.length;
	}

	@Override
	short getResponseMessageId() {
		return (short) 0xb000;
	}

	@Override
	void initResponseBody(MessageImpl message, ByteBuffer buffer) {
		// message body
		buffer.putShort(message.getSerialNumber());
		buffer.put((byte) 0);
		buffer.put(AUTH_KEY_BYTES);
	}

}
