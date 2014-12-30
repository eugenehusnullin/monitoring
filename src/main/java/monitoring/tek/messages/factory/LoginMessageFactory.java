package monitoring.tek.messages.factory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import monitoring.tek.messages.domain.LoginMessage;
import monitoring.tek.messages.domain.MessageImpl;

public class LoginMessageFactory extends MessageFactory {

	@Override
	public MessageImpl createTekMessage() {
		return new LoginMessage();
	}

	@Override
	public void initializeMessageBody(MessageImpl message, ByteBuffer buffer) {
		LoginMessage loginMessage = (LoginMessage) message;

		byte[] messageBody = new byte[loginMessage.getBodyLength()];

		buffer.get(messageBody);
		loginMessage.setAuthKey(new String(messageBody, Charset.forName("GBK")));
	}

}
