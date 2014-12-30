package monitoring.tek.messages.factory;

import java.nio.ByteBuffer;

import monitoring.tek.messages.domain.MessageImpl;
import monitoring.tek.messages.domain.RegistrationMessage;

public class RegistrationMessageFactory extends MessageFactory {

	@Override
	public MessageImpl createTekMessage() {
		return new RegistrationMessage();
	}

	@Override
	public void initializeMessageBody(MessageImpl message, ByteBuffer buffer) {
		RegistrationMessage registrationMessage = (RegistrationMessage) message;
		byte[] messageBody = new byte[registrationMessage.getBodyLength()];
		buffer.get(messageBody);
	}

}
