package monitoring.terminal.munic;

import monitoring.domain.Message;
import monitoring.handler.MessageFormatter;
import monitoring.handler.wialon.DataPacket;
import monitoring.terminal.munic.utils.PositionHelper;

public class MunicWialonMessageFormatter implements MessageFormatter {

	@Override
	public DataPacket fromTerminalFormatt(Message message) {
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

		DataPacket dataPacket = new DataPacket();
		PositionHelper.initPosition(dataPacket, municMessage);
		return dataPacket;
	}

	@Override
	public Message toTerminalFormatt(Message message) {
		return null;
	}

}
