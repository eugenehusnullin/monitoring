package ru.gm.munic.tekobd2;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Decoder extends CumulativeProtocolDecoder {

	private static final Logger logger = LoggerFactory.getLogger(Decoder.class);
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		byte[] bytes = new byte[in.remaining()];
		in.get(bytes);
		String str = bytesToHex(bytes);
		logger.error("BYTES:: " + str);
		out.write(str);
		return true;
		
		

//		// search start of message
//		while (in.get() != 0x7e) {
//			if (!in.hasRemaining()) {
//				return false;
//			}
//		}
//
//		// ok, now we have start of message
//		// search end of message
//		int count = 0;
//		while (in.get() != 0x7e) {
//			count++;
//			if (!in.hasRemaining()) {
//				// return pointer to the start of the message
//				in.position(in.position() - count);
//				return false;
//			}
//		}
//
//		byte[] messageBytes = new byte[count];
//		in.position(in.position() - count);
//		in.get(messageBytes);
//
//		parseMessage(messageBytes);
//		// out.write(object);
//		// return true;
//
//		return false;
	}

	private void parseMessage(byte[] messageBytes) {

	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
