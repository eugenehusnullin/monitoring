package monitoring.terminal.munic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import monitoring.domain.Message;
import monitoring.utils.Base64Utils;

public class MunicMessage implements Message {
	private JSONObject jsonFields;

	private String event;
	private long terminalId;
	private Date recordedAt;
	private double lat;
	private double lon;
	private Boolean dioIgnition = null;

	public void setJsonMessage(JSONObject jsonMessage) throws JSONException, ParseException {
		JSONObject meta = jsonMessage.getJSONObject("meta");
		event = meta.getString("event");
		JSONObject payload = jsonMessage.getJSONObject("payload");
		terminalId = Long.parseLong(payload.getString("asset"));
		jsonFields = payload.optJSONObject("fields");

		if (isTrack()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			recordedAt = dateFormat.parse(payload.getString("recorded_at"));
			JSONArray loc = payload.optJSONArray("loc");
			if (loc != null) {
				lon = loc.optDouble(0);
				lat = loc.optDouble(1);
			}
			if (jsonFields != null) {
				dioIgnition = getBooleanField("DIO_IGNITION");
			}
		}
	}

	private boolean isTrack() {
		return event.equals("track");
	}

	@Override
	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public Date getRecordedAt() {
		return recordedAt;
	}

	public void setRecordedAt(Date recordedAt) {
		this.recordedAt = recordedAt;
	}

	private Boolean getBooleanField(String key) {
		JSONObject jsonObject = jsonFields != null ? jsonFields.optJSONObject(key) : null;
		if (jsonObject != null) {
			return Base64Utils.decodeBase64Boolean(jsonObject.getString("b64_value"));
		} else {
			return null;
		}
	}

}
