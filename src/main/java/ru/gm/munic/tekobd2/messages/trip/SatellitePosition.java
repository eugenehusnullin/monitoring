package ru.gm.munic.tekobd2.messages.trip;

import java.nio.ByteBuffer;

import ru.gm.munic.domain.IHasPosition;
import ru.gm.munic.domain.Position;

public class SatellitePosition implements ISubMessage, IHasPosition {

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

	@Override
	public void parse(ByteBuffer bb, short length) {
		latitude = bb.getInt();
		longitude = bb.getInt();
		elevation = bb.getShort();
		speed = bb.getShort();
		direction = bb.getShort();
	}

	@Override
	public Position getPosition() {
		Position position = new Position();

		position.setSpeed((double) (speed / 10));
		position.setCourse((double) direction);
		position.setAltitude((double) elevation);
		position.setLat((double) (latitude / 1000000));
		position.setLon((double) (longitude / 1000000));

		return null;
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
