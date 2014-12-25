package monitoring.tekobd2.messages;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

import monitoring.domain.IHasPosition;
import monitoring.domain.Position;
import monitoring.tekobd2.ByteUtilities;
import monitoring.tekobd2.messages.trip.AlarmData;
import monitoring.tekobd2.messages.trip.BaseStationPosition;
import monitoring.tekobd2.messages.trip.DtcData;
import monitoring.tekobd2.messages.trip.ObdData;
import monitoring.tekobd2.messages.trip.SatellitePosition;

public class TripDataMessage extends GeneralResponseMessage implements IHasPosition {

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

	private ObdData obdData;
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
				Integer.parseInt(timeString.substring(0, 2)),
				Integer.parseInt(timeString.substring(2, 4)),
				Integer.parseInt(timeString.substring(4, 6)),
				Integer.parseInt(timeString.substring(6, 8)),
				Integer.parseInt(timeString.substring(8, 10)),
				Integer.parseInt(timeString.substring(10))
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
			byte tripDataMessageId = bb.get();
			short length = bb.getShort();
			
			switch (tripDataMessageId) {
			case (byte) 0xA0:
				obdData = new ObdData();
				obdData.parse(bb, length);
				break;

			case (byte) 0xA1:
				satellitePosition = new SatellitePosition();
				satellitePosition.parse(bb, length);
				break;

			case (byte) 0xA2:
				baseStationPosition = new BaseStationPosition();
				baseStationPosition.parse(bb, length);
				break;

			case (byte) 0xA3:
				dtcData = new DtcData();
				dtcData.parse(bb, length);
				break;

			case (byte) 0xA4:
				alarmData = new AlarmData();
				alarmData.parse(bb, length);
				break;

			default:
				break;
			}
		}
	}
	
	@Override
	public Position getPosition() {
		if (satellitePosition != null) {
			Position position = satellitePosition.getPosition();
			position.setDate(uploadingTime);
			position.setTerminalId(getTerminalId());
			position.setSatellites((int) satelites);
			return position;
		} else {
			return null;
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

	public ObdData getObdData() {
		return obdData;
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
