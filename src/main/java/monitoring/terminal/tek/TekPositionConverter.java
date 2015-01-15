package monitoring.terminal.tek;

import monitoring.domain.Message;
import monitoring.handler.PositionConverter;
import monitoring.handler.position.Position;
import monitoring.terminal.tek.messages.domain.TripDataMessage;
import monitoring.terminal.tek.utils.PositionHelper;

class TekPositionConverter implements PositionConverter {

	@Override
	public Position convert(Message message) {
		if (message instanceof TripDataMessage) {
			TripDataMessage tripDataMessage = (TripDataMessage) message;

			if (tripDataMessage.getSatellitePosition() != null) {
				Position position = new Position();
				PositionHelper.initPosition(position, tripDataMessage);
				return position;
			}
		}

		return null;
	}
}
