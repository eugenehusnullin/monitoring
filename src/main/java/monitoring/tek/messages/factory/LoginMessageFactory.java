package monitoring.tek.messages.factory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import monitoring.tek.messages.domain.LoginMessage;
import monitoring.tek.messages.domain.TekMessageImpl;

public class LoginMessageFactory extends MessageFactory {

	@Override
	TekMessageImpl getNewTekMessage() {
		return new LoginMessage();
	}

	@Override
	void initializeMessageBody(TekMessageImpl message, ByteBuffer buffer) {
		LoginMessage loginMessage = (LoginMessage) message;

		byte[] messageBody = new byte[loginMessage.getBodyLength()];

		buffer.get(messageBody);
		loginMessage.setAuthKey(new String(messageBody, Charset.forName("GBK")));
	}

}
