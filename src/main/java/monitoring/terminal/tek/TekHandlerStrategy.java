package monitoring.terminal.tek;

import monitoring.handler.HandlerStrategyAdapter;
import monitoring.handler.MessageFormatter;
import monitoring.handler.PositionConverter;
import monitoring.handler.TerminalSession;

public class TekHandlerStrategy extends HandlerStrategyAdapter {

	private PositionConverter positionConverter;
	private MessageFormatter messageFormatter;
	private TekTerminalSessionKeeper terminalSessionKeeper;

	@Override
	public PositionConverter getPositionConverter() {
		return positionConverter;
	}

	@Override
	public MessageFormatter getWialonMessageFormatter() {
		return messageFormatter;
	}

	@Override
	public TerminalSession getTerminalSession(long terminalId) {
		return terminalSessionKeeper.getTerminalSession(terminalId);
	}

	public void setPositionConverter(PositionConverter positionConverter) {
		this.positionConverter = positionConverter;
	}

	public void setMessageFormatter(MessageFormatter messageFormatter) {
		this.messageFormatter = messageFormatter;
	}

	public void setTerminalSessionKeeper(TekTerminalSessionKeeper terminalSessionKeeper) {
		this.terminalSessionKeeper = terminalSessionKeeper;
	}

}
