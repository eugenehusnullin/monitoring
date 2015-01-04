package monitoring.handler;

import monitoring.domain.Message;
import monitoring.handler.wialon.DataPacket;

public interface MessageFormatter {
	DataPacket fromTerminalFormatt(Message message);

	Message toTerminalFormatt(Message message);
}
