package monitoring.terminal.tek.messages.factory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import monitoring.terminal.tek.messages.domain.LoginMessage;
import monitoring.terminal.tek.messages.domain.TekMessage;

public class LoginMessageFactory extends MessageFactory {

	@Override
	public TekMessage createMessage() {
		return new LoginMessage();
	}

	@Override
	public void initializeMessageBody(TekMessage message, ByteBuffer buffer) {
		LoginMessage loginMessage = (LoginMessage) message;

		byte[] messageBody = new byte[loginMessage.getBodyLength()];

		buffer.get(messageBody);
		loginMessage.setAuthKey(new String(messageBody, Charset.forName("GBK")));
	}

}
