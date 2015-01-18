package monitoring.terminal.tek.messages.domain.trip;

public class SatellitePosition {

	// fields for tripDataMessageId == 0xA1
	private int latitude; // The latitude value in the unit of degree shall be
							// multiplied by 10^6 and corrected in a million
							// degree
	private int longitude; // The longitude value in the unit of degree shall be
							// multiplied by 10^6 and corrected in a million
							// degree
	private short elevation; // Altitude, unit: m
	private short speed; // 1/10km/h
	private short direction; // 0-359, the true north is 0, clockwise

	public int getRawLatitude() {
		return latitude;
	}

	public int getRawLongitude() {
		return longitude;
	}
	
	public double getLatitude() {
		return latitude / 1000000;
	}

	public double getLongitude() {
		return longitude / 1000000;
	}

	public short getElevation() {
		return elevation;
	}

	public short getRawSpeed() {
		return speed;
	}
	
	public double getSpeed() {
		return (double) (speed / 10);
	}

	public short getDirection() {
		return direction;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public void setElevation(short elevation) {
		this.elevation = elevation;
	}

	public void setSpeed(short speed) {
		this.speed = speed;
	}

	public void setDirection(short direction) {
		this.direction = direction;
	}
}
