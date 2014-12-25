package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.mina.core.buffer.IoBuffer;

import ru.gm.munic.domain.IMessage;
import ru.gm.munic.tekobd2.ByteUtilities;

public abstract class Message implements IMessage {

	private byte[] inBytes;

	private short messageId;
	private long terminalId;
	private short serialNumber;
	private short bodyLength;
	private byte encryptWay;
	private boolean subPackage;
	private byte checkCode;

	abstract void parseMessageBody(ByteBuffer bb);

	public static Message parseMessage(IoBuffer in, int inLength) throws Exception {
		// example how the bytes arrived
		// 7E 3017 0002 814087547576 000B 0300 3C 7E
		// 7E 3000 0019 814087547576 000C
		// 00000000000000000000000000000000000000000000000000 34 7E
		// 7E30000019814087547576001000000000000000000000000000000000000000000000000000287E7E3000001981408754757600110000000000000000000000
		// 0000000000000000000000000000297E
		// 7E 3000 0019 814087351432 0020
		// 00000000000000000000000000000000000000000000000000 5C 7E
		// 7E 3017 0002 814087547576 0015 0300 22 7E
		// 7E 3000 0019 814087547576 0017
		// 00000000000000000000000000000000000000000000000000 2F 7E

		byte[] bytes = MessageUtilities.escapeIn(in, inLength);

		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.BIG_ENDIAN);

		// message Id
		short messageId = bb.getShort();

		Message message = defineMessage(messageId);
		if (message == null) {
			throw new Exception("Coudn't define message type by message id = " + messageId);
		}

		message.setInBytes(bytes);
		message.setMessageId(messageId);

		// body attribute
		short bodyAttribute = bb.getShort();

		message.setBodyLength((short) (bodyAttribute & 0x3ff));

		byte encrWay = (byte) ((bodyAttribute >> 10) & 0b111);
		message.setEncryptWay(encrWay);

		byte subPackage = (byte) ((bodyAttribute >> 13) & 0b1);
		message.setSubPackage(subPackage == (byte) 1);

		// terminal Id / 6 bytes
		String terminalIdString = ByteUtilities.bcdToString(message.getInBytes(), 4, 6);
		message.setTerminalId(Long.parseLong(terminalIdString));
		bb.position(bb.position() + 6);

		// message serial number
		message.setSerialNumber(bb.getShort());

		if (bb.remaining() != message.getBodyLength() + 1) {
			throw new Exception("bad packet!");
		} else {
			message.parseMessageBody(bb);

			message.setCheckCode(bb.get(bytes.length - 1));

			return message;
		}
	}

	private static Message defineMessage(short messageId) {
		Message message = null;
		switch (messageId) {

		case 0x3000:
			// Terminal registration
			message = new RegistrationMessage();
			break;

		case 0x3001:
			// Terminal logout

			break;

		case 0x3002:
			message = new LoginMessage();
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
			message = new TripDataMessage();
			break;

		case 0x3007:
			// Inquiry vehicle information terminal response

			break;

		default:
			break;
		}

		return message;
	}

	public short getMessageId() {
		return messageId;
	}

	public void setMessageId(short id) {
		this.messageId = id;
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public short getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(short serialNumber) {
		this.serialNumber = serialNumber;
	}

	public short getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(short bodyLength) {
		this.bodyLength = bodyLength;
	}

	public byte getEncryptWay() {
		return encryptWay;
	}

	public void setEncryptWay(byte encyptWay) {
		this.encryptWay = encyptWay;
	}

	public boolean getSubPackage() {
		return subPackage;
	}

	public void setSubPackage(boolean subPackage) {
		this.subPackage = subPackage;
	}

	public byte getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(byte checkCode) {
		this.checkCode = checkCode;
	}

	public byte[] getInBytes() {
		return inBytes;
	}

	public void setInBytes(byte[] bytes) {
		this.inBytes = bytes;
	}

}
