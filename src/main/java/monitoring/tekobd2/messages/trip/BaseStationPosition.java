package monitoring.tekobd2.messages.trip;

import java.nio.ByteBuffer;

import monitoring.tekobd2.ByteUtilities;

public class BaseStationPosition implements ISubMessage {
	/*
	 * For example: China MCC: 460, MNC of China Mobile GSM: 02, corresponding
	 * hexadecimal byte (bytes0~byte 3) indicating: 00 04 60 02; BYTE4~
	 * BYTE5-LAC (upper byte is in the front): BYTE6~ BYTE7-CELLID (upper byte
	 * is in the front): (Will consider adding to three groups base station area
	 * code afterwards.)
	 */
	// fields for tripDataMessageId == 0xA2
	private int mcc;
	private byte mnc;
	private short lac;
	private short cellid;

	@Override
	public void parse(ByteBuffer bb, short length) {
		mcc = Integer.parseInt(ByteUtilities.bcdToString(bb, 3));
		mnc = Byte.parseByte(ByteUtilities.bcdToString(bb, 1));
		lac = bb.getShort();
		cellid = bb.getShort();
	}

	public int getMcc() {
		return mcc;
	}

	public byte getMnc() {
		return mnc;
	}

	public short getLac() {
		return lac;
	}

	public short getCellid() {
		return cellid;
	}

}
