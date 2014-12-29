package monitoring.tek.messages.domain.trip;


public class DtcData {
	// fields for tripDataMessageId == 0xA3
	private byte dtcQuantity;

	public byte getDtcQuantity() {
		return dtcQuantity;
	}

	public void setDtcQuantity(byte dtcQuantity) {
		this.dtcQuantity = dtcQuantity;
	}
}
