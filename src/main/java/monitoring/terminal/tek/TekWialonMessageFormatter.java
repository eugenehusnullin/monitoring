package monitoring.terminal.tek;

import monitoring.domain.Message;
import monitoring.handler.MessageFormatter;
import monitoring.handler.wialon.DataPacket;
import monitoring.handler.wialon.WialonMessage;
import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.terminal.tek.messages.domain.TripDataMessage;
import monitoring.terminal.tek.utils.PositionHelper;

public class TekWialonMessageFormatter implements MessageFormatter {

	@Override
	public DataPacket fromTerminalFormatt(Message message) {
		if (message instanceof TripDataMessage) {
			TripDataMessage tripDataMessage = (TripDataMessage) message;

			if (tripDataMessage.getSatellitePosition() != null) {
				DataPacket dataPacket = new DataPacket();
				PositionHelper.initPosition(dataPacket, tripDataMessage);
				return dataPacket;
			}
		}
		return null;
	}

	/**
	 * Incoming message is {@link WialonMessage}, output must be derived from
	 * {@link TekMessage}
	 */
	@Override
	public Message toTerminalFormatt(Message message) {
		return null;
	}

}
