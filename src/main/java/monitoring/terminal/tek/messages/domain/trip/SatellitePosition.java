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

	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public short getElevation() {
		return elevation;
	}

	public short getSpeed() {
		return speed;
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
