package monitoring.tek.messages.factory;

import java.nio.ByteBuffer;
import java.util.Calendar;

import monitoring.tek.ByteUtilities;
import monitoring.tek.messages.domain.TekMessageImpl;
import monitoring.tek.messages.domain.TripDataMessage;
import monitoring.tek.messages.domain.TripDataMessage.PositioningMode;
import monitoring.tek.messages.domain.trip.AlarmData;
import monitoring.tek.messages.domain.trip.AlarmData.Alarm;
import monitoring.tek.messages.domain.trip.BaseStationPosition;
import monitoring.tek.messages.domain.trip.DtcData;
import monitoring.tek.messages.domain.trip.ObdData;
import monitoring.tek.messages.domain.trip.SatellitePosition;

public class TripDataMessageFactory extends MessageFactory {

	@Override
	TekMessageImpl getNewTekMessage() {
		return new TripDataMessage();
	}

	@Override
	void initializeMessageBody(TekMessageImpl message, ByteBuffer buffer) {
		TripDataMessage tripDataMessage = (TripDataMessage) message;
		
		
		// uploading time
		String timeString = ByteUtilities.bcdToString(buffer, 6);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(timeString.substring(0, 2)), Integer.parseInt(timeString.substring(2, 4)),
				Integer.parseInt(timeString.substring(4, 6)), Integer.parseInt(timeString.substring(6, 8)),
				Integer.parseInt(timeString.substring(8, 10)), Integer.parseInt(timeString.substring(10)));
		calendar.add(Calendar.HOUR_OF_DAY, -8);
		tripDataMessage.setUploadingTime(calendar.getTime());

		// State word
		byte[] dword = new byte[4];
		buffer.get(dword);
		byte pmbyte = (byte) (dword[0] & 0b111);

		tripDataMessage.setPositioningMode(PositioningMode.valueOfMode(pmbyte));
		tripDataMessage.setSatelitePositioningStatus((byte) ((dword[0] >> 3) & 0b11));
		tripDataMessage.setBaseStationPositioningStatus(((dword[0] >> 5) & 0b1) == 1);
		tripDataMessage.setNorthern(((dword[0] >> 6) & 0b1) == 0);
		tripDataMessage.setEastern(((dword[0] >> 7) & 0b1) == 0);

		tripDataMessage.setSatelites((byte) (dword[1] & 0b1111));
		tripDataMessage.setGsmSignalValue((byte) ((dword[1] >> 4) & 0b1111));

		tripDataMessage.setObdEnabled((dword[2] & 0b1) == 1);

		while (buffer.remaining() > 1) {
			byte tripDataMessageId = buffer.get();
			short length = buffer.getShort();

			switch (tripDataMessageId) {
			case (byte) 0xA0:
				tripDataMessage.setObdData(parseObdData(buffer, length));
				break;

			case (byte) 0xA1:
				tripDataMessage.setSatellitePosition(parseSatellitePosition(buffer, length));
				break;

			case (byte) 0xA2:
				tripDataMessage.setBaseStationPosition(parseBaseStationPosition(buffer, length));
				break;

			case (byte) 0xA3:
				tripDataMessage.setDtcData(parseDtcdata(buffer, length));
				break;

			case (byte) 0xA4:
				tripDataMessage.setAlarmData(parseAlarm(buffer, length));
				break;

			default:
				break;
			}
		}
	}
	
	private SatellitePosition parseSatellitePosition(ByteBuffer buffer, short length) {
		SatellitePosition satellitePosition = new SatellitePosition();
		
		satellitePosition.setLatitude(buffer.getInt());
		satellitePosition.setLongitude(buffer.getInt());
		satellitePosition.setElevation(buffer.getShort());
		satellitePosition.setSpeed(buffer.getShort());
		satellitePosition.setDirection(buffer.getShort());
		
		return satellitePosition;
	}
	
	private ObdData parseObdData(ByteBuffer buffer, short length) {
		ObdData obdData = new ObdData();
		
		obdData.setTripId(buffer.getInt());
		obdData.setTimeInterval(buffer.get());
		obdData.setTotal(buffer.get());
		
		for (int i = 0; i < obdData.getTotal(); i++) {
			byte spLength = buffer.get();

			int spRetrived = 0;
			while (spLength > spRetrived) {
				@SuppressWarnings("unused")
				byte pid = buffer.get();
				byte[] content = new byte[buffer.get()];
				buffer.get(content);
				
				spRetrived += 2 + content.length;
			}
		}
		
		return obdData;
	}
	
	private AlarmData parseAlarm(ByteBuffer buffer, short length) {
		AlarmData alarmData = new AlarmData();
		
		alarmData.setAlarmQuantity(buffer.get());
		alarmData.setAlarmArray(new Alarm[alarmData.getAlarmQuantity()]);

		for (int i = 0; i < alarmData.getAlarmQuantity(); i++) {
			Alarm alarm = new Alarm();
			alarm.type = buffer.get();
			alarm.state = buffer.get();
			alarm.content = buffer.getShort();

			alarmData.getAlarmArray()[i] = alarm;
		}
		
		return alarmData;
	}
	
	private BaseStationPosition parseBaseStationPosition (ByteBuffer buffer, short length) {
		BaseStationPosition baseStationPosition = new BaseStationPosition();
		
		baseStationPosition.setMcc(Integer.parseInt(ByteUtilities.bcdToString(buffer, 3)));
		baseStationPosition.setMnc(Byte.parseByte(ByteUtilities.bcdToString(buffer, 1)));
		baseStationPosition.setLac(buffer.getShort());
		baseStationPosition.setCellid(buffer.getShort());
		
		return baseStationPosition;
	}
	
	private DtcData parseDtcdata(ByteBuffer buffer, short length) {
		DtcData dtcData = new DtcData();
		
		return dtcData;
	}

}
