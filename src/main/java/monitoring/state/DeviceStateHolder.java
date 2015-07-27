package monitoring.state;

import monitoring.handler.position.Position;

public interface DeviceStateHolder {

	boolean checkCrazy(Position position);
}
