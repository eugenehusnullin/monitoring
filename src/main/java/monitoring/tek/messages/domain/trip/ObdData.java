package monitoring.tek.messages.domain.trip;

public class ObdData {

	// fields for tripDataMessageId == 0xA0
	private int tripId;
	private byte timeInterval;
	private byte total;

	public int getTripId() {
		return tripId;
	}

	public byte getTimeInterval() {
		return timeInterval;
	}

	public byte getTotal() {
		return total;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public void setTimeInterval(byte timeInterval) {
		this.timeInterval = timeInterval;
	}

	public void setTotal(byte total) {
		this.total = total;
	}
}
