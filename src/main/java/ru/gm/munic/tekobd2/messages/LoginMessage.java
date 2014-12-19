package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class LoginMessage extends GeneralResponseMessage {

	@Override
	public void parseMessageBody(ByteBuffer bb) {
		byte[] messageBody = new byte[getBodyLength()];

		bb.get(messageBody);
		setAuthKey(new String(messageBody, Charset.forName("GBK")));
	}

	private String authKey;

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

}
