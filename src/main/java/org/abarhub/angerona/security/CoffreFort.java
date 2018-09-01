package org.abarhub.angerona.security;

import org.abarhub.angerona.config.ConfigCrypt;

import java.nio.file.Path;
import java.security.KeyStore;

public class CoffreFort {

	private byte[] messageCrypte;
	private KeyStore keystore;
	private ConfigCrypt config;

	public void save(Path fichier){

	}

	public void load(Path fichier){

	}

	public byte[] getMessageCrypte() {
		return messageCrypte;
	}

	public void setMessageCrypte(byte[] messageCrypte) {
		this.messageCrypte = messageCrypte;
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
}
