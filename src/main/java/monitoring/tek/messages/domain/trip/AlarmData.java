package monitoring.tek.messages.domain.trip;

public class AlarmData {
	public static class Alarm {
		public byte type;
		public byte state;
		public short content;
	}

	// fields for tripDataMessageId == 0xA4
	private byte alarmQuantity;
	private Alarm[] alarmArray;

	public byte getAlarmQuantity() {
		return alarmQuantity;
	}

	public void setAlarmQuantity(byte alarmQuantity) {
		this.alarmQuantity = alarmQuantity;
	}

	public Alarm[] getAlarmArray() {
		return alarmArray;
	}

	public void setAlarmArray(Alarm[] alarmArray) {
		this.alarmArray = alarmArray;
	}

}
