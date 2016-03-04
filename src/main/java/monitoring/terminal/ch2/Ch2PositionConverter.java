package monitoring.terminal.ch2;

import monitoring.domain.Message;
import monitoring.handler.PositionConverter;
import monitoring.handler.position.Position;
import monitoring.terminal.munic.utils.PositionHelper;

public class Ch2PositionConverter implements PositionConverter {

	@Override
	public Position convert(Message message) {
		if (!(message instanceof Ch2Message)) {
			return null;
		}
		
		Ch2Message m = (Ch2Message) message;
		if (m.getLongitude() == null || m.getLatitude() == null) {
			return null;
		}
		
		Position position = new Position();
		PositionHelper.initPosition(position, m);
		return position;
	}

}
