package monitoring.terminal.tek.messages.domain.trip;

public class BaseStationPosition {
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

	public void setMcc(int mcc) {
		this.mcc = mcc;
	}

	public void setMnc(byte mnc) {
		this.mnc = mnc;
	}

	public void setLac(short lac) {
		this.lac = lac;
	}

	public void setCellid(short cellid) {
		this.cellid = cellid;
	}

}
