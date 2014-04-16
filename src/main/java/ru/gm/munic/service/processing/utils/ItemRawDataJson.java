package ru.gm.munic.service.processing.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import ru.gm.munic.domain.MunicData;
import ru.gm.munic.domain.MunicDataBehave;
import ru.gm.munic.domain.MunicDataMdi;

public class ItemRawDataJson {
	private String event;
	private String asset;
	private String recorded_at;
	private JSONObject payload;
	private JSONArray loc;
	private JSONObject fields;
	private double lat;
	private double lon;

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
				lat = loc.getDouble(0);
				lon = loc.getDouble(1);
			}
		}
	}

	public boolean isTrack() {
		return event.equals("track");
	}

	public String getAsset() {
		return asset;
	}

	public MunicData getMunicData() {
		if (isTrack()) {
			MunicData data = new MunicData();
			data.setEvent(event);
			data.setAsset(Long.parseLong(asset));

			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date recorderAt = dateFormatter.parse(recorded_at.replace('T', ' ').replace("Z", ""));
				data.setRecordedAt(recorderAt);
			} catch (ParseException e) {
			}

			if (loc != null) {
				data.setLon(lon);
				data.setLat(lat);
			}

			data.setGpsDir(getIntegerField("GPS_DIR"));
			data.setGpsSpeed(getIntegerField("GPS_SPEED"));
			data.setDioIgnition(getBooleanField("DIO_IGNITION"));
			data.setOdoFull(getIntegerField("ODO_FULL"));

			if (containsFieldStartsWith("BEHAVE_")) {
				MunicDataBehave dataBehave = new MunicDataBehave();
				data.setMunicDataBehave(dataBehave);
				dataBehave.setMunicData(data);

				dataBehave.setBehaveUniqueId(getIntegerField("BEHAVE_UNIQUE_ID"));
				dataBehave.setBehaveId(getIntegerField("BEHAVE_ID"));
				dataBehave.setBehaveElapsed(getIntegerField("BEHAVE_ELAPSED"));
				dataBehave.setBehaveAccXBegin(getIntegerField("BEHAVE_ACC_X_BEGIN"));
				dataBehave.setBehaveAccXPeak(getIntegerField("BEHAVE_ACC_X_PEAK"));
				dataBehave.setBehaveAccXEnd(getIntegerField("BEHAVE_ACC_X_END"));
				dataBehave.setBehaveAccYBegin(getIntegerField("BEHAVE_ACC_Y_BEGIN"));
				dataBehave.setBehaveAccYPeak(getIntegerField("BEHAVE_ACC_Y_PEAK"));
				dataBehave.setBehaveAccYEnd(getIntegerField("BEHAVE_ACC_Y_END"));
				dataBehave.setBehaveAccZBegin(getIntegerField("BEHAVE_ACC_Z_BEGIN"));
				dataBehave.setBehaveAccZPeak(getIntegerField("BEHAVE_ACC_Z_PEAK"));
				dataBehave.setBehaveAccZEnd(getIntegerField("BEHAVE_ACC_Z_END"));
				dataBehave.setBehaveGpsSpeedBegin(getIntegerField("BEHAVE_GPS_SPEED_BEGIN"));
				dataBehave.setBehaveGpsSpeedPeak(getIntegerField("BEHAVE_GPS_SPEED_PEAK"));
				dataBehave.setBehaveGpsSpeedEnd(getIntegerField("BEHAVE_GPS_SPEED_END"));
				dataBehave.setBehaveGpsHeadingBegin(getIntegerField("BEHAVE_GPS_HEADING_BEGIN"));
				dataBehave.setBehaveGpsHeadingPeak(getIntegerField("BEHAVE_GPS_HEADING_PEAK"));
				dataBehave.setBehaveGpsHeadingEnd(getIntegerField("BEHAVE_GPS_HEADING_END"));
			}

			if (containsFieldStartsWith("MDI_")) {
				MunicDataMdi dataMdi = new MunicDataMdi();
				data.setMunicDataMdi(dataMdi);
				dataMdi.setMunicData(data);

				dataMdi.setMdiExtBattLow(getBooleanField("MDI_EXT_BATT_LOW"));
				dataMdi.setMdiExtBattVoltage(getIntegerField("MDI_EXT_BATT_VOLTAGE"));

				dataMdi.setMdiDtcList(getStringField("MDI_DTC_LIST"));
				dataMdi.setMdiDtcMil(getBooleanField("MDI_DTC_MIL"));
				dataMdi.setMdiDtcNumber(getIntegerField("MDI_DTC_NUMBER"));

				dataMdi.setMdiRpmMax(getIntegerField("MDI_RPM_MAX"));
				dataMdi.setMdiRpmMin(getIntegerField("MDI_RPM_MIN"));
				dataMdi.setMdiRpmAverage(getIntegerField("MDI_RPM_AVERAGE"));
				dataMdi.setMdiRpmOver(getBooleanField("MDI_RPM_OVER"));

				dataMdi.setMdiObdSpeed(getIntegerField("MDI_OBD_SPEED"));
				dataMdi.setMdiObdRpm(getIntegerField("MDI_OBD_RPM"));
				dataMdi.setMdiObdFuel(getIntegerField("MDI_OBD_FUEL"));
				dataMdi.setMdiObdVin(getStringField("MDI_OBD_VIN"));
				dataMdi.setMdiObdMileage(getIntegerField("MDI_OBD_MILEAGE"));

				dataMdi.setMdiOdoJourney(getIntegerField("MDI_ODO_JOURNEY"));
				dataMdi.setMdiOverSpeedCounter(getIntegerField("MDI_OVERSPEED_COUNTER"));
				dataMdi.setMdiOverSpeed(getBooleanField("MDI_OVERSPEED"));
				dataMdi.setMdiRecordReason(getStringField("MDI_RECORD_REASON"));
				dataMdi.setMdiVehicleState(getStringField("MDI_VEHICLE_STATE"));
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
			insertBooleanFieldIfExists("DIO_IGNITION", sb);
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

}
