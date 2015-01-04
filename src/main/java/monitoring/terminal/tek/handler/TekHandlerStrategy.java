package monitoring.terminal.tek.handler;

import monitoring.handler.HandlerStrategyAdapter;
import monitoring.handler.MessageFormatter;
import monitoring.handler.PositionConverter;
import monitoring.handler.TerminalSession;
import monitoring.terminal.tek.TekIoHandler;

public class TekHandlerStrategy extends HandlerStrategyAdapter {

	private TekPositionConverter tekPositionConverter;
	private TekIoHandler handler;

	@Override
	public PositionConverter getPositionConverter() {
		return tekPositionConverter;
	}

	@Override
	public MessageFormatter getWialonMessageFormatter() {
		return null;
	}

	@Override
	public TerminalSession getTerminalSession(long terminalId) {
		return handler.getTerminalSession(terminalId);
	}

}
