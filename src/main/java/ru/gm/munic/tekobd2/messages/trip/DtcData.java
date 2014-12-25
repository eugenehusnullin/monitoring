package ru.gm.munic.tekobd2.messages.trip;

import java.nio.ByteBuffer;

public class DtcData implements ISubMessage {
	// fields for tripDataMessageId == 0xA3
	private byte dtcQuantity;

	public byte getDtcQuantity() {
		return dtcQuantity;
	}

	@Override
	public void parse(ByteBuffer bb, short length) {
		// TODO Auto-generated method stub

	}

}
