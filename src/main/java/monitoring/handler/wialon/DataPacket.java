package monitoring.handler.wialon;

import java.util.HashMap;
import java.util.Map;

import monitoring.handler.position.Position;

public class DataPacket extends Position {
	private Map<String, String> params = new HashMap<String, String>();

	public Map<String, String> getParams() {
		return params;
	}
}
