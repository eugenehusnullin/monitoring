package monitoring.terminal.tek.messages.domain;

import java.util.Date;

import monitoring.terminal.tek.messages.domain.trip.AlarmData;
import monitoring.terminal.tek.messages.domain.trip.BaseStationPosition;
import monitoring.terminal.tek.messages.domain.trip.DtcData;
import monitoring.terminal.tek.messages.domain.trip.ObdData;
import monitoring.terminal.tek.messages.domain.trip.SatellitePosition;

public class TripDataMessage extends TekMessage {

	public enum PositioningMode {
		GPS((byte) 0b000), BeidouSatelite((byte) 0b001), GPSBeidou((byte) 0b010), Glonass((byte) 0b011), BaseStation(
				(byte) 0b100), BaseStationGPS((byte) 0b101), BaseStationBeidou((byte) 0b110), NoPositioning(
				(byte) 0b111);

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

	public void setUploadingTime(Date uploadingTime) {
		this.uploadingTime = uploadingTime;
	}

	public void setPositioningMode(PositioningMode positioningMode) {
		this.positioningMode = positioningMode;
	}

	public void setSatelitePositioningStatus(byte satelitePositioningStatus) {
		this.satelitePositioningStatus = satelitePositioningStatus;
	}

	public void setBaseStationPositioningStatus(boolean baseStationPositioningStatus) {
		this.baseStationPositioningStatus = baseStationPositioningStatus;
	}

	public void setNorthern(boolean isNorthern) {
		this.isNorthern = isNorthern;
	}

	public void setEastern(boolean isEastern) {
		this.isEastern = isEastern;
	}

	public void setSatelites(byte satelites) {
		this.satelites = satelites;
	}

	public void setGsmSignalValue(byte gsmSignalValue) {
		this.gsmSignalValue = gsmSignalValue;
	}

	public void setObdEnabled(boolean obdEnabled) {
		this.obdEnabled = obdEnabled;
	}

	public void setObdData(ObdData obdData) {
		this.obdData = obdData;
	}

	public void setSatellitePosition(SatellitePosition satellitePosition) {
		this.satellitePosition = satellitePosition;
	}

	public void setBaseStationPosition(BaseStationPosition baseStationPosition) {
		this.baseStationPosition = baseStationPosition;
	}

	public void setDtcData(DtcData dtcData) {
		this.dtcData = dtcData;
	}

	public void setAlarmData(AlarmData alarmData) {
		this.alarmData = alarmData;
	}

}
