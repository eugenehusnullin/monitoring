package monitoring.terminal.ch2;

import monitoring.handler.HandlerStrategyAdapter;
import monitoring.handler.MessageFormatter;
import monitoring.handler.PositionConverter;

public class Ch2HandlerStrategy extends HandlerStrategyAdapter {
	private PositionConverter positionConverter;
	private MessageFormatter messageFormatter;

	@Override
	public PositionConverter getPositionConverter() {
		return positionConverter;
	}

	@Override
	public MessageFormatter getWialonMessageFormatter() {
		return messageFormatter;
	}

	public void setPositionConverter(PositionConverter positionConverter) {
		this.positionConverter = positionConverter;
	}

	public void setMessageFormatter(MessageFormatter messageFormatter) {
		this.messageFormatter = messageFormatter;
	}
}
