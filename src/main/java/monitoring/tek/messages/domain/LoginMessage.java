package monitoring.tek.messages.domain;


public class LoginMessage extends GeneralResponseMessage {
	private String authKey;

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

}
