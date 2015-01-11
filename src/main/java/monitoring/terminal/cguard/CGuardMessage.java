package monitoring.terminal.cguard;

import monitoring.domain.Message;

public class CGuardMessage implements Message {
	private long terminalId;
	private String commandType;

	public String getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	@Override
	public long getTerminalId() {
		return terminalId;
	}

}
