package ru.gm.munic.tekobd2.messages;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

import ru.gm.munic.tekobd2.ByteUtilities;
import ru.gm.munic.tekobd2.messages.sub.AlarmData;
import ru.gm.munic.tekobd2.messages.sub.BaseStationPosition;
import ru.gm.munic.tekobd2.messages.sub.DtcData;
import ru.gm.munic.tekobd2.messages.sub.ObdData;
import ru.gm.munic.tekobd2.messages.sub.SatellitePosition;

public class TripDataMessage extends GeneralResponseMessage {

	private enum PositioningMode {
		GPS((byte) 0b000),
		BeidouSatelite((byte) 0b001),
		GPSBeidou((byte) 0b010),
		Glonass((byte) 0b011),
		BaseStation((byte) 0b100),
		BaseStationGPS((byte) 0b101),
		BaseStationBeidou((byte) 0b110),
		NoPositioning((byte) 0b111);
		
		private final byte mode;

		private PositioningMode(byte value) {
			this.mode = value;
		}

		public byte getMode() {
			return mode;
		}
		
		public static PositioningMode valueOfMode(byte mode) {
			for (PositioningMode pm : PositioningMode.values()) {
				if (pm.getMode() == mode) {
					return pm;
				}
			}
			return null;
		}
	}

	private Date uploadingTime;
	private PositioningMode positioningMode;
	private byte satelitePositioningStatus;
	private boolean baseStationPositioningStatus;
	private boolean isNorthern;
	private boolean isEastern;
	private byte satelites;
	private byte gsmSignalValue;
	private boolean obdEnabled;

	private ObdData obdDataSubmessage;
	private SatellitePosition satellitePosition;
	private BaseStationPosition baseStationPosition;
	private DtcData dtcData;
	private AlarmData alarmData;

	@Override
	public void parseMessageBody(ByteBuffer bb) {

		// uploading time
		String timeString = ByteUtilities.bcdToString(bb, 6);
		Calendar calendar = Calendar.getInstance();
		calendar.set(
				Integer.parseInt(timeString.substring(0, 1)),
				Integer.parseInt(timeString.substring(2, 3)),
				Integer.parseInt(timeString.substring(4, 5)),
				Integer.parseInt(timeString.substring(7, 8)),
				Integer.parseInt(timeString.substring(9, 10)),
				Integer.parseInt(timeString.substring(11, 12))
			);
		calendar.add(Calendar.HOUR_OF_DAY, -8);
		uploadingTime = calendar.getTime();

		// State word
		byte[] dword = new byte[4];
		bb.get(dword);
		byte pmbyte = (byte) (dword[0] & 0b111);

		positioningMode = PositioningMode.valueOfMode(pmbyte);
		satelitePositioningStatus = (byte) ((dword[0] >> 3) & 0b11);
		baseStationPositioningStatus = ((dword[0] >> 5) & 0b1) == 1;
		isNorthern = ((dword[0] >> 6) & 0b1) == 0;
		isEastern = ((dword[0] >> 7) & 0b1) == 0;

		satelites = (byte) (dword[1] & 0b1111);
		gsmSignalValue = (byte) ((dword[1] >> 4) & 0b1111);

		obdEnabled = (dword[2] & 0b1) == 1;

		while (bb.remaining() > 1) {
			short tripDataMessageId = bb.getShort();
			short length = bb.getShort();
			
			switch (tripDataMessageId) {
			case 0xA0:
				obdDataSubmessage = new ObdData();
				obdDataSubmessage.parse(bb, length);
				break;

			case 0xA1:
				satellitePosition = new SatellitePosition();
				satellitePosition.parse(bb, length);
				break;

			case 0xA2:
				baseStationPosition = new BaseStationPosition();
				baseStationPosition.parse(bb, length);
				break;

			case 0xA3:
				dtcData = new DtcData();
				dtcData.parse(bb, length);
				break;

			case 0xA4:
				alarmData = new AlarmData();
				alarmData.parse(bb, length);
				break;

			default:
				break;
			}
		}
	}
	
	public Date getUploadingTime() {
		return uploadingTime;
	}

	public PositioningMode getPositioningMode() {
		return positioningMode;
	}

	public byte getSatelitePositioningStatus() {
		return satelitePositioningStatus;
	}

	public boolean isBaseStationPositioningStatus() {
		return baseStationPositioningStatus;
	}

	public boolean isNorthern() {
		return isNorthern;
	}

	public boolean isEastern() {
		return isEastern;
	}

	public byte getSatelites() {
		return satelites;
	}

	public byte getGsmSignalValue() {
		return gsmSignalValue;
	}

	public boolean isObdEnabled() {
		return obdEnabled;
	}

	public ObdData getObdDataSubmessage() {
		return obdDataSubmessage;
	}

	public SatellitePosition getSatellitePosition() {
		return satellitePosition;
	}

	public BaseStationPosition getBaseStationPosition() {
		return baseStationPosition;
	}

	public DtcData getDtcData() {
		return dtcData;
	}

	public AlarmData getAlarmData() {
		return alarmData;
	}
}
