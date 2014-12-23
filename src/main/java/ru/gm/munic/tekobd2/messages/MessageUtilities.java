package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;

import org.apache.mina.core.buffer.IoBuffer;

import ru.gm.munic.tekobd2.ByteUtilities;
import ru.gm.munic.tekobd2.Decoder;

public class MessageUtilities {

	public static byte[] escapeIn(IoBuffer in, int length) throws Exception {
		int startPosition = in.position();
		int count = ByteUtilities.countInBuffer(in, Decoder.ESCAPE_BYTE, length);
		in.position(startPosition);
		byte[] bytes = new byte[length - count];

		int j = 0;
		int i = 0;
		while (in.hasRemaining() && i < length) {
			byte b = in.get();
			i++;

			if (b == Decoder.MARKER_BYTE) {
				break;
			}

			if (b == Decoder.ESCAPE_BYTE) {
				if (!in.hasRemaining()) {
					throw new Exception("Escape rules are broken! (not enough bytes)");
				}
				b = in.get();
				i++;

				if (b == 0x02) {
					bytes[j] = Decoder.MARKER_BYTE;

				} else if (b == 0x01) {
					bytes[j] = Decoder.ESCAPE_BYTE;

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
			if (b == Decoder.MARKER_BYTE || b == Decoder.ESCAPE_BYTE) {
				cnt++;
			}
		}
		bb.position(startPosition);

		byte[] escapedBytes = new byte[cnt];
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
}
