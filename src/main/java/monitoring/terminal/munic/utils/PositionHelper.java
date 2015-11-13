package monitoring.terminal.munic.utils;

import monitoring.handler.position.Position;
import monitoring.terminal.ch2.Ch2Message;
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
		if (municMessage.getGprmcValid() != null) {
			position.setGpsValid(municMessage.getGprmcValid());
		}
	}
	
	public static void initPosition(Position position, Ch2Message m) {
		position.setTerminalId(m.getTerminalId());
		
		position.setDate(m.getDate());
		if (m.getLatitude() != null) {
			position.setLat(m.getLatitude());
		}
		if (m.getLongitude() != null) {
			position.setLon(m.getLongitude());
		}
		if (m.getSpeed() != null) {
			position.setSpeed(m.getSpeed());
		}
		if (m.getValidLocation() != null) {
			position.setGpsValid(m.getValidLocation());
		}
		if (m.getCourse() != null) {
			position.setCourse((double) m.getCourse());
		}
	}
}
