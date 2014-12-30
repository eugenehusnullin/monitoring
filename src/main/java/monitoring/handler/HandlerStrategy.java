package monitoring.handler;

import monitoring.handler.position.PositionConverter;

public interface HandlerStrategy {

	PositionConverter getPositionConverter();
}
