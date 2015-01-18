package monitoring.terminal.munic;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MunicMessageFactory {
	private static final Logger logger = LoggerFactory.getLogger(MunicMessageFactory.class);

	private Map<Long, Boolean> ignitionStateMap = new HashMap<Long, Boolean>();

	public MunicMessage createMessage(JSONObject jsonObject) {
		MunicMessage municMessage = new MunicMessage();
		try {
			municMessage.setJsonMessage(jsonObject);
			ignitionProof(municMessage);

		} catch (JSONException | ParseException e) {
			logger.error(e.toString());
			return null;
		}
		return municMessage;
	}

	private void ignitionProof(MunicMessage municMessage) {
		synchronized (ignitionStateMap) {
			if (municMessage.getDioIgnition() != null) {
				ignitionStateMap.put(municMessage.getTerminalId(), municMessage.getDioIgnition());
			} else {
				Boolean ignitionState = ignitionStateMap.get(municMessage.getTerminalId());
				if (ignitionState != null) {
					municMessage.setDioIgnition(ignitionState);
				}
			}
			ignitionStateMap.notifyAll();
		}
	}
}
