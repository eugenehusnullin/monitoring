package monitoring.handler.position;

import monitoring.domain.Message;
import monitoring.handler.position.domain.Position;

public interface PositionConverter {
	Position convert(Message message);
}
