package org.abarhub.angerona.config;

import java.time.LocalDateTime;
import java.util.Date;

public class ConfigCrypt {

	private String keystoreAlgo;
	private LocalDateTime dateCreation;
	private KeyCrypt keyCrypt;

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
}
