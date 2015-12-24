package monitoring.terminal.ch2;

import monitoring.handler.AccelerometrConverter;
import monitoring.handler.GyroConverter;
import monitoring.handler.HandlerStrategyAdapter;
import monitoring.handler.MessageFormatter;
import monitoring.handler.PositionConverter;
import monitoring.handler.TerminalSession;

public class Ch2HandlerStrategy extends HandlerStrategyAdapter {
	private PositionConverter positionConverter;
	private MessageFormatter messageFormatter;
	private Ch2TerminalsSessionsKeeper terminalsSessionsKeeper;
	private AccelerometrConverter accelerometrConverter;
	private GyroConverter gyroConverter;

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
		return terminalsSessionsKeeper.getTerminalSession(terminalId);
	}

	@Override
	public AccelerometrConverter getAccelerometrConverter() {
		return accelerometrConverter;
	}

	@Override
	public GyroConverter getGyroConverter() {
		return gyroConverter;
	}

	public void setAccelerometrConverter(AccelerometrConverter accelerometrConverter) {
		this.accelerometrConverter = accelerometrConverter;
	}

	public void setPositionConverter(PositionConverter positionConverter) {
		this.positionConverter = positionConverter;
	}

	public void setMessageFormatter(MessageFormatter messageFormatter) {
		this.messageFormatter = messageFormatter;
	}

	public void setTerminalsSessionsKeeper(Ch2TerminalsSessionsKeeper terminalsSessionsKeeper) {
		this.terminalsSessionsKeeper = terminalsSessionsKeeper;
	}

	public void setGyroConverter(GyroConverter gyroConverter) {
		this.gyroConverter = gyroConverter;
	}
}
