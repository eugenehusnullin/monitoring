package monitoring.terminal.tek.utils;

import monitoring.handler.position.Position;
import monitoring.terminal.tek.messages.domain.TripDataMessage;
import monitoring.terminal.tek.messages.domain.trip.SatellitePosition;

public class PositionHelper {

	public static void initPosition(Position position, TripDataMessage tripDataMessage) {
		SatellitePosition satellitePosition = tripDataMessage.getSatellitePosition();

		position.setTerminalId(tripDataMessage.getTerminalId());
		position.setDate(tripDataMessage.getUploadingTime());
		position.setSpeed(satellitePosition.getSpeed());
		position.setCourse((double) satellitePosition.getDirection());
		position.setAltitude((double) satellitePosition.getElevation());
		position.setLat(satellitePosition.getLatitude());
		position.setLon(satellitePosition.getLongitude());
		position.setSatellites((int) tripDataMessage.getSatelites());
	}
}
