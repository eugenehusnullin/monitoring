package ru.gm.munic.tekobd2;

import org.junit.Test;

public class MessageTest {
	
	@Test
	public void testParse() {
		// 3000 0019 814087547576 0010 00000000000000000000000000000000000000000000000000 28
		// B000 0003 814087547576 0010 001001 13
		String s = "3000001981408754757600100000000000000000000000000000000000000000000000000028";
		byte[] inBytes = Decoder.hexStringToByteArray(s);
		try {
			Message message = Message.parseMessage(inBytes, 0, inBytes.length);
			byte[] outBytes = message.makeResponse();
			String outString = Decoder.bytesToHex(outBytes);
			System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
