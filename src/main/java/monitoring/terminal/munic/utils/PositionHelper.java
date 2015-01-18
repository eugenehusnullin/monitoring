package monitoring.terminal.munic.utils;

import monitoring.handler.position.Position;
import monitoring.terminal.munic.MunicMessage;

public class PositionHelper {
	public static void initPosition(Position position, MunicMessage municMessage) {
		position.setTerminalId(municMessage.getTerminalId());
		if (municMessage.getDirection() != null) {
			position.setCourse(municMessage.getDirection().doubleValue());
		}
		position.setDate(municMessage.getRecordedAt());
		if (municMessage.getLatitude() != null) {
			position.setLat(municMessage.getLatitude());
		}
		if (municMessage.getLongitude() != null) {
			position.setLon(municMessage.getLongitude());
		}
		if (municMessage.getSpeed() != null) {
			position.setSpeed(municMessage.getSpeed());
		}
	}
}
