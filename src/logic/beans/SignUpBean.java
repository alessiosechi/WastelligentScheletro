package logic.beans;

public class SignUpBean {
	private String username;
	private String password;
	private int tipologiaId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTipologiaId() {
		return tipologiaId;
	}

	public void setTipologiaId(int tipologiaId) {
		this.tipologiaId = tipologiaId;
	}
}
