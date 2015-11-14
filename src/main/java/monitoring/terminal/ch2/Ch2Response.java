package monitoring.terminal.ch2;

import monitoring.domain.Message;

public class Ch2Response implements Message {
	private long terminalId;
	private String response;

	@Override
	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}
