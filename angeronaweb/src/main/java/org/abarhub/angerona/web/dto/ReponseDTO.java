package org.abarhub.angerona.web.dto;

public class ReponseDTO {

	private String reponse;

	private String reponseHmac;

	private String iv;

	private String ivHmac;

	private String cle;

	private String cleHmac;

	public String getReponse() {
		return reponse;
	}

	public void setReponse(String reponse) {
		this.reponse = reponse;
	}

	public String getReponseHmac() {
		return reponseHmac;
	}

	public void setReponseHmac(String reponseHmac) {
		this.reponseHmac = reponseHmac;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	public String getIvHmac() {
		return ivHmac;
	}

	public void setIvHmac(String ivHmac) {
		this.ivHmac = ivHmac;
	}

	public String getCle() {
		return cle;
	}

	public void setCle(String cle) {
		this.cle = cle;
	}

	public String getCleHmac() {
		return cleHmac;
	}

	public void setCleHmac(String cleHmac) {
		this.cleHmac = cleHmac;
	}

	@Override
	public String toString() {
		return "ReponseDTO{" +
				"reponse='" + reponse + '\'' +
				", reponseHmac='" + reponseHmac + '\'' +
				", iv='" + iv + '\'' +
				", ivHmac='" + ivHmac + '\'' +
				", cle='" + cle + '\'' +
				", cleHmac='" + cleHmac + '\'' +
				'}';
	}
}
