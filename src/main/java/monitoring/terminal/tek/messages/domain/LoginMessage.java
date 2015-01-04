package monitoring.terminal.tek.messages.domain;

public class LoginMessage extends TekMessage {
	private String authKey;

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

}
