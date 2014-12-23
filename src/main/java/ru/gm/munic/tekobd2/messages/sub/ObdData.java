package ru.gm.munic.tekobd2.messages.sub;

import java.nio.ByteBuffer;

public class ObdData implements ISubMessage {

	// fields for tripDataMessageId == 0xA0
	private int tripId;
	private byte timeInterval;
	private byte total;

	@Override
	public void parse(ByteBuffer bb, short length) {
		tripId = bb.getInt();
		timeInterval = bb.get();
		total = bb.get();
		
		for (int i = 0; i < total; i++) {
			byte spLength = bb.get();

			int spRetrived = 0;
			while (spLength > spRetrived) {
				byte pid = bb.get();
				byte[] content = new byte[bb.get()];
				bb.get(content);
				
				spRetrived += 2 + content.length;
			}
		}
	}

	public int getTripId() {
		return tripId;
	}

	public byte getTimeInterval() {
		return timeInterval;
	}

	public byte getTotal() {
		return total;
	}
}
