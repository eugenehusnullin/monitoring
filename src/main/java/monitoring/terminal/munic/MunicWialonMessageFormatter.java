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
		
		if (municMessage.getDioIgnition() != null) {
			dataPacket.getParams().put("ignition", municMessage.getDioIgnition().booleanValue() ? "1" : "0");
		}
		
		if (municMessage.getOdometer() != null) {
			dataPacket.getParams().put("odometer", municMessage.getOdometer().toString());
		}
		return dataPacket;
	}

	@Override
	public Message toTerminalFormatt(Message message) {
		return null;
	}

}
