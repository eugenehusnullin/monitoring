package ru.gm.munic.tekobd2.messages.trip;

import java.nio.ByteBuffer;

public class AlarmData implements ISubMessage {
	class Alarm {
		public byte type;
		public byte state;
		public short content;
	}

	// fields for tripDataMessageId == 0xA4
	private byte alarmQuantity;
	private Alarm[] alarmArray;

	@Override
	public void parse(ByteBuffer bb, short length) {
		alarmQuantity = bb.get();
		alarmArray = new Alarm[alarmQuantity];

		for (int i = 0; i < alarmQuantity; i++) {
			Alarm alarm = new Alarm();
			alarm.type = bb.get();
			alarm.state = bb.get();
			alarm.content = bb.getShort();

			alarmArray[i] = alarm;
		}
	}
}
