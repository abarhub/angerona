package org.abarhub.angerona.core.security;

import com.google.common.base.Preconditions;
import org.abarhub.angerona.core.coffrefort.CoffreFort;
import org.abarhub.angerona.core.coffrefort.Message;
import org.abarhub.angerona.core.coffrefort.ToolsCoffreFort;
import org.abarhub.angerona.core.config.CiperCrypt;
import org.abarhub.angerona.core.config.ConfigCrypt;
import org.abarhub.angerona.core.config.ConfigFactory;
import org.abarhub.angerona.core.exception.CoffreFortException;
import org.abarhub.angerona.core.utils.Config;
import org.abarhub.angerona.core.utils.Resultat;
import org.abarhub.angerona.core.utils.Tools;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.util.Random;

public class Cryptage2 implements ICryptage {

	private final static Logger LOGGER = LoggerFactory.getLogger(Cryptage2.class);

	//private static final String CLEF_CRYPTAGE = "clef_cryptage";

	private Config config;
	private CoffreFort coffreFort;

	public Cryptage2(Config config) {
		config = Preconditions.checkNotNull(config);
		if (Security.getProvider("BC") == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
		this.config = config;
		if (config.getRep_data() == null) {
			throw new IllegalArgumentException();
		}
		coffreFort = new CoffreFort();
		coffreFort.setMessage(new Message());
		coffreFort.setConfig(ConfigFactory.createNewConfigCrypt());
	}

	@Override
	public void lecture(char[] pwd) throws IOException, DataLengthException, GeneralSecurityException, CoffreFortException {
		Cipher cipher;
		byte buf[];
		int len;
		String buf3;
		Preconditions.checkNotNull(coffreFort);
		LOGGER.info("lecture data");
		LOGGER.debug("debut lecture");
//		ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
//		Path fichierCoffreFort = this.getPathCoffreFort();
//		if (fichierCoffreFort == null || !Files.exists(fichierCoffreFort)) {
//			throw new FileNotFoundException("Le fichier coffre fort n'existe pas");
//		}
		CoffreFort coffreFort = this.coffreFort;//toolsCoffreFort.load(fichierCoffreFort, pwd);

		buf = new byte[512];
		cipher = getBlockCipher(false, pwd);
		ByteArrayOutputStream buf2;

		ByteArrayInputStream inputStream = new ByteArrayInputStream(coffreFort.getMessage().getMessageCrypte());
		try (CipherInputStream in2 = new CipherInputStream(inputStream, cipher)) {
			buf2 = new ByteArrayOutputStream();
			while ((len = in2.read(buf)) != -1) {
				buf2.write(buf, 0, len);
			}
		}
		buf3 = buf2.toString(StandardCharsets.UTF_8.displayName());
		coffreFort.getMessage().setMessage(buf3);
		//this.coffreFort = coffreFort;
		LOGGER.debug("fin lecture");
	}

	@Override
	public void ecriture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, CoffreFortException {
		Preconditions.checkNotNull(pwd, "Le mot de passe ne peut pas être null");
		Preconditions.checkArgument(pwd.length > 0, "Le mot de passe ne peut pas être null");
		Preconditions.checkNotNull(coffreFort);
		Preconditions.checkNotNull(coffreFort.getMessage());
		Preconditions.checkNotNull(coffreFort.getMessage().getMessage());

		LOGGER.debug("debut ecriture");
		Cipher cipher;
		ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
		toolsCoffreFort.backup();
		LOGGER.info("ecriture data");
		cipher = getBlockCipher(true, pwd);
		byte texte[];
		String contenu = coffreFort.getMessage().getMessage();
		texte = contenu.getBytes(StandardCharsets.UTF_8);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (CipherOutputStream out2 = new CipherOutputStream(outputStream, cipher)) {
			out2.write(texte);
		}

		byte[] messageCrypte = outputStream.toByteArray();

		coffreFort.getMessage().setMessageCrypte(messageCrypte);

		coffreFort.setKeystorePassword(pwd);

		Path path = getPathCoffreFort();

		toolsCoffreFort.save(coffreFort, path);

		LOGGER.debug("fin ecriture");
	}

	private Cipher getBlockCipher(boolean cryptage, char[] pwd) throws GeneralSecurityException {
		Preconditions.checkNotNull(pwd, "Le mot de passe ne peut pas être null");
		Preconditions.checkArgument(pwd.length > 0, "Le mot de passe ne peut pas être null");
		Preconditions.checkNotNull(coffreFort);
		Preconditions.checkNotNull(coffreFort.getKeystore());
		Preconditions.checkNotNull(coffreFort.getConfig());
		//Preconditions.checkNotNull(coffreFort.getConfig().getCiperCrypt());

		LOGGER.debug("debut getBlockCipher");
		KeyStore keyStore = coffreFort.getKeystore();
		if (keyStore == null) {
			throw new IllegalArgumentException();
		}
		LOGGER.debug("getCiperCrypt ...");
		CiperCrypt ciperCrypt = coffreFort.getConfig().getCiperCrypt();
		LOGGER.debug("getCiperCrypt ok");
		if (ciperCrypt == null) {
			LOGGER.debug("ConfigFactory.createCiperCrypt ...");
			ciperCrypt = ConfigFactory.createCiperCrypt();
			LOGGER.debug("ConfigFactory.createCiperCrypt ok");
			LOGGER.debug("setCiperCrypt ...");
			coffreFort.getConfig().setCiperCrypt(ciperCrypt);
			LOGGER.debug("setCiperCrypt ok");
		}
		Cipher cipher;
		if (ciperCrypt.getProvider() == null || ciperCrypt.getProvider().trim().isEmpty()) {
			LOGGER.debug("Cipher.getInstance ...");
			cipher = Cipher.getInstance(ciperCrypt.getAlgorithme());
			LOGGER.debug("Cipher.getInstance ok");
		} else {
			LOGGER.debug("Cipher.getInstance2 ...");
			cipher = Cipher.getInstance(ciperCrypt.getAlgorithme(), ciperCrypt.getProvider());//new DESEngine();
			LOGGER.debug("Cipher.getInstance2 ok");
		}
		SecretKeySpec key;
		LOGGER.debug("get ivBytes ...");
		byte ivBytes[] = new byte[]{56, -35, 13, 84, 17, 21, 90, 39, 32, 112, 115, 41, -63, 33, -92, 64};
		if (ciperCrypt.getKeyIv() != null && ciperCrypt.getKeyIv().length > 0) {
			ivBytes = ciperCrypt.getKeyIv();
		}
		LOGGER.debug("get ivBytes ok");

		LOGGER.debug("keyStore.getKey ...");
		Key clef = keyStore.getKey(coffreFort.getConfig().getKeyCrypt().getSecretKeyEntry(), pwd);
		LOGGER.debug("keyStore.getKey ok");
		LOGGER.debug("SecretKeySpec ...");
		key = new SecretKeySpec(clef.getEncoded(), clef.getAlgorithm());
		LOGGER.debug("SecretKeySpec ok");
		LOGGER.debug("IvParameterSpec ...");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		LOGGER.debug("IvParameterSpec ok");
		LOGGER.debug("debut cipher.init (cryptage={})", cryptage);
		if (cryptage) {
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		}
		LOGGER.debug("fin cipher.init");
		if (ciperCrypt != null) {
			ciperCrypt.setKeyIv(ivBytes);
		}
		LOGGER.debug("fin getBlockCipher");
		return cipher;
	}

	@Override
	public String getContenu() {
		Preconditions.checkNotNull(coffreFort);
		Preconditions.checkNotNull(coffreFort.getMessage());
		return coffreFort.getMessage().getMessage();
	}

	@Override
	public void setContenu(String contenu) {
		Preconditions.checkNotNull(contenu);
		Preconditions.checkNotNull(coffreFort);
		Preconditions.checkNotNull(coffreFort.getMessage());
		Message message = new Message();
		message.setMessage(contenu);
		coffreFort.setMessage(message);
	}

	@Override
	public void init_keystore(char[] password) throws GeneralSecurityException, IOException {
		Preconditions.checkNotNull(password, "Le mot de passe ne peut pas être null");
		Preconditions.checkArgument(password.length > 0, "Le mot de passe ne peut pas être null");
		Preconditions.checkNotNull(coffreFort);
		Preconditions.checkNotNull(coffreFort.getConfig());

		LOGGER.debug("debut init_keystore");
		ConfigCrypt configCrypt = coffreFort.getConfig();

		KeyStore keyStore = KeyStore.getInstance(configCrypt.getKeystoreAlgo());
		//KeyStore keyStore = KeyStore.getInstance("PKCS12","BC");
		keyStore.load(null, null); // Initialize a blank keystore
		Random random = Tools.getSecureRandom();
		byte[] val = new byte[32];
		random.nextBytes(val);
		SecretKey key = new SecretKeySpec(val, configCrypt.getKeyCrypt().getSecretKeyCryptage());
		byte[] salt = new byte[20];
		random.nextBytes(salt);
		keyStore.setEntry(configCrypt.getKeyCrypt().getSecretKeyEntry(), new KeyStore.SecretKeyEntry(key),
				new KeyStore.PasswordProtection(password,
						configCrypt.getKeyCrypt().getProtectionAlgo(),
						new PBEParameterSpec(salt, configCrypt.getKeyCrypt().getProtectionIteration())));

		configCrypt.getKeyCrypt().setKeyIv(salt);

		coffreFort.setKeystore(keyStore);
		LOGGER.debug("fin init_keystore");
	}

	@Override
	public void loadKeyStore(char[] key) throws GeneralSecurityException, IOException, CoffreFortException {
		Preconditions.checkNotNull(key, "Le mot de passe ne peut pas être null");
		Preconditions.checkArgument(key.length > 0, "Le mot de passe ne peut pas être null");
		LOGGER.debug("debut loadKeyStore");
		ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
		Path fichier = getPathCoffreFort();
		coffreFort = toolsCoffreFort.load(fichier, key);
		LOGGER.debug("fin loadKeyStore");
	}

	@Override
	public Resultat verifie_password(char[] password) {
		Resultat res;
		LOGGER.debug("debut verifie_password");
		res = new Resultat();
		try {
			if (password == null || password.length == 0) {
				res.addError("Mot de passe vide");
				return res;
			}
			ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
			CoffreFort coffreFort = toolsCoffreFort.load(getPathCoffreFort(), password);
			if (coffreFort == null) {
				LOGGER.error("Impossible de lire le coffre fort");
				res.addError("Impossible de lire le coffre fort");
			}
		} catch (GeneralSecurityException | IOException | CoffreFortException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			res.addError(ex.getLocalizedMessage());
		}
		LOGGER.debug("fin verifie_password");
		return res;
	}

	private Path getPathCoffreFort() {
		Path path = Paths.get(config.getRep_data().getAbsolutePath(), "coffrefort.zip");
		LOGGER.debug("getPathCoffreFort()={}", path);
		return path;
	}


}
