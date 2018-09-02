package org.abarhub.angerona.config;

public class KeyCrypt {

	private String secretKeyCryptage;
	private String secretKeyEntry;
	private String protectionAlgo;
	private int protectionIteration;
	private String keystoreFilename;
	private byte[] keyIv;

	public String getSecretKeyCryptage() {
		return secretKeyCryptage;
	}

	public void setSecretKeyCryptage(String secretKeyCryptage) {
		this.secretKeyCryptage = secretKeyCryptage;
	}

	public String getSecretKeyEntry() {
		return secretKeyEntry;
	}

	public void setSecretKeyEntry(String secretKeyEntry) {
		this.secretKeyEntry = secretKeyEntry;
	}

	public String getProtectionAlgo() {
		return protectionAlgo;
	}

	public void setProtectionAlgo(String protectionAlgo) {
		this.protectionAlgo = protectionAlgo;
	}

	public int getProtectionIteration() {
		return protectionIteration;
	}

	public void setProtectionIteration(int protectionIteration) {
		this.protectionIteration = protectionIteration;
	}

	public String getKeystoreFilename() {
		return keystoreFilename;
	}

	public void setKeystoreFilename(String keystoreFilename) {
		this.keystoreFilename = keystoreFilename;
	}

	public byte[] getKeyIv() {
		return keyIv;
	}

	public void setKeyIv(byte[] keyIv) {
		this.keyIv = keyIv;
	}
}
