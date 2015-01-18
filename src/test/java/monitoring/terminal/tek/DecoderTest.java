package monitoring.terminal.tek;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import monitoring.terminal.tek.messages.MessageParser;
import monitoring.terminal.tek.messages.domain.LoginMessage;
import monitoring.terminal.tek.messages.domain.RegistrationMessage;
import monitoring.terminal.tek.messages.domain.TripDataMessage;
import monitoring.terminal.tek.messages.factory.MessageFactoryDetector;
import monitoring.utils.ByteUtilities;

import org.apache.mina.core.buffer.IoBuffer;
import org.junit.Test;

public class DecoderTest {
	
	private Decoder createDecoder() {
		MessageFactoryDetector messageFactoryDetector = new MessageFactoryDetector();
		MessageParser messageParser = new MessageParser();
		messageParser.setMessageFactoryDetector(messageFactoryDetector);
		Decoder decoder = new Decoder();
		decoder.setMessageParser(messageParser);
		
		return decoder;
	}

	@Test
	public void testRegistrationPacket() {
		/*
		 * 7E delimeter
		 * 3000 message type
		 * 0019 header
		 * 814087547576 terminalId
		 * 0010 serialNumber
		 * 00000000000000000000000000000000000000000000000000 body
		 * 28 check code
		 * 7E delimeter
		 */
		String s = "7E30000019814087547576001000000000000000000000000000000000000000000000000000287E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			Decoder decoder = createDecoder();
			RegistrationMessage message = (RegistrationMessage) decoder.process(in);

			assertEquals(message.getMessageType(), 0x3000);
			assertEquals(message.getBodyLength(), 0x19);
			assertEquals(message.getTerminalId(), 814087547576L);
			assertEquals(message.getCheckCode(), 0x28);
			assertEquals(message.getSerialNumber(), 0x10);
			assertFalse(message.getSubPackage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTripDataPacket() {
		String s = "7E30060107814087547576043B1412101610290001FF2DA000DE1412002A01120BFF0203800C0219330D01340BFF0202F20C0218960D01330BFF0203010C0218960D01330BFF0202E60C0218080D01330BFF0202F40C0218080D01320BFF0203C80C0218580D01320BFF0203C80C0218580D01320BFF0203C80C0218580D01320BFF0203CE0C0218170D01310BFF0203CE0C0218170D01310BFF0203D50C0218130D01310BFF0203D50C0218130D01310BFF02036C0C0218130D01310BFF02036C0C0218510D01310BFF02036C0C0218510D01310BFF02038A0C02180E0D01310BFF0203320C0217C70D01310BFF0203320C0217C70D0131A1000E0351B27D0202478B4E0000020900CCA200080002500213DDCE8C8B7E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			Decoder decoder = createDecoder();
			TripDataMessage message = (TripDataMessage) decoder.process(in);
			assertEquals(message.getMessageType(), 0x3006);
			assertEquals(message.getBodyLength(), 0x107);
			assertEquals(message.getTerminalId(), 814087547576L);
			
			assertNotNull(message.getObdData());
			assertNotNull(message.getSatellitePosition());
			assertNotNull(message.getBaseStationPosition());
			assertNull(message.getAlarmData());
			assertNull(message.getDtcData());
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRegistrationPacketLength0() {
		String s = "7E300000008140875475760010287E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			Decoder decoder = createDecoder();
			RegistrationMessage message = (RegistrationMessage) decoder.process(in);
			assertEquals(message.getMessageType(), 0x3000);
			assertEquals(message.getBodyLength(), 0x0);
			assertEquals(message.getTerminalId(), 814087547576L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testLoginPacket() {
		String s = "7E30020003814087351432000E53434F357E";
		byte[] inBytes = ByteUtilities.hexToBytes(s);
		IoBuffer in = IoBuffer.wrap(inBytes);
		try {
			Decoder decoder = createDecoder();
			LoginMessage message = (LoginMessage) decoder.process(in);
			assertEquals(message.getMessageType(), 0x3002);
			assertEquals(message.getBodyLength(), 0x3);
			assertEquals(message.getTerminalId(), 814087351432L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
