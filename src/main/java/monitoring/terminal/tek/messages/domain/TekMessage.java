package monitoring.terminal.tek.messages.domain;

import monitoring.domain.Message;


public abstract class TekMessage implements Message {

	private short messageType;
	private long terminalId;
	private short serialNumber;
	private short bodyLength;
	private byte encryptWay;
	private boolean subPackage;
	private byte checkCode;

	public short getMessageType() {
		return messageType;
	}

	public void setMessageType(short messageType) {
		this.messageType = messageType;
	}

	@Override
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
}
