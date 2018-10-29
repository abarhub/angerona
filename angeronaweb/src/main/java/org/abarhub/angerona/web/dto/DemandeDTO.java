package org.abarhub.angerona.web.dto;

public class DemandeDTO {

	private String password;

	private String cle;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}

	@Override
	public String toString() {
		return "DemandeDTO{" +
				"password='" + password + '\'' +
				", cle='" + cle + '\'' +
				'}';
	}
}
