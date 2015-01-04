package monitoring.handler;

import monitoring.domain.Message;
import monitoring.handler.position.Position;

public interface PositionConverter {
	Position convert(Message message);
}
