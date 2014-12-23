package ru.gm.munic.tekobd2;

import static org.junit.Assert.assertEquals;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

import ru.gm.munic.tekobd2.messages.IResponse;
import ru.gm.munic.tekobd2.messages.Message;

public class MessageTest {

	@Test
	public void testRegistrationPacket() {

		// 3000 0019 814087547576 0010
		// 00000000000000000000000000000000000000000000000000 28
		// B000 0003 814087547576 0010 001001 13
		// B000 0006 814087547576 0010 00100053434F F8
		// B000 0006 814087547576 0010 00100053434F F8
		String s = "7E30000019814087547576001000000000000000000000000000000000000000000000000000287E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			// Message message = Message.parseMessage(in, inBytes.length);
			Message message = Decoder.process(in);
			assertEquals(message.getMessageId(), 0x3000);
			assertEquals(message.getBodyLength(), 0x19);
			assertEquals(message.getTerminalId(), 814087547576L);

			if (message instanceof IResponse) {
				byte[] outBytes = ((IResponse) message).makeResponse();
				String outString = ByteUtilities.bytesToHex(outBytes);
				assertEquals("B0000006814087547576001000100053434FF8", outString);
				// System.out.println(outString);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testRegistrationPacketLength0() {
		// 3000 0019 814087547576 0010
		// 00000000000000000000000000000000000000000000000000 28
		// B000 0003 814087547576 0010 001001 13
		// B000 0006 814087547576 0010 00100053434F F8
		// B000 0006 814087547576 0010 00100053434F F8
		// B000 0006 814087547576 0010 00100053434F F8
		String s = "7E300000008140875475760010287E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			// Message message = Message.parseMessage(in, inBytes.length);
			Message message = Decoder.process(in);
			assertEquals(message.getMessageId(), 0x3000);
			assertEquals(message.getBodyLength(), 0x0);
			assertEquals(message.getTerminalId(), 814087547576L);

			if (message instanceof IResponse) {
				byte[] outBytes = ((IResponse) message).makeResponse();
				String outString = ByteUtilities.bytesToHex(outBytes);
				assertEquals("B0000006814087547576001000100053434FF8", outString);
				// System.out.println(outString);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testLoginPacket() {
		// 3002 0003 814087351432 000E 53434F 35
		String s = "7E30020003814087351432000E53434F357E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			// Message message = Message.parseMessage(in, inBytes.length);
			Message message = Decoder.process(in);
			assertEquals(message.getMessageId(), 0x3002);
			assertEquals(message.getBodyLength(), 0x3);
			assertEquals(message.getTerminalId(), 814087351432L);

			if (message instanceof IResponse) {
				byte[] outBytes = ((IResponse) message).makeResponse();
				String outString = ByteUtilities.bytesToHex(outBytes);
				assertEquals("B0030005814087351432000E000E300200D1", outString);
				// System.out.println(outString);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
