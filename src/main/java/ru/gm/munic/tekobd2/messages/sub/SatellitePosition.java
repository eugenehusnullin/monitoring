package ru.gm.munic.tekobd2.messages.sub;

import java.nio.ByteBuffer;

public class SatellitePosition implements ISubMessage {

	// fields for tripDataMessageId == 0xA1
	private int latitude;
	private int longitude;
	private short elevation;
	private short speed;
	private short direction;

	@Override
	public void parse(ByteBuffer bb, short length) {
		latitude = bb.getInt();
		longitude = bb.getInt();
		elevation = bb.getShort();
		speed = bb.getShort();
		direction = bb.getShort();
	}

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

}
