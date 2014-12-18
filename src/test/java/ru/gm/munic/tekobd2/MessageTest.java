package ru.gm.munic.tekobd2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MessageTest {
	
	@Test
	public void testRegistrationPacket() {
		// 3000 0019 814087547576 0010 00000000000000000000000000000000000000000000000000 28
		// B000 0003 814087547576 0010 001001 13
		// B000 0006 814087547576 0010 00100053434F F8
		// B000 0006 814087547576 0010 00100053434F F8
		String s = "3000001981408754757600100000000000000000000000000000000000000000000000000028";
		byte[] inBytes = Decoder.hexStringToByteArray(s);
		try {
			Message message = Message.parseMessage(inBytes, 0, inBytes.length);
			assertEquals(message.getId(), 0x3000);
			assertEquals(message.getBodyLength(), 0x19);
			assertEquals(message.getTerminalId(), 814087547576L);
			
			byte[] outBytes = message.makeResponse();
			String outString = Decoder.bytesToHex(outBytes);
			assertEquals("B0000006814087547576001000100053434FF8", outString);
			//System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLoginPacket() {
		// 3002 0003 814087351432 000E 53434F 35
		String s = "30020003814087351432000E53434F35";
		byte[] inBytes = Decoder.hexStringToByteArray(s);
		try {
			Message message = Message.parseMessage(inBytes, 0, inBytes.length);
			assertEquals(message.getId(), 0x3002);
			assertEquals(message.getBodyLength(), 0x3);
			assertEquals(message.getTerminalId(), 814087351432L);
			
			byte[] outBytes = message.makeResponse();
			String outString = Decoder.bytesToHex(outBytes);
			assertEquals("B0030005814087351432000E000E300200D1", outString);
			//System.out.println(outString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
