package org.abarhub.angerona.config;

import java.time.LocalDateTime;

public class ConfigFactory {

	public static ConfigCrypt createNewConfigCrypt() {
		ConfigCrypt configCrypt = new ConfigCrypt();
		configCrypt.setDateCreation(LocalDateTime.now());
		configCrypt.setKeystoreAlgo("PKCS12");
		KeyCrypt keyCrypt = new KeyCrypt();
		keyCrypt.setSecretKeyCryptage("AES");
		keyCrypt.setSecretKeyEntry("clef_cryptage");
		keyCrypt.setProtectionAlgo("PBEWithHmacSHA512AndAES_128");
		keyCrypt.setProtectionIteration(100_000);
		configCrypt.setKeyCrypt(keyCrypt);
		configCrypt.setVersion(2);
		return configCrypt;
	}

}
