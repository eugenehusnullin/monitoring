package ru.gm.munic.service.processing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.postgresql.util.Base64;

import ru.gm.munic.domain.Message;

public class MessageJson {
	private String event;
	private String asset;
	private String recorded_at;
	private JSONObject payload;
	private JSONArray loc;
	private JSONObject fields;

	public MessageJson(Message message) {
		JSONTokener jsonTokener = new JSONTokener(message.getValue());
		setMessageJsonObject((JSONObject) jsonTokener.nextValue());
	}

	public MessageJson(JSONObject messsageJsonValue) {
		setMessageJsonObject(messsageJsonValue);
	}

	private void setMessageJsonObject(JSONObject messsageJsonValue) {
		JSONObject meta = messsageJsonValue.getJSONObject("meta");
		event = meta.getString("event");

		payload = messsageJsonValue.getJSONObject("payload");
		asset = payload.getString("asset");
		recorded_at = payload.getString("recorded_at").replace("Z", ""); // 2014-03-26T11:42:01Z
		loc = payload.optJSONArray("loc");
		fields = payload.optJSONObject("fields");
	}

	public String getEvent() {
		return event;
	}

	private int decodeBase64Integer(String value) {
		int result = 0;
		if (value != null && value != "") {
			byte[] values = Base64.decode(value);
			if (values.length > 0) {
				for (int i = 0; i < values.length; i++) {
					result <<= 8;
					result += values[i] < 0 ? 256 + values[i] : values[i];
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
	private String decodeBase64String(String value) {
		String result = "";
		if (value != null && value != "") {
			byte[] values = Base64.decode(value);
			for (int i = 0; i < values.length; i++) {
				result += (char) values[i];
			}
		}
		return result;
	}

	private boolean decodeBase64Boolean(String value) {
		boolean result = false;
		if (value != null && value != "") {
			byte[] values = Base64.decode(value);
			result = values[0] != 0;
		}
		return result;
	}

	private void insertIntegerFieldIfExists(String key, StringBuilder sb) {
		JSONObject jsonObject = fields.optJSONObject(key);
		if (jsonObject != null) {
			String value = Integer.toString(decodeBase64Integer(jsonObject.getString("b64_value")));
			insertPair(key, value, sb);
		}
	}
	
	private void insertBooleanFieldIfExists(String key, StringBuilder sb) {
		JSONObject jsonObject = fields.optJSONObject(key);
		if (jsonObject != null) {
			String value = decodeBase64Boolean(jsonObject.getString("b64_value")) ? "1" : "0";
			insertPair(key, value, sb);
		}
	}

	private void insertPair(String key, String value, StringBuilder sb) {
		sb.append("|");
		sb.append(key);
		sb.append("=");
		sb.append(value);
	}

	public String getString4Tcp() {
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
	}

}
