package monitoring.terminal.ch2;

import java.util.Date;

public class Ch2DemoInfo {
	private Date lastDateCoord;
	private Date disconnectedDate;
	private Double lat = null;
	private Double lon = null;
	private String vin = null;
	private Long mileage = null;

	private boolean oldVinVersion = false;

	private boolean detached = false;
	private boolean vinChanged = false;
	private boolean disconnected = false;

	private Ch2TerminalSession session;

	private long cntMileage = 100;
	private long cntVin = 100;
	
	private static long mileagePeriod = 109;
	private static long vinPeriod = 100;

	public boolean needMileage() {
		long mod = cntMileage % mileagePeriod;
		cntMileage++;
		return mod == 0;
	}
	
	public boolean needVin() {
		long mod = cntVin % vinPeriod;
		cntVin++;
		return mod == 0;
	}

	public boolean changeVinVersion() {
		boolean result = oldVinVersion;
		oldVinVersion = !oldVinVersion;
		return result;
	}

	public Date getLastDateCoord() {
		return lastDateCoord;
	}

	public void setLastDateCoord(Date lastDateCoord) {
		this.lastDateCoord = lastDateCoord;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public boolean isDetached() {
		return detached;
	}

	public void setDetached(boolean detachAlarmed) {
		this.detached = detachAlarmed;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public Long getMileage() {
		return mileage;
	}

	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}

	public Ch2TerminalSession getSession() {
		return session;
	}

	public void setSession(Ch2TerminalSession session) {
		this.session = session;
	}

	public boolean isVinChanged() {
		return vinChanged;
	}

	public void setVinChanged(boolean vinChanged) {
		this.vinChanged = vinChanged;
	}

	public boolean isDisconnected() {
		return disconnected;
	}

	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
	}

	public Date getDisconnectedDate() {
		return disconnectedDate;
	}

	public void setDisconnectedDate(Date disconnectedDate) {
		this.disconnectedDate = disconnectedDate;
	}
}
