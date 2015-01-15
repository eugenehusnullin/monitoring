package monitoring.handler;


public interface HandlerStrategy {

	PositionConverter getPositionConverter();

	MessageFormatter getWialonMessageFormatter();
	
	TerminalSession getTerminalSession(long terminalId);
	
	AccelerometrConverter getAccelerometrConverter();
}
