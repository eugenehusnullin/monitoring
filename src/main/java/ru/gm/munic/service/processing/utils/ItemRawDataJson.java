package ru.gm.munic.service.processing.utils;

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
	}

	public boolean isTrack() {
		return event.equals("track");
	}
	
	public String getAsset() {
		return asset;
	}

	public String getString4Wialon() {
		if (isTrack()) {
			recorded_at = payload.getString("recorded_at").replace("Z", ""); // 2014-03-26T11:42:01Z
			loc = payload.optJSONArray("loc");
			fields = payload.optJSONObject("fields");

			StringBuilder sb = new StringBuilder();
			sb.append("|asset=");
			sb.append(asset);
			sb.append("|recorded_at=");
			sb.append(recorded_at);
			if (loc != null) {
				double lat = loc.getDouble(0);
				double lon = loc.getDouble(1);

				sb.append("|lat=");
				sb.append(lat);
				sb.append("|lon=");
				sb.append(lon);
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

}
