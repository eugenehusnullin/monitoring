package monitoring.utils;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

public class ByteUtilities {
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

	public static String ioBufferToHex(IoBuffer in) {
		return byteBufferToHex(in.buf());
	}

	public static String ioBufferToHex(IoBuffer in, int length) {
		return byteBufferToHex(in.buf(), length);
	}

	public static String byteBufferToHex(ByteBuffer bb) {
		return byteBufferToHex(bb, bb.remaining());
	}

	public static String byteBufferToHex(ByteBuffer bb, int length) {
		byte[] bytes = new byte[length];
		bb.get(bytes);
		return bytesToHex(bytes);
	}

	public static byte[] hexToBytes(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static int searchInBuffer(IoBuffer in, byte key) {
		while (in.hasRemaining()) {
			if (in.get() == key) {
				return in.position() - 1;
			}
		}
		return -1;
	}

	public static int countInBuffer(IoBuffer in, byte key, int length) {
		int result = 0;
		int i = 0;
		while (in.hasRemaining() && i < length) {
			if (in.get() == key) {
				result++;
			}
			i++;
		}
		return result;
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

	public static String bcdToString(ByteBuffer bb, int length) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			byte b = bb.get();
			byte high = (byte) (b & 0xf0);
			high = (byte) (high >>> 4);
			high = (byte) (high & 0x0f);

			byte low = (byte) (b & 0x0f);

			sb.append(high);
			sb.append(low);
		}

		return sb.toString();
	}

	public static byte[] decimalToBcd(long num) {
		int digits = 0;

		long temp = num;
		while (temp != 0) {
			digits++;
			temp /= 10;
		}

		int byteLen = digits % 2 == 0 ? digits / 2 : (digits + 1) / 2;
		boolean isOdd = digits % 2 != 0;

		byte bcd[] = new byte[byteLen];

		for (int i = 0; i < digits; i++) {
			byte tmp = (byte) (num % 10);

			if (i == digits - 1 && isOdd)
				bcd[i / 2] = tmp;
			else if (i % 2 == 0)
				bcd[i / 2] = tmp;
			else {
				byte foo = (byte) (tmp << 4);
				bcd[i / 2] |= foo;
			}

			num /= 10;
		}

		for (int i = 0; i < byteLen / 2; i++) {
			byte tmp = bcd[i];
			bcd[i] = bcd[byteLen - i - 1];
			bcd[byteLen - i - 1] = tmp;
		}

		return bcd;
	}

	public static String hexToAscii(String hex) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
			String str = hex.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}
}
