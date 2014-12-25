package ru.gm.munic.tekobd2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

import ru.gm.munic.tekobd2.messages.IResponse;
import ru.gm.munic.tekobd2.messages.LoginMessage;
import ru.gm.munic.tekobd2.messages.RegistrationMessage;
import ru.gm.munic.tekobd2.messages.TripDataMessage;

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
			RegistrationMessage message = (RegistrationMessage) Decoder.process(in);
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
			RegistrationMessage message = (RegistrationMessage) Decoder.process(in);
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
			LoginMessage message = (LoginMessage) Decoder.process(in);
			assertEquals(message.getMessageId(), 0x3002);
			assertEquals(message.getBodyLength(), 0x3);
			assertEquals(message.getTerminalId(), 814087351432L);

			if (message instanceof IResponse) {
				byte[] outBytes = ((IResponse) message).makeResponse();
				String outString = ByteUtilities.bytesToHex(outBytes);
				// System.out.println(outString);
				assertEquals("B0030005814087351432000E000E300200D1", outString);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testTripDataPacket() {
		String s = "7E30060107814087547576043B1412101610290001FF2DA000DE1412002A01120BFF0203800C0219330D01340BFF0202F20C0218960D01330BFF0203010C0218960D01330BFF0202E60C0218080D01330BFF0202F40C0218080D01320BFF0203C80C0218580D01320BFF0203C80C0218580D01320BFF0203C80C0218580D01320BFF0203CE0C0218170D01310BFF0203CE0C0218170D01310BFF0203D50C0218130D01310BFF0203D50C0218130D01310BFF02036C0C0218130D01310BFF02036C0C0218510D01310BFF02036C0C0218510D01310BFF02038A0C02180E0D01310BFF0203320C0217C70D01310BFF0203320C0217C70D0131A1000E0351B27D0202478B4E0000020900CCA200080002500213DDCE8C8B7E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			// Message message = Message.parseMessage(in, inBytes.length);
			TripDataMessage message = (TripDataMessage) Decoder.process(in);
			assertEquals(message.getMessageId(), 0x3006);
			assertEquals(message.getBodyLength(), 0x107);
			assertEquals(message.getTerminalId(), 814087547576L);
			
			assertNotNull(message.getObdData());
			assertNotNull(message.getSatellitePosition());
			assertNotNull(message.getBaseStationPosition());
			assertNull(message.getAlarmData());
			assertNull(message.getDtcData());

			if (message instanceof IResponse) {
				byte[] outBytes = ((IResponse) message).makeResponse();
				String outString = ByteUtilities.bytesToHex(outBytes);
				//System.out.println(outString);
				assertEquals("B0030005814087547576043B043B30060091", outString);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
