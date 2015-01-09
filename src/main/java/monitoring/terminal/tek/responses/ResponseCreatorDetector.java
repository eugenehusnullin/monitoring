package monitoring.terminal.tek.responses;

public class ResponseCreatorDetector {
	public ResponseCreator getResponseCreator(short messageId) {
		ResponseCreator creator = null;

		switch (messageId) {

		case 0x3000:
			// Terminal registration
			creator = new RegistrationResponseCreator();
			break;

		case 0x3001: // Terminal logout
		case 0x3002: // Terminal login
		case 0x3004: // Terminal heart beat
		case 0x3005: // Trip starting and ending report
		case 0x3006: // Trip data reporting
		case 0x3012: // Vehicle supported protocol and data stream reporting
			creator = new GeneralResponseCreator();
			break;

		case 0x3003:
			// Terminal general response

			break;

		case 0x3007:
			// Inquiry vehicle information terminal response

			break;
			
		case 0x3015: // Terminal inquiry server system time
			// Terminal inquiry server system time server response
			// Message ID: 0XB015
			break;

		default:
			break;
		}

		return creator;
	}
}
