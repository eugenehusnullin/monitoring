package monitoring.handler.wialon;

import monitoring.domain.Message;

public class WialonMessage implements Message {
	private long terminalId;
	private String strMessage;

	@Override
	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public String getStrMessage() {
		return strMessage;
	}

	public void setStrMessage(String strMessage) {
		this.strMessage = strMessage;
	}

}
