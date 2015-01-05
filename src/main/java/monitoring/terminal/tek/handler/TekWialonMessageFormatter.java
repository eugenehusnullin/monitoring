package monitoring.terminal.tek.handler;

import monitoring.domain.Message;
import monitoring.handler.MessageFormatter;
import monitoring.handler.wialon.DataPacket;
import monitoring.handler.wialon.WialonMessage;
import monitoring.terminal.tek.messages.domain.TekMessage;
import monitoring.terminal.tek.messages.domain.TripDataMessage;
import monitoring.terminal.tek.messages.domain.trip.SatellitePosition;

public class TekWialonMessageFormatter implements MessageFormatter {

	@Override
	public DataPacket fromTerminalFormatt(Message message) {
		if (message instanceof TripDataMessage) {
			TripDataMessage tripDataMessage = (TripDataMessage) message;

			if (tripDataMessage.getSatellitePosition() != null) {
				SatellitePosition satellitePosition = tripDataMessage.getSatellitePosition();
				DataPacket dataPacket = new DataPacket();
				dataPacket.setTerminalId(tripDataMessage.getTerminalId());
				dataPacket.setDate(tripDataMessage.getUploadingTime());
				dataPacket.setSpeed(satellitePosition.getSpeed());
				dataPacket.setCourse((double) satellitePosition.getDirection());
				dataPacket.setAltitude((double) satellitePosition.getElevation());
				dataPacket.setLat(satellitePosition.getLatitude());
				dataPacket.setLon(satellitePosition.getLongitude());
				dataPacket.setSatellites((int) tripDataMessage.getSatelites());
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
