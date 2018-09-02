package org.abarhub.angerona.config;

public class CiperCrypt {

	private String algorithme;
	private String provider;
	private byte[] keyIv;

	public String getAlgorithme() {
		return algorithme;
	}

	public void setAlgorithme(String algorithme) {
		this.algorithme = algorithme;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public byte[] getKeyIv() {
		return keyIv;
	}

	public void setKeyIv(byte[] keyIv) {
		this.keyIv = keyIv;
	}
}
