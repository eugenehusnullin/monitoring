package monitoring.state;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import monitoring.handler.position.Position;

public class DeviceStateHolderImpl implements DeviceStateHolder {

	private Long checkInterval;
	private Long maxSpeed;
	private Map<Long, Date> lastBads = new HashMap<Long, Date>();

	public void setCheckInterval(Long checkInterval) {
		this.checkInterval = checkInterval;
	}

	public void setMaxSpeed(Long maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	public boolean checkCrazy(Position position) {
		synchronized (lastBads) {
			if (!position.getGpsValid()) {
				lastBads.put(position.getTerminalId(), position.getDate());
				return true;

			} else {
				Date last = lastBads.get(position.getTerminalId());
				if (last == null) {
					return false;

				} else {
					Long diffInSeconds = (position.getDate().getTime() - last.getTime()) / 1000;
					if (diffInSeconds > checkInterval) {
						return false;

					} else {
						if (position.getSpeed() < maxSpeed) {
							return false;

						} else {
							lastBads.put(position.getTerminalId(), position.getDate());
							return true;
						}
					}
				}
			}
		}
	}
}
