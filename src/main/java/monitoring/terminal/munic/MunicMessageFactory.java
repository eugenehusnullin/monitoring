package monitoring.terminal.munic;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MunicMessageFactory {
	private static final Logger logger = LoggerFactory.getLogger(MunicMessageFactory.class);

	public MunicMessage createMessage(JSONObject jsonObject) {
		MunicMessage municMessage = new MunicMessage();
		try {
			municMessage.setJsonMessage(jsonObject);
		} catch (JSONException | ParseException e) {
			logger.error(e.toString());
			return null;
		}
		return municMessage;
	}

}
