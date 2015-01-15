package monitoring.handler.wialon;

import java.text.SimpleDateFormat;
import java.util.Map.Entry;

class WialonMessageFormatter {

	WialonMessage formatt(DataPacket dataPacket) {

		StringBuilder sb = new StringBuilder();
		insertPair("terminalId", Long.toString(dataPacket.getTerminalId()), sb);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		insertPair("recorded_at", dateFormat.format(dataPacket.getDate()), sb);

		if (dataPacket.getLat() != null) {
			insertPair("lat", String.valueOf(dataPacket.getLat()), sb);
		}
		if (dataPacket.getLon() != null) {
			insertPair("lon", String.valueOf(dataPacket.getLon()), sb);
		}
		if (dataPacket.getCourse() != null) {
			insertPair("dir", String.valueOf(dataPacket.getCourse()), sb);
		}
		if (dataPacket.getSpeed() != null) {
			insertPair("speed", String.valueOf(dataPacket.getSpeed()), sb);
		}
		if (dataPacket.getSatellites() != null) {
			insertPair("sats", String.valueOf(dataPacket.getSatellites()), sb);
		}
		if (dataPacket.getAccuracy() != null) {
			insertPair("accuracy", String.valueOf(dataPacket.getAccuracy()), sb);
		}
		if (dataPacket.getAltitude() != null) {
			insertPair("altitude", String.valueOf(dataPacket.getAltitude()), sb);
		}
		
		for (Entry<String, String> entry : dataPacket.getParams().entrySet()) {
			insertPair(entry.getKey(), entry.getValue(), sb);
		}

		WialonMessage wialonMessage = new WialonMessage();
		wialonMessage.setTerminalId(dataPacket.getTerminalId());
		wialonMessage.setStrMessage(sb.toString());
		return wialonMessage;
	}

	private void insertPair(String key, String value, StringBuilder sb) {
		sb.append("|");
		sb.append(key);
		sb.append("=");
		sb.append(value);
	}
}
