package ru.gm.munic.tekobd2.messages.sub;

import java.nio.ByteBuffer;

import ru.gm.munic.tekobd2.ByteUtilities;

public class BaseStationPosition implements ISubMessage {
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
