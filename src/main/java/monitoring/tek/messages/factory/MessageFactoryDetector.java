package monitoring.tek.messages.factory;


public class MessageFactoryDetector {

	public MessageFactory getMessageFactory(short messageId) {
		MessageFactory messageFactory = null;
		switch (messageId) {

		case 0x3000:
			// Terminal registration
			messageFactory = new RegistrationMessageFactory();
			break;

		case 0x3001:
			// Terminal logout

			break;

		case 0x3002:
			messageFactory = new LoginMessageFactory();
			break;

		case 0x3003:
			// Terminal general response

			break;

		case 0x3004:
			// Terminal heartbeat

			break;

		case 0x3005:
			// Trip starting and ending report

			break;

		case 0x3006:
			// Trip data reporting
			messageFactory = new TripDataMessageFactory();
			break;

		case 0x3007:
			// Inquiry vehicle information terminal response

			break;

		default:
			break;
		}

		return messageFactory;
	}
}
