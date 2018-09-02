package org.abarhub.angerona.config;

import java.time.LocalDateTime;

public class ConfigCrypt {

	private String keystoreAlgo;
	private LocalDateTime dateCreation;
	private KeyCrypt keyCrypt;
	private int version;
	private CiperCrypt ciperCrypt;

	public String getKeystoreAlgo() {
		return keystoreAlgo;
	}

	public void setKeystoreAlgo(String keystoreAlgo) {
		this.keystoreAlgo = keystoreAlgo;
	}

	public LocalDateTime getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(LocalDateTime dateCreation) {
		this.dateCreation = dateCreation;
	}

	public KeyCrypt getKeyCrypt() {
		return keyCrypt;
	}

	public void setKeyCrypt(KeyCrypt keyCrypt) {
		this.keyCrypt = keyCrypt;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public CiperCrypt getCiperCrypt() {
		return ciperCrypt;
	}

	public void setCiperCrypt(CiperCrypt ciperCrypt) {
		this.ciperCrypt = ciperCrypt;
	}
}
