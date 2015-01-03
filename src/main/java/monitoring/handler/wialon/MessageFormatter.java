package monitoring.handler.wialon;

import monitoring.domain.Message;

public interface MessageFormatter {
	Message formatt(Message message);
}
