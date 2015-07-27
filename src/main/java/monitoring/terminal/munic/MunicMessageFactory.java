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

	private Map<Long, State> stateMap = new HashMap<Long, State>();

	public MunicMessage createMessage(JSONObject jsonObject) {
		MunicMessage municMessage = new MunicMessage();
		try {
			municMessage.setJsonMessage(jsonObject);
			stateProof(municMessage);

		} catch (JSONException | ParseException e) {
			logger.error(e.toString());
			return null;
		}
		return municMessage;
	}

	private void stateProof(MunicMessage municMessage) {
		if (municMessage.isTrackEvent()) {
			synchronized (stateMap) {
				State state = stateMap.get(municMessage.getTerminalId());
				if (state == null) {
					state = new State();
					stateMap.put(municMessage.getTerminalId(), state);
				}

				// ignition
				if (municMessage.getDioIgnition() != null) {
					state.setIgnition(municMessage.getDioIgnition());
				} else {
					Boolean ignitionState = state.getIgnition();
					if (ignitionState != null) {
						municMessage.setDioIgnition(ignitionState);
					}
				}

				// gprmc valid
				if (municMessage.getGprmcValid() != null) {
					state.setGpsValid(municMessage.getGprmcValid());
				} else {
					Boolean gprmcValid = state.getGpsValid();
					if (gprmcValid != null) {
						municMessage.setGprmcValid(gprmcValid);
					}
				}

				// notify all threads
				stateMap.notifyAll();
			}
		}
	}
}
