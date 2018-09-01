package org.abarhub.angerona.security;

import java.util.Date;

public class ConfigCrypt {

	private String keystoreAlgo;
	private String secretKeyCryptage;
	private String secretKeyEntry;
	private String protectionAlgo;
	private long protectionIteration;
	private String keystoreFilename;
	private byte[] keyIv;
	private Date dateCreation;

	public String getKeystoreAlgo() {
		return keystoreAlgo;
	}

	public void setKeystoreAlgo(String keystoreAlgo) {
		this.keystoreAlgo = keystoreAlgo;
	}

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

	public long getProtectionIteration() {
		return protectionIteration;
	}

	public void setProtectionIteration(long protectionIteration) {
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

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
}
