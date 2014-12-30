package monitoring.terminal.tek.handler;

import monitoring.domain.Message;
import monitoring.handler.position.PositionConverter;
import monitoring.handler.position.domain.Position;
import monitoring.terminal.tek.messages.domain.TripDataMessage;
import monitoring.terminal.tek.messages.domain.trip.SatellitePosition;

public class TekPositionConverter implements PositionConverter {

	@Override
	public Position convert(Message message) {
		if (message instanceof TripDataMessage) {
			TripDataMessage tripDataMessage = (TripDataMessage) message;

			if (tripDataMessage.getSatellitePosition() != null) {
				SatellitePosition satellitePosition = tripDataMessage.getSatellitePosition();

				Position position = new Position();
				position.setSpeed((double) (satellitePosition.getSpeed() / 10));
				position.setCourse((double) satellitePosition.getDirection());
				position.setAltitude((double) satellitePosition.getElevation());
				position.setLat((double) (satellitePosition.getLatitude() / 1000000));
				position.setLon((double) (satellitePosition.getLongitude() / 1000000));
				position.setDate(tripDataMessage.getUploadingTime());
				position.setTerminalId(tripDataMessage.getTerminalId());
				position.setSatellites((int) tripDataMessage.getSatelites());

				return position;
			}
		}

		return null;
	}
}
