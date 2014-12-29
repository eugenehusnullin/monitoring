package monitoring.tek.messages.factory;

import java.nio.ByteBuffer;

import monitoring.tek.ByteUtilities;
import monitoring.tek.messages.domain.TekMessageImpl;

public abstract class MessageFactory {

	public TekMessageImpl initializeMessage(ByteBuffer buffer) throws Exception {
		TekMessageImpl message = getNewTekMessage();

		message.setMessageId(buffer.getShort());

		// body attribute
		short bodyAttribute = buffer.getShort();

		message.setBodyLength((short) (bodyAttribute & 0x3ff));

		byte encrWay = (byte) ((bodyAttribute >> 10) & 0b111);
		message.setEncryptWay(encrWay);

		byte subPackage = (byte) ((bodyAttribute >> 13) & 0b1);
		message.setSubPackage(subPackage == (byte) 1);

		// terminal Id / 6 bytes
		String terminalIdString = ByteUtilities.bcdToString(buffer, 6);
		message.setTerminalId(Long.parseLong(terminalIdString));

		// message serial number
		message.setSerialNumber(buffer.getShort());

		if (buffer.remaining() != message.getBodyLength() + 1) {
			throw new Exception("bad packet body length!");
		} else {
			initializeMessageBody(message, buffer);

			buffer.position(buffer.limit() - 1);
			message.setCheckCode(buffer.get());

			return message;
		}
	}
	
	abstract TekMessageImpl getNewTekMessage();
	
	abstract void initializeMessageBody(TekMessageImpl message, ByteBuffer buffer);
}
