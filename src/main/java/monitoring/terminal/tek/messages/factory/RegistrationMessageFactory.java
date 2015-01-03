package monitoring.terminal.tek.messages.factory;

import java.nio.ByteBuffer;

import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.terminal.tek.messages.domain.RegistrationMessage;

public class RegistrationMessageFactory extends MessageFactory {

	@Override
	public TekMessage createMessage() {
		return new RegistrationMessage();
	}

	@Override
	public void initializeMessageBody(TekMessage message, ByteBuffer buffer) {
		RegistrationMessage registrationMessage = (RegistrationMessage) message;
		byte[] messageBody = new byte[registrationMessage.getBodyLength()];
		buffer.get(messageBody);
	}

}
