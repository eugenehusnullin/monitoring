package monitoring.terminal.ch2;

import monitoring.domain.Message;

public class Ch2Response implements Message {
	private long terminalId;
	private String response;
	private String responseType;
	private String raw;

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

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

}
