package monitoring.terminal.munic.processing.utils;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import monitoring.terminal.munic.controller.domain.MunicData;
import monitoring.terminal.munic.controller.domain.MunicDataBehave;
import monitoring.terminal.munic.controller.domain.MunicDataMdi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ItemRawDataJson {
	private String event;
	private String asset;
	private String recorded_at;
	private JSONObject payload;
	private JSONArray loc;
	private JSONObject fields;
	private double lat;
	private double lon;
	private Boolean dioIgnition = null;

	public ItemRawDataJson(String itemRawData) {
		JSONTokener tokener = new JSONTokener(itemRawData);
		JSONObject jsonObject = (JSONObject) tokener.nextValue();
		setMessageJsonObject(jsonObject);
	}

	public ItemRawDataJson(JSONObject messsageJsonValue) {
		setMessageJsonObject(messsageJsonValue);
	}

	private void setMessageJsonObject(JSONObject messsageJsonValue) {
		JSONObject meta = messsageJsonValue.getJSONObject("meta");
		event = meta.getString("event");
		payload = messsageJsonValue.getJSONObject("payload");
		asset = payload.getString("asset");
		fields = payload.optJSONObject("fields");

		if (isTrack()) {
			recorded_at = payload.getString("recorded_at"); // 2014-03-26T11:42:01Z
			loc = payload.optJSONArray("loc");
			if (loc != null) {
				lon = loc.getDouble(0);
				lat = loc.getDouble(1);
			}
			if (fields != null) {
				dioIgnition = getBooleanField("DIO_IGNITION");
			}
		}
	}

	public boolean isTrack() {
		return event.equals("track");
	}

	public String getAsset() {
		return asset;
	}

	@SuppressWarnings("unused")
	private String getGPRMC() {
		// $GPRMC,180933.000,A,5544.0903,N,3736.7949,E,25.00,194.00,160414,0,N,A*15
		Date recordedAt = getRecordedAt();
		Integer gpsSpeed = getIntegerField("GPS_SPEED");
		Integer gpsDir = getIntegerField("GPS_DIR");

		if (recordedAt != null && loc != null && gpsSpeed != null && gpsDir != null) {
			SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss.SSS");
			SimpleDateFormat sdfDate = new SimpleDateFormat("ddMMyy");

			StringBuffer sb = new StringBuffer();
			sb.append("GPRMC,");
			sb.append(sdfTime.format(recordedAt));
			sb.append(",A,");

			double lat1 = convertDecDegToDecMin(lat);
			sb.append((int) lat1);
			sb.append('.');
			sb.append((int) ((lat1 - (int) lat1) * 10000));
			sb.append(',');
			sb.append(lat > 0 ? 'N' : 'S');
			sb.append(',');

			double lon1 = convertDecDegToDecMin(lon);
			sb.append((int) lon1);
			sb.append('.');
			sb.append((int) ((lon1 - (int) lon1) * 10000));
			sb.append(',');
			sb.append(lon > 0 ? 'E' : 'W');
			sb.append(',');

			double gpsSpeed1 = gpsSpeed / 1000;
			sb.append((int) gpsSpeed1);
			sb.append('.');
			sb.append((int) ((gpsSpeed1 - (int) gpsSpeed1) * 100));
			sb.append(',');

			double gpsDir1 = gpsDir / 100;
			sb.append((int) gpsDir1);
			sb.append('.');
			sb.append((int) ((gpsDir1 - (int) gpsDir1) * 100));
			sb.append(',');

			sb.append(sdfDate.format(recordedAt));
			sb.append(",0,N,A");

			int crc = gprmcCRC(sb.toString().getBytes(Charset.forName("ASCII")));
			sb.insert(0, '$');
			sb.append('*');
			sb.append(Integer.toHexString(crc));

			return sb.toString();
		}
		return null;
	}

	private int gprmcCRC(byte[] bytes) {
		int crc = 0x0;
		for (int i = 0; i < bytes.length; i++) {
			crc = crc ^ (bytes[i] < 0 ? 256 + bytes[i] : bytes[i]);
		}
		return crc;
	}

	private double convertDecDegToDecMin(double number) {
		int decimal = (int) number;
		double fractional = number - decimal;
		double result = (decimal * 100) + (fractional * 60);
		return result;
	}

	private Date getRecordedAt() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return dateFormatter.parse(recorded_at.replace('T', ' ').replace("Z", ""));
		} catch (ParseException e) {
		}
		return null;
	}

	public MunicData getMunicData() {
		if (isTrack()) {
			MunicData data = new MunicData();
			data.setEvent(event);
			data.setAsset(Long.parseLong(asset));
			data.setRecordedAt(getRecordedAt());

			if (loc != null) {
				data.setLon(lon);
				data.setLat(lat);
			}

			data.setGpsDir(getIntegerField("GPS_DIR"));
			data.setGpsSpeed(getIntegerField("GPS_SPEED"));
			data.setDioIgnition(getBooleanField("DIO_IGNITION"));
			data.setOdoFull(getIntegerField("ODO_FULL"));
			data.setGprmcValid(getStringField("GPRMC_VALID"));
			data.setDioAlarm(getBooleanField("DIO_ALARM"));
			data.setDriverId(getStringField("DRIVER_ID"));
			data.setDioInTor(getIntegerField("DIO_IN_TOR"));
			data.setBatt(getIntegerField("BATT"));
			data.setGprsHeader(getIntegerField("GPRS_HEADER"));
			data.setRssi(getIntegerField("RSSI"));
			data.setBattVolt(getIntegerField("BATT_VOLT"));
			data.setMvtState(getBooleanField("MVT_STATE"));

			if (containsFieldStartsWith("BEHAVE_")) {
				MunicDataBehave behave = new MunicDataBehave();
				data.setMunicDataBehave(behave);
				behave.setMunicData(data);

				behave.setBehaveUniqueId(getIntegerField("BEHAVE_UNIQUE_ID"));
				behave.setBehaveId(getIntegerField("BEHAVE_ID"));
				behave.setBehaveElapsed(getIntegerField("BEHAVE_ELAPSED"));
				behave.setBehaveAccXBegin(getIntegerField("BEHAVE_ACC_X_BEGIN"));
				behave.setBehaveAccXPeak(getIntegerField("BEHAVE_ACC_X_PEAK"));
				behave.setBehaveAccXEnd(getIntegerField("BEHAVE_ACC_X_END"));
				behave.setBehaveAccYBegin(getIntegerField("BEHAVE_ACC_Y_BEGIN"));
				behave.setBehaveAccYPeak(getIntegerField("BEHAVE_ACC_Y_PEAK"));
				behave.setBehaveAccYEnd(getIntegerField("BEHAVE_ACC_Y_END"));
				behave.setBehaveAccZBegin(getIntegerField("BEHAVE_ACC_Z_BEGIN"));
				behave.setBehaveAccZPeak(getIntegerField("BEHAVE_ACC_Z_PEAK"));
				behave.setBehaveAccZEnd(getIntegerField("BEHAVE_ACC_Z_END"));
				behave.setBehaveGpsSpeedBegin(getIntegerField("BEHAVE_GPS_SPEED_BEGIN"));
				behave.setBehaveGpsSpeedPeak(getIntegerField("BEHAVE_GPS_SPEED_PEAK"));
				behave.setBehaveGpsSpeedEnd(getIntegerField("BEHAVE_GPS_SPEED_END"));
				behave.setBehaveGpsHeadingBegin(getIntegerField("BEHAVE_GPS_HEADING_BEGIN"));
				behave.setBehaveGpsHeadingPeak(getIntegerField("BEHAVE_GPS_HEADING_PEAK"));
				behave.setBehaveGpsHeadingEnd(getIntegerField("BEHAVE_GPS_HEADING_END"));
				behave.setBehaveLong(getIntegerField("BEHAVE_LONG"));
				behave.setBehaveLat(getIntegerField("BEHAVE_LAT"));
				behave.setBehaveDayOfYear(getIntegerField("BEHAVE_DAY_OF_YEAR"));
				behave.setBehaveTimeOfDay(getIntegerField("BEHAVE_TIME_OF_DAY"));
			}

			if (containsFieldStartsWith("MDI_")) {
				MunicDataMdi mdi = new MunicDataMdi();
				data.setMunicDataMdi(mdi);
				mdi.setMunicData(data);

				mdi.setMdiExtBattLow(getBooleanField("MDI_EXT_BATT_LOW"));
				mdi.setMdiExtBattVoltage(getIntegerField("MDI_EXT_BATT_VOLTAGE"));
				mdi.setMdiAreaList(getStringField("MDI_AREA_LIST"));
				mdi.setMdiCrashDetected(getStringField("MDI_CRASH_DETECTED"));

				mdi.setMdiDtcList(getStringField("MDI_DTC_LIST"));
				mdi.setMdiDtcMil(getBooleanField("MDI_DTC_MIL"));
				mdi.setMdiDtcNumber(getIntegerField("MDI_DTC_NUMBER"));

				mdi.setMdiRpmMax(getIntegerField("MDI_RPM_MAX"));
				mdi.setMdiRpmMin(getIntegerField("MDI_RPM_MIN"));
				mdi.setMdiRpmAverage(getIntegerField("MDI_RPM_AVERAGE"));
				mdi.setMdiRpmOver(getBooleanField("MDI_RPM_OVER"));

				mdi.setMdiObdSpeed(getIntegerField("MDI_OBD_SPEED"));
				mdi.setMdiObdRpm(getIntegerField("MDI_OBD_RPM"));
				mdi.setMdiObdFuel(getIntegerField("MDI_OBD_FUEL"));
				mdi.setMdiObdVin(getStringField("MDI_OBD_VIN"));
				mdi.setMdiObdMileage(getIntegerField("MDI_OBD_MILEAGE"));

				mdi.setMdiJourneyTime(getIntegerField("MDI_JOURNEY_TIME"));
				mdi.setMdiIdleJourney(getIntegerField("MDI_IDLE_JOURNEY"));
				mdi.setMdiDrivingJorney(getIntegerField("MDI_DRIVING_JOURNEY"));
				mdi.setMdiTowAway(getBooleanField("MDI_TOW_AWAY"));
				mdi.setMdiMaxSpeedJourney(getIntegerField("MDI_MAX_SPEED_JOURNEY"));
				mdi.setMdiJourneyState(getBooleanField("MDI_JOURNEY_STATE"));
				mdi.setMdiOdoJourney(getIntegerField("MDI_ODO_JOURNEY"));
				mdi.setMdiOverSpeedCounter(getIntegerField("MDI_OVERSPEED_COUNTER"));
				mdi.setMdiOverSpeed(getBooleanField("MDI_OVERSPEED"));

				mdi.setMdiVehicleState(getStringField("MDI_VEHICLE_STATE"));

				mdi.setMdiRecordReason(getStringField("MDI_RECORD_REASON"));
				mdi.setMdiBootReason(getStringField("MDI_BOOT_REASON"));
				mdi.setMdiShutdownReason(getStringField("MDI_SHUTDOWN_REASON"));

				mdi.setMdiPanicState(getBooleanField("MDI_PANIC_STATE"));
				mdi.setMdiPanicMessage(getStringField("MDI_PANIC_MESSAGE"));
			}

			return data;
		} else {
			return null;
		}
	}

	public String getString4Wialon() {
		if (isTrack()) {
			StringBuilder sb = new StringBuilder();
			insertPair("asset", asset, sb);
			insertPair("recorded_at", recorded_at.replace("Z", ""), sb);
			if (loc != null) {
				insertPair("lat", String.valueOf(lat), sb);
				insertPair("lon", String.valueOf(lon), sb);
			}

			insertIntegerFieldIfExists("GPS_DIR", sb);
			insertIntegerFieldIfExists("GPS_SPEED", sb);
			insertBooleanPair("DIO_IGNITION", dioIgnition, sb);
			insertIntegerFieldIfExists("ODO_FULL", sb);
			insertIntegerFieldIfExists("BEHAVE_ID", sb);
			insertIntegerFieldIfExists("BEHAVE_ELAPSED", sb);
			insertIntegerFieldIfExists("BEHAVE_LONG", sb);
			insertIntegerFieldIfExists("BEHAVE_LAT", sb);
			insertIntegerFieldIfExists("BEHAVE_DAY_OF_YEAR", sb);
			insertIntegerFieldIfExists("BEHAVE_TIME_OF_DAY", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_X_BEGIN", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_X_PEAK", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_X_END", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_Y_BEGIN", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_Y_PEAK", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_Y_END", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_Z_BEGIN", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_Z_PEAK", sb);
			insertIntegerFieldIfExists("BEHAVE_ACC_Z_END", sb);
			insertIntegerFieldIfExists("BEHAVE_GPS_SPEED_BEGIN", sb);
			insertIntegerFieldIfExists("BEHAVE_GPS_SPEED_PEAK", sb);
			insertIntegerFieldIfExists("BEHAVE_GPS_SPEED_END", sb);
			insertIntegerFieldIfExists("BEHAVE_GPS_HEADING_BEGIN", sb);
			insertIntegerFieldIfExists("BEHAVE_GPS_HEADING_PEAK", sb);
			insertIntegerFieldIfExists("BEHAVE_GPS_HEADING_END", sb);

			return sb.toString();
		} else {
			return null;
		}
	}

	private boolean containsFieldStartsWith(String startsWith) {
		boolean contains = false;
		for (Object key : fields.keySet()) {
			String keyString = (String) key;
			if (keyString.startsWith(startsWith)) {
				contains = true;
				break;
			}
		}
		return contains;
	}

	private void insertIntegerFieldIfExists(String key, StringBuilder sb) {
		JSONObject jsonObject = fields.optJSONObject(key);
		if (jsonObject != null) {
			String value = Integer.toString(Base64Utils.decodeBase64Integer(jsonObject.getString("b64_value")));
			insertPair(key, value, sb);
		}
	}

	@SuppressWarnings("unused")
	private void insertBooleanFieldIfExists(String key, StringBuilder sb) {
		JSONObject jsonObject = fields.optJSONObject(key);
		if (jsonObject != null) {
			String value = Base64Utils.decodeBase64Boolean(jsonObject.getString("b64_value")) ? "1" : "0";
			insertPair(key, value, sb);
		}
	}

	private void insertPair(String key, String value, StringBuilder sb) {
		sb.append("|");
		sb.append(key);
		sb.append("=");
		sb.append(value);
	}
	
	private void insertBooleanPair(String key, Boolean value, StringBuilder sb) {
		if (value != null) {
			insertPair(key, value ? "1" : "0", sb);
		}
	}

	private Integer getIntegerField(String key) {
		JSONObject jsonObject = fields.optJSONObject(key);
		if (jsonObject != null) {
			return Base64Utils.decodeBase64Integer(jsonObject.getString("b64_value"));
		} else {
			return null;
		}
	}

	private Boolean getBooleanField(String key) {
		JSONObject jsonObject = fields.optJSONObject(key);
		if (jsonObject != null) {
			return Base64Utils.decodeBase64Boolean(jsonObject.getString("b64_value"));
		} else {
			return null;
		}
	}

	private String getStringField(String key) {
		JSONObject jsonObject = fields.optJSONObject(key);
		if (jsonObject != null) {
			return Base64Utils.decodeBase64String(jsonObject.getString("b64_value"));
		} else {
			return null;
		}
	}

	public Boolean getDioIgnition() {
		return dioIgnition;
	}

	public void setDioIgnition(Boolean previousDioIgnition) {
		dioIgnition = previousDioIgnition;
	}

}
