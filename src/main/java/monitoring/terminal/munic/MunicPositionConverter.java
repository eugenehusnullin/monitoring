package monitoring.terminal.munic;

import monitoring.domain.Message;
import monitoring.handler.PositionConverter;
import monitoring.handler.position.Position;
import monitoring.terminal.munic.utils.PositionHelper;

public class MunicPositionConverter implements PositionConverter {

	@Override
	public Position convert(Message message) {
		if (!(message instanceof MunicMessage)) {
			return null;
		}

		MunicMessage municMessage = (MunicMessage) message;
		if (!municMessage.isTrackEvent()) {
			return null;
		}

		if (!municMessage.hasGeo()) {
			return null;
		}
		
		Position position = new Position();
		PositionHelper.initPosition(position, municMessage);
		return position;
	}

}
