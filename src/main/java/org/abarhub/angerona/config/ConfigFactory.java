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
		CiperCrypt ciperCrypt = createCiperCrypt();
		configCrypt.setCiperCrypt(ciperCrypt);
		configCrypt.setVersion(2);
		return configCrypt;
	}

	public static CiperCrypt createCiperCrypt(){
		CiperCrypt ciperCrypt = new CiperCrypt();
		ciperCrypt.setAlgorithme("AES/CTR/PKCS7Padding");
		ciperCrypt.setProvider("BC");
		ciperCrypt.setKeyIv(new byte[]{56, -35, 13, 84, 17, 21, 90, 39, 32, 112, 115, 41, -63, 33, -92, 64});
		return ciperCrypt;
	}

}
