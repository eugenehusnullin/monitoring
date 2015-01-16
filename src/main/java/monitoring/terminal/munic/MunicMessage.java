package monitoring.terminal.munic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import monitoring.domain.Message;
import monitoring.utils.Base64Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MunicMessage implements Message {
	private long terminalId;
	private Date recordedAt;
	private JSONObject jsonPayload;
	private JSONObject jsonFields;
	private String event;
	private double latitude = Double.NaN;
	private double longitude = Double.NaN;

	public void setJsonMessage(JSONObject jsonMessage) throws JSONException, ParseException {
		JSONObject meta = jsonMessage.getJSONObject("meta");
		event = meta.getString("event");

		jsonPayload = jsonMessage.getJSONObject("payload");
		terminalId = Long.parseLong(jsonPayload.getString("asset"));
		jsonFields = jsonPayload.optJSONObject("fields");

		String timeFieldNameString;
		if (isTrackEvent() || isMessageEvent()) {
			timeFieldNameString = "recorded_at";
		} else {
			timeFieldNameString = "time";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		recordedAt = dateFormat.parse(jsonPayload.getString(timeFieldNameString));

		if (isTrackEvent()) {
			JSONArray loc = jsonPayload.optJSONArray("loc");
			if (loc != null) {
				longitude = loc.optDouble(0);
				latitude = loc.optDouble(1);
			}
		}
	}

	public boolean hasGeo() {
		return latitude != Double.NaN && longitude != Double.NaN;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public boolean isTrackEvent() {
		return event.equals("track");
	}

	public boolean isMessageEvent() {
		return event.equals("message");
	}

	public boolean isPresenceEvent() {
		return event.equals("presence");
	}

	public boolean hasBehave() {
		return containsFieldStartsWith("BEHAVE_");
	}

	public Integer getAccX() {
		return Math.max(Math.max(getIntegerField("BEHAVE_ACC_X_BEGIN"), getIntegerField("BEHAVE_ACC_X_PEAK")),
				getIntegerField("BEHAVE_ACC_X_END"));
	}
	
	public Integer getAccY() {
		return Math.max(Math.max(getIntegerField("BEHAVE_ACC_Y_BEGIN"), getIntegerField("BEHAVE_ACC_Y_PEAK")),
				getIntegerField("BEHAVE_ACC_Y_END"));
	}
	
	public Integer getAccZ() {
		return Math.max(Math.max(getIntegerField("BEHAVE_ACC_Z_BEGIN"), getIntegerField("BEHAVE_ACC_Z_PEAK")),
				getIntegerField("BEHAVE_ACC_Z_END"));
	}

	@Override
	public long getTerminalId() {
		return terminalId;
	}

	public Date getRecordedAt() {
		return recordedAt;
	}

	public Boolean getDioIgnition() {
		return getBooleanField("DIO_IGNITION");
	}

	public Double getDirection() {
		Integer dirInteger = getIntegerField("GPS_DIR");
		if (dirInteger != null) {
			return dirInteger.doubleValue() / 100;
		} else {
			return null;
		}
	}

	public Double getSpeed() {
		Integer speedInteger = getIntegerField("GPS_SPEED");
		if (speedInteger != null) {
			return speedInteger.doubleValue() / 1000 * 1.852;
		} else {
			return null;
		}
	}

	/*
	 * Other fields: getIntegerField("ODO_FULL") getStringField("GPRMC_VALID")
	 * getBooleanField("DIO_ALARM") getStringField("DRIVER_ID")
	 * getIntegerField("DIO_IN_TOR") getIntegerField("BATT")
	 * getIntegerField("GPRS_HEADER") getIntegerField("RSSI")
	 * getIntegerField("BATT_VOLT") getBooleanField("MVT_STATE")
	 * 
	 * 
	 * behave.setBehaveUniqueId(getIntegerField("BEHAVE_UNIQUE_ID"));
	 * behave.setBehaveId(getIntegerField("BEHAVE_ID"));
	 * behave.setBehaveElapsed(getIntegerField("BEHAVE_ELAPSED"));
	 * behave.setBehaveAccXBegin(getIntegerField("BEHAVE_ACC_X_BEGIN"));
	 * behave.setBehaveAccXPeak(getIntegerField("BEHAVE_ACC_X_PEAK"));
	 * behave.setBehaveAccXEnd(getIntegerField("BEHAVE_ACC_X_END"));
	 * behave.setBehaveAccYBegin(getIntegerField("BEHAVE_ACC_Y_BEGIN"));
	 * behave.setBehaveAccYPeak(getIntegerField("BEHAVE_ACC_Y_PEAK"));
	 * behave.setBehaveAccYEnd(getIntegerField("BEHAVE_ACC_Y_END"));
	 * behave.setBehaveAccZBegin(getIntegerField("BEHAVE_ACC_Z_BEGIN"));
	 * behave.setBehaveAccZPeak(getIntegerField("BEHAVE_ACC_Z_PEAK"));
	 * behave.setBehaveAccZEnd(getIntegerField("BEHAVE_ACC_Z_END"));
	 * behave.setBehaveGpsSpeedBegin(getIntegerField("BEHAVE_GPS_SPEED_BEGIN"));
	 * behave.setBehaveGpsSpeedPeak(getIntegerField("BEHAVE_GPS_SPEED_PEAK"));
	 * behave.setBehaveGpsSpeedEnd(getIntegerField("BEHAVE_GPS_SPEED_END"));
	 * behave
	 * .setBehaveGpsHeadingBegin(getIntegerField("BEHAVE_GPS_HEADING_BEGIN"));
	 * behave
	 * .setBehaveGpsHeadingPeak(getIntegerField("BEHAVE_GPS_HEADING_PEAK"));
	 * behave.setBehaveGpsHeadingEnd(getIntegerField("BEHAVE_GPS_HEADING_END"));
	 * behave.setBehaveLong(getIntegerField("BEHAVE_LONG"));
	 * behave.setBehaveLat(getIntegerField("BEHAVE_LAT"));
	 * behave.setBehaveDayOfYear(getIntegerField("BEHAVE_DAY_OF_YEAR"));
	 * behave.setBehaveTimeOfDay(getIntegerField("BEHAVE_TIME_OF_DAY"));
	 * 
	 * 
	 * 
	 * mdi.setMdiExtBattLow(getBooleanField("MDI_EXT_BATT_LOW"));
	 * mdi.setMdiExtBattVoltage(getIntegerField("MDI_EXT_BATT_VOLTAGE"));
	 * mdi.setMdiAreaList(getStringField("MDI_AREA_LIST"));
	 * mdi.setMdiCrashDetected(getStringField("MDI_CRASH_DETECTED"));
	 * mdi.setMdiDtcList(getStringField("MDI_DTC_LIST"));
	 * mdi.setMdiDtcMil(getBooleanField("MDI_DTC_MIL"));
	 * mdi.setMdiDtcNumber(getIntegerField("MDI_DTC_NUMBER"));
	 * mdi.setMdiRpmMax(getIntegerField("MDI_RPM_MAX"));
	 * mdi.setMdiRpmMin(getIntegerField("MDI_RPM_MIN"));
	 * mdi.setMdiRpmAverage(getIntegerField("MDI_RPM_AVERAGE"));
	 * mdi.setMdiRpmOver(getBooleanField("MDI_RPM_OVER"));
	 * mdi.setMdiObdSpeed(getIntegerField("MDI_OBD_SPEED"));
	 * mdi.setMdiObdRpm(getIntegerField("MDI_OBD_RPM"));
	 * mdi.setMdiObdFuel(getIntegerField("MDI_OBD_FUEL"));
	 * mdi.setMdiObdVin(getStringField("MDI_OBD_VIN"));
	 * mdi.setMdiObdMileage(getIntegerField("MDI_OBD_MILEAGE"));
	 * mdi.setMdiJourneyTime(getIntegerField("MDI_JOURNEY_TIME"));
	 * mdi.setMdiIdleJourney(getIntegerField("MDI_IDLE_JOURNEY"));
	 * mdi.setMdiDrivingJorney(getIntegerField("MDI_DRIVING_JOURNEY"));
	 * mdi.setMdiTowAway(getBooleanField("MDI_TOW_AWAY"));
	 * mdi.setMdiMaxSpeedJourney(getIntegerField("MDI_MAX_SPEED_JOURNEY"));
	 * mdi.setMdiJourneyState(getBooleanField("MDI_JOURNEY_STATE"));
	 * mdi.setMdiOdoJourney(getIntegerField("MDI_ODO_JOURNEY"));
	 * mdi.setMdiOverSpeedCounter(getIntegerField("MDI_OVERSPEED_COUNTER"));
	 * mdi.setMdiOverSpeed(getBooleanField("MDI_OVERSPEED"));
	 * mdi.setMdiVehicleState(getStringField("MDI_VEHICLE_STATE"));
	 * mdi.setMdiRecordReason(getStringField("MDI_RECORD_REASON"));
	 * mdi.setMdiBootReason(getStringField("MDI_BOOT_REASON"));
	 * mdi.setMdiShutdownReason(getStringField("MDI_SHUTDOWN_REASON"));
	 * mdi.setMdiPanicState(getBooleanField("MDI_PANIC_STATE"));
	 * mdi.setMdiPanicMessage(getStringField("MDI_PANIC_MESSAGE"));
	 */

	private Boolean getBooleanField(String key) {
		JSONObject jsonObject = jsonFields.optJSONObject(key);
		if (jsonObject != null) {
			return Base64Utils.decodeBase64Boolean(jsonObject.getString("b64_value"));
		} else {
			return null;
		}
	}

	private Integer getIntegerField(String key) {
		JSONObject jsonObject = jsonFields.optJSONObject(key);
		if (jsonObject != null) {
			return Base64Utils.decodeBase64Integer(jsonObject.getString("b64_value"));
		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	private String getStringField(String key) {
		JSONObject jsonObject = jsonFields.optJSONObject(key);
		if (jsonObject != null) {
			return Base64Utils.decodeBase64String(jsonObject.getString("b64_value"));
		} else {
			return null;
		}
	}

	private boolean containsFieldStartsWith(String startsWith) {
		boolean contains = false;
		for (Object key : jsonFields.keySet()) {
			String keyString = (String) key;
			if (keyString.startsWith(startsWith)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
}
