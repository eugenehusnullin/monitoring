package monitoring.terminal.munic;

import monitoring.handler.AccelerometrConverter;
import monitoring.handler.HandlerStrategyAdapter;
import monitoring.handler.MessageFormatter;
import monitoring.handler.PositionConverter;
import monitoring.handler.TerminalSession;

public class MunicHandlerStrategy extends HandlerStrategyAdapter {

	private PositionConverter positionConverter;
	private MessageFormatter messageFormatter;
	private AccelerometrConverter accelerometrConverter;

	@Override
	public PositionConverter getPositionConverter() {
		return positionConverter;
	}

	@Override
	public TerminalSession getTerminalSession(long terminalId) {
		return null;
	}

	@Override
	public MessageFormatter getWialonMessageFormatter() {
		return messageFormatter;
	}

	@Override
	public AccelerometrConverter getAccelerometrConverter() {
		return accelerometrConverter;
	}

	public void setPositionConverter(PositionConverter positionConverter) {
		this.positionConverter = positionConverter;
	}

	public void setMessageFormatter(MessageFormatter messageFormatter) {
		this.messageFormatter = messageFormatter;
	}

	public void setAccelerometrConverter(AccelerometrConverter accelerometrConverter) {
		this.accelerometrConverter = accelerometrConverter;
	}
}
