package ru.gm.munic.tekobd2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class Message {

	private byte[] inBytes;
	private int bytesLength;
	private short id;
	private long terminalId;
	private short serialNumber;
	private short bodyLength;
	private byte encyptWay;
	private byte subPackage;
	private byte checkCode;
	private static final String AUTH_KEY = "SCO";
	private static byte[] AUTH_KEY_BYTES;

	static {
		AUTH_KEY_BYTES = AUTH_KEY.getBytes(Charset.forName("GBK"));
	}

	public short getId() {
		return id;
	}

	public void setId(short id) {
		this.id = id;
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

	public byte getEncyptWay() {
		return encyptWay;
	}

	public void setEncyptWay(byte encyptWay) {
		this.encyptWay = encyptWay;
	}

	public byte getSubPackage() {
		return subPackage;
	}

	public void setSubPackage(byte subPackage) {
		this.subPackage = subPackage;
	}

	public byte getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(byte checkCode) {
		this.checkCode = checkCode;
	}

	public static Message parseMessage(byte[] srcBytes, int offset, int length) throws Exception {
		// example how the bytes arrived
		// 7E 3017 0002 814087547576 000B 0300 3C 7E
		// 7E 3000 0019 814087547576 000C
		// 00000000000000000000000000000000000000000000000000 34 7E
		// 7E 3017 0002 814087547576 000D 0300 3A 7E
		// 7E 3017 0002 814087547576 000E 0300 39 7E
		// 7E 3017 0002 814087547576 000F 0300 38 7E
		// 7E30000019814087547576001000000000000000000000000000000000000000000000000000287E7E3000001981408754757600110000000000000000000000
		// 0000000000000000000000000000297E
		// 7E 3017 0002 814087547576 0012 0300 25 7E
		// 7E 3017 0002 814087547576 0013 0300 24 7E
		// 7E 3017 0002 814087547576 0014 0300 23 7E
		// 7E 3000 0019 814087351432 0020
		// 00000000000000000000000000000000000000000000000000 5C 7E
		// 7E 3017 0002 814087547576 0015 0300 22 7E
		// 7E 3017 0002 814087547576 0016 0300 21 7E
		// 7E 3000 0019 814087547576 0017
		// 00000000000000000000000000000000000000000000000000 2F 7E

		Message message = new Message();
		message.setBytes(new byte[length]);

		int i = offset;
		int j = 0;
		while (i < (offset + length)) {
			if (srcBytes[i] == 0x7d && srcBytes[i + 1] == 0x02) {
				message.getBytes()[j] = 0x7e;
				i = i + 2;
			} else if (srcBytes[i] == 0x7d && srcBytes[i + 1] == 0x01) {
				message.getBytes()[j] = 0x7d;
				i = i + 2;
			} else {
				message.getBytes()[j] = srcBytes[i];
				i++;
			}
			j++;
		}
		message.setBytesLength(j);

		ByteBuffer bb = ByteBuffer.allocate(j);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.put(message.getBytes(), 0, j);
		bb.position(0);

		// message Id
		message.setId(bb.getShort());

		// body attribute
		short bodyAttribute = bb.getShort();

		message.setBodyLength((short) (bodyAttribute & 0x3ff));

		byte encrWay = (byte) ((bodyAttribute >> 10) & 0b111);
		message.setEncyptWay(encrWay);

		byte subPackage = (byte) ((bodyAttribute >> 13) & 0b1);
		message.setSubPackage(subPackage);

		// terminal Id / 6 bytes
		String terminalIdString = bcdToString(message.getBytes(), 4, 6);
		message.setTerminalId(Long.parseLong(terminalIdString));
		bb.position(bb.position() + 6);

		// message serial number
		message.setSerialNumber(bb.getShort());

		if (bb.remaining() != message.getBodyLength() + 1) {
			throw new Exception("bad packet!");
		} else {
			byte[] messageBody = new byte[message.getBodyLength()];
			bb.get(messageBody);
			parseMessageBody(messageBody, message);

			message.setCheckCode(bb.get());

			return message;
		}
	}

	private static void parseMessageBody(byte[] messageBody, Message message) {
		if (message.getId() == 0x3000) {
			// Terminal registration

		}
	}

	public byte[] makeResponse() {
		if (getId() == 0x3000) {
			return makeRegistrationResponse();
		} else {
			return null;
		}
	}

	private byte[] makeRegistrationResponse() {
		int messageBodyLength = 3 + AUTH_KEY_BYTES.length;
		ByteBuffer bb = ByteBuffer.allocate(13 + messageBodyLength);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.position(0);

		bb.putShort((short) 0xb000);
		bb.putShort((short) messageBodyLength);
		bb.put(inBytes, 4, 6);
		bb.putShort(serialNumber);
		bb.putShort(serialNumber);
		bb.put((byte) 0);
		bb.put(AUTH_KEY_BYTES);

		bb.position(0);
		bb.put(createCheckCode(bb, bb.limit() - 1));

		bb.position(0);
		byte[] outBytes = escape4Out(bb);
		return outBytes;
	}

	private byte[] escape4Out(ByteBuffer bb) {
		int cnt = bb.remaining();
		while (bb.hasRemaining()) {
			byte b = bb.get();
			if (b == Decoder.MARKER_BYTE || b == Decoder.ESCAPE_BYTE) {
				cnt++;
			}
		}

		byte[] escapedBytes = new byte[cnt];
		bb.position(0);
		int j = 0;
		while (bb.hasRemaining()) {
			byte b = bb.get();
			if (b == Decoder.MARKER_BYTE) {
				escapedBytes[j] = Decoder.ESCAPE_BYTE;
				j++;
				escapedBytes[j] = 0x02;
			} else if (b == Decoder.ESCAPE_BYTE) {
				escapedBytes[j] = Decoder.ESCAPE_BYTE;
				j++;
				escapedBytes[j] = 0x01;

			} else {
				escapedBytes[j] = b;
			}
			j++;
		}

		return escapedBytes;
	}

	public static byte createCheckCode(ByteBuffer bb, int length) {
		byte checkCode = bb.get();
		for (int i = 0; i < length - 1; i++) {
			checkCode = (byte) (checkCode ^ bb.get());
		}
		return checkCode;
	}

	public static String bcdToString(byte[] bcd, int offset, int length) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			byte high = (byte) (bcd[offset + i] & 0xf0);
			high = (byte) (high >>> 4);
			high = (byte) (high & 0x0f);

			byte low = (byte) (bcd[offset + i] & 0x0f);

			sb.append(high);
			sb.append(low);
		}

		return sb.toString();
	}

	public byte[] getBytes() {
		return inBytes;
	}

	public void setBytes(byte[] bytes) {
		this.inBytes = bytes;
	}

	public int getBytesLength() {
		return bytesLength;
	}

	public void setBytesLength(int bytesLength) {
		this.bytesLength = bytesLength;
	}

}
