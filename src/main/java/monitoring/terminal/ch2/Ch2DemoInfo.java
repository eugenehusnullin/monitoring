package monitoring.terminal.ch2;

import java.util.Date;

public class Ch2DemoInfo {
	private Date lastDateCoord;
	private Double lat = null;
	private Double lon = null;
	private String vin = null;
	private boolean oldVersion = false;

	private boolean detachAlarmed = false;
	private boolean vinChangeAlarmed = false;

	public boolean changeVersion() {
		boolean result = oldVersion;
		oldVersion = !oldVersion;
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

	public boolean isDetachAlarmed() {
		return detachAlarmed;
	}

	public void setDetachAlarmed(boolean detachAlarmed) {
		this.detachAlarmed = detachAlarmed;
	}

	public boolean isVinChangeAlarmed() {
		return vinChangeAlarmed;
	}

	public void setVinChangeAlarmed(boolean vinChangeAlarmed) {
		this.vinChangeAlarmed = vinChangeAlarmed;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
}
