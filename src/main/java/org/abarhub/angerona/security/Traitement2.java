package org.abarhub.angerona.security;

import com.google.gson.Gson;
import org.abarhub.angerona.coffrefort.CoffreFort;
import org.abarhub.angerona.coffrefort.Message;
import org.abarhub.angerona.coffrefort.ToolsCoffreFort;
import org.abarhub.angerona.config.ConfigCrypt;
import org.abarhub.angerona.config.ConfigFactory;
import org.abarhub.angerona.utils.Config;
import org.abarhub.angerona.utils.Tools;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Random;

public class Traitement2 extends Traitement implements ITraitement {

	private final static Logger LOGGER = LoggerFactory.getLogger(Traitement2.class);

	private Config config;

	public Traitement2() throws IOException {
		this.config = new Config();
	}

	@Override
	protected ICryptage getCrypt() throws IOException {
		return new Cryptage2((config != null) ? config : new Config());
	}

//	@Override
//	public void enregistre(String s, char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException {
//		enregistre2(s, pwd);
//	}

	private void enregistre2(String s, char[] password) {
		try {
			Path fichier = Paths.get(config.getRep_data().getAbsolutePath(), "keystore.p12");

			LOGGER.info("Enregistrement de {} ...", fichier);
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			keyStore.load(null, null); // Initialize a blank keystore
			Random random = Tools.getSecureRandom();
			byte[] val = new byte[32];
			random.nextBytes(val);
			SecretKey key = new SecretKeySpec(val, "AES");
			//char[] password = "changeit".toCharArray();
			byte[] salt = new byte[20];
			random.nextBytes(salt);
			keyStore.setEntry("clef_cryptage", new KeyStore.SecretKeyEntry(key),
					new KeyStore.PasswordProtection(password,
							"PBEWithHmacSHA512AndAES_128",
							new PBEParameterSpec(salt, 100_000)));
			//keyStore.store(Files.newOutputStream(fichier,StandardOpenOption.CREATE_NEW,
			//		StandardOpenOption.TRUNCATE_EXISTING), password);
			keyStore.store(new FileOutputStream(fichier.toFile()), password);

			LOGGER.info("Enregistrement de {} OK", fichier);

			LOGGER.info("Fichier {} existe : {}", fichier, Files.exists(fichier));

			ConfigCrypt configCrypt;
			configCrypt = ConfigFactory.createNewConfigCrypt();
			configCrypt.getKeyCrypt().setKeyIv(salt);
			configCrypt.setVersion(2);

			Gson gson;
			//gson = gsonBuilder.create();
			gson = Tools.createGson();
			//gson = new Gson();
			String json = gson.toJson(configCrypt);

			Files.write(fichier.getParent().resolve("param.json"), json.getBytes(StandardCharsets.UTF_8));

			CoffreFort coffreFort = new CoffreFort();
			coffreFort.setConfig(configCrypt);
			coffreFort.setKeystore(keyStore);
			Message message = new Message();
			message.setMessageCrypte(s.getBytes(StandardCharsets.UTF_8));
			coffreFort.setMessage(message);
			coffreFort.setKeystorePassword(password);

			Path fichierZip = Paths.get(config.getRep_data().getAbsolutePath(), "coffrefort.zip");

			ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
			toolsCoffreFort.save(coffreFort, fichierZip);

			LOGGER.info("chargement {} ...", fichierZip);

			CoffreFort coffreFort2 = toolsCoffreFort.load(fichierZip, password);

			LOGGER.info("chargement {} OK", fichierZip);

		} catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	protected void init(char[] password) {
		try {
			crypt.loadKeyStore(password);
		} catch (Exception e) {
			LOGGER.error("Erreur", e);
			throw new RuntimeException(e);
		}

	}
//	@Override
//	public void enregistre_changement_clef(String s, char[] new_password) throws GeneralSecurityException, IOException, DataLengthException, InvalidCipherTextException, DecoderException, KeyStoreHashException {
//
//	}
//
//	@Override
//	public String lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, DecoderException {
//		return null;
//	}
//
//	@Override
//	public void initialise_keystore(char[] key) throws GeneralSecurityException, IOException {
//
//	}
//
//	@Override
//	public void load_keystore(char[] key) throws GeneralSecurityException, IOException, DecoderException, KeyStoreHashException {
//
//	}
//
//	@Override
//	public Resultat verifie_password(char[] password) {
//		return null;
//	}
}
