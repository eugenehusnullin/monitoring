package monitoring.handler.wialon;

import java.text.SimpleDateFormat;

public class WialonMessageFormatter {

	WialonMessage formatt(DataPacket dataPacket) {

		StringBuilder sb = new StringBuilder();
		insertPair("terminalId", Long.toString(dataPacket.getTerminalId()), sb);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		insertPair("recorded_at", dateFormat.format(dataPacket.getDate()), sb);
		insertPair("lat", String.valueOf(dataPacket.getLat()), sb);
		insertPair("lon", String.valueOf(dataPacket.getLon()), sb);
		insertPair("dir", String.valueOf(dataPacket.getCourse()), sb);
		insertPair("speed", String.valueOf(dataPacket.getSpeed()), sb);
		insertPair("sats", String.valueOf(dataPacket.getSatellites()), sb);
		
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
