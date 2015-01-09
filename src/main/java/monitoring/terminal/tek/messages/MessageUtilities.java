package monitoring.terminal.tek.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.utils.ByteUtilities;

import org.apache.mina.core.buffer.IoBuffer;

public class MessageUtilities {
	public static final byte ESCAPE_BYTE = (byte) 0x7d;
	public static final byte MARKER_BYTE = (byte) 0x7e;

	public static byte[] escapeIn(IoBuffer in, int length) throws Exception {
		int startPosition = in.position();
		int count = ByteUtilities.countInBuffer(in, ESCAPE_BYTE, length);
		in.position(startPosition);
		byte[] bytes = new byte[length - count];

		int j = 0;
		int i = 0;
		while (in.hasRemaining() && i < length) {
			byte b = in.get();
			i++;

			if (b == MARKER_BYTE) {
				break;
			}

			if (b == ESCAPE_BYTE) {
				if (!in.hasRemaining()) {
					throw new Exception("Escape rules are broken! (not enough bytes)");
				}
				b = in.get();
				i++;

				if (b == 0x02) {
					bytes[j] = MARKER_BYTE;

				} else if (b == 0x01) {
					bytes[j] = ESCAPE_BYTE;

				} else {
					throw new Exception("Escape rules are broken! (uncorrect byte folowed escape byte)");
				}

			} else {
				bytes[j] = b;
			}

			j++;

		}

		return bytes;
	}

	public static byte[] escapeOut(ByteBuffer bb) {
		int startPosition = bb.position();
		int cnt = bb.remaining();
		while (bb.hasRemaining()) {
			byte b = bb.get();
			if (b == MARKER_BYTE || b == ESCAPE_BYTE) {
				cnt++;
			}
		}
		bb.position(startPosition);

		byte[] escapedBytes = new byte[cnt];
		int j = 0;
		while (bb.hasRemaining()) {
			byte b = bb.get();
			if (b == MARKER_BYTE) {
				escapedBytes[j] = ESCAPE_BYTE;
				j++;
				escapedBytes[j] = 0x02;
			} else if (b == ESCAPE_BYTE) {
				escapedBytes[j] = ESCAPE_BYTE;
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
	
	public static ByteBuffer preprocessIncoming(IoBuffer in, int length) throws Exception {
		byte[] bytes = MessageUtilities.escapeIn(in, length);

		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.order(ByteOrder.BIG_ENDIAN);

		return buffer;
	}

	public static short detectMessageId(ByteBuffer buffer) {
		short messageId = buffer.getShort();

		buffer.position(buffer.position() - 2);

		return messageId;
	}

	public static void initializeHeader(TekMessage message, ByteBuffer buffer) throws Exception {
		message.setMessageType(buffer.getShort());

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
		}
	}
	
	public static void initializeFooter(TekMessage message, ByteBuffer buffer) {
		buffer.position(buffer.limit() - 1);
		message.setCheckCode(buffer.get());
	}
}
