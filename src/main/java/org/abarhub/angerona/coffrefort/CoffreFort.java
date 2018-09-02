package org.abarhub.angerona.coffrefort;

import org.abarhub.angerona.config.ConfigCrypt;

import java.nio.file.Path;
import java.security.KeyStore;

public class CoffreFort {

	private Message message;
	private KeyStore keystore;
	private char[] keystorePassword;
	private ConfigCrypt config;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public KeyStore getKeystore() {
		return keystore;
	}

	public void setKeystore(KeyStore keystore) {
		this.keystore = keystore;
	}

	public ConfigCrypt getConfig() {
		return config;
	}

	public void setConfig(ConfigCrypt config) {
		this.config = config;
	}

	public char[] getKeystorePassword() {
		return keystorePassword;
	}

	public void setKeystorePassword(char[] keystorePassword) {
		this.keystorePassword = keystorePassword;
	}
}
