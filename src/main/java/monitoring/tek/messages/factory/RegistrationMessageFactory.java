package monitoring.tek.messages.factory;

import java.nio.ByteBuffer;

import monitoring.tek.messages.domain.TekMessageImpl;
import monitoring.tek.messages.domain.RegistrationMessage;

public class RegistrationMessageFactory extends MessageFactory {

	@Override
	TekMessageImpl getNewTekMessage() {
		return new RegistrationMessage();
	}

	@Override
	void initializeMessageBody(TekMessageImpl message, ByteBuffer buffer) {
		RegistrationMessage registrationMessage = (RegistrationMessage) message;
		byte[] messageBody = new byte[registrationMessage.getBodyLength()];
		buffer.get(messageBody);
	}

}
