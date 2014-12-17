package ru.gm.munic.tekobd2;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Decoder extends CumulativeProtocolDecoder {

	private static final Logger logger = LoggerFactory.getLogger(Decoder.class);
	private static final int MIN_MESS_SIZE = 15;
	public static final byte MARKER_BYTE = (byte) 0x7e;
	public static final byte ESCAPE_BYTE = (byte) 0x7d;

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		byte[] bytes = new byte[in.remaining()];
		in.get(bytes);

		if (logger.isDebugEnabled()) {
			String str = bytesToHex(bytes);
			logger.debug("BYTES:: " + str);
		}

		int startIndex = searchByte(bytes, 0, MARKER_BYTE);
		if (startIndex != -1) {
			// если стыковка двух пакетов смещаем startIndex
			if (bytes.length > startIndex + 1) {
				if (bytes[startIndex + 1] == MARKER_BYTE) {
					startIndex++;
				}
			}

			int endIndex = searchByte(bytes, startIndex + 1, MARKER_BYTE);
			if (endIndex != -1) {
				if (endIndex - startIndex >= MIN_MESS_SIZE) {
					// покажем пакет в логе
					if (logger.isDebugEnabled()) {
						String str = bytesToHex(bytes, startIndex, endIndex - startIndex);
						logger.debug("PACKET:: " + str);
					}

					// смещаем сразу за endIndex, там может быть и пустота, но
					// мы не берем данные
					in.position(in.position() - (bytes.length - 1 - endIndex));

					try {
						Message message = Message.parseMessage(bytes, startIndex + 1, endIndex - startIndex - 2);
						out.write(message);
					} catch (Exception e) {
						String str = bytesToHex(bytes, startIndex, endIndex - startIndex);
						logger.warn("WRONG SIZE OF BODY :: " + str);
					}

					return true;

				} else {
					// неправильный пакет
					String wrongPacket = bytesToHex(bytes, startIndex, endIndex - startIndex);
					logger.warn("Wrong packet: " + wrongPacket);

					// смещаем так что бы в след. итерации начать с endIndex -
					// быть может это начало нормального пакета
					in.position(in.position() - (bytes.length - endIndex));
				}
			} else {
				in.position(in.position() - (bytes.length - startIndex));
			}
		}

		return false;
	}

	private int searchByte(byte[] bytes, int start, byte key) {
		for (int i = start; i < bytes.length; i++) {
			if (bytes[i] == key) {
				return i;
			}
		}
		return -1;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes, int offset, int length) {

		char[] hexChars = new char[length * 2];

		for (int j = 0; j < length; j++) {
			int v = bytes[offset + j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexChars);
	}

	public static String bytesToHex(byte[] bytes) {
		return bytesToHex(bytes, 0, bytes.length);
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
}
