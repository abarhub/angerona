package org.abarhub.angerona.security;

import com.google.common.base.Preconditions;
import org.abarhub.angerona.coffrefort.CoffreFort;
import org.abarhub.angerona.coffrefort.Message;
import org.abarhub.angerona.coffrefort.ToolsCoffreFort;
import org.abarhub.angerona.config.ConfigCrypt;
import org.abarhub.angerona.config.ConfigFactory;
import org.abarhub.angerona.exception.CoffreFortException;
import org.abarhub.angerona.exception.KeyStoreHashException;
import org.abarhub.angerona.utils.Config;
import org.abarhub.angerona.utils.Resultat;
import org.abarhub.angerona.utils.Tools;
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
import java.io.*;
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

	private static final String CLEF_CRYPTAGE = "clef_cryptage";

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
		BufferedInputStream in = null;
		byte buf[];
		int len;
		String buf3;
		LOGGER.info("lecture data");
		//f = donne_fichier_data();
		ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
		Path fichierCoffreFort = this.getPathCoffreFort();
		if (fichierCoffreFort == null || !Files.exists(fichierCoffreFort)) {
			throw new FileNotFoundException("Le fichier coffre fort n'existe pas");
		}
		CoffreFort coffreFort = toolsCoffreFort.load(fichierCoffreFort, pwd);

		buf = new byte[512];
		cipher = getBlockCipher(false, pwd);
		//CipherInputStream in2;
		ByteArrayOutputStream buf2;

		ByteArrayInputStream inputStream = new ByteArrayInputStream(coffreFort.getMessage().getMessageCrypte());
		try (CipherInputStream in2 = new CipherInputStream(inputStream, cipher)) {
			//in = new BufferedInputStream(new FileInputStream(f));
			//in2 = new CipherInputStream(inputStream, cipher);
			buf2 = new ByteArrayOutputStream();
			while ((len = in2.read(buf)) != -1) {
				buf2.write(buf, 0, len);
			}
			in2.close();
		} finally {
//			if (in != null)
//				in.close();
//			in = null;
		}
		buf3 = buf2.toString(StandardCharsets.UTF_8.displayName());
//		if (verifie(buf2.toByteArray(), true)) {
////			contenu = buf3;
////		} else {
////			LOGGER.error("Erreur dans le fichier avec les hash");
////			throw new IllegalArgumentException();
////		}
		coffreFort.getMessage().setMessage(buf3);
		this.coffreFort = coffreFort;
	}

	@Override
	public void ecriture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, CoffreFortException {
		Preconditions.checkNotNull(pwd, "Le mot de passe ne peut pas être null");
		Preconditions.checkArgument(pwd.length > 0, "Le mot de passe ne peut pas être null");
		Preconditions.checkNotNull(coffreFort);
		Preconditions.checkNotNull(coffreFort.getMessage());
		Preconditions.checkNotNull(coffreFort.getMessage().getMessage());

		//File f, f2;
		Cipher cipher;
		//BufferedOutputStream out = null;
		//backup();
		ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
		toolsCoffreFort.backup();
		LOGGER.info("ecriture data");
		//f = donne_fichier_data();
		cipher = getBlockCipher(true, pwd);
		//CipherOutputStream out2 = null;
		byte texte[];
		String contenu = coffreFort.getMessage().getMessage();
		texte = contenu.getBytes(StandardCharsets.UTF_8);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try (CipherOutputStream out2 = new CipherOutputStream(outputStream, cipher)) {
			//out = new BufferedOutputStream(outputStream);
			//out2 = new CipherOutputStream(outputStream, cipher);
			out2.write(texte);
		} /*finally {
			if (out2 != null) {
				out2.close();
			} else if (out != null) {
				out.close();
			}
		}*/

		byte[] messageCrypte = outputStream.toByteArray();

		coffreFort.getMessage().setMessageCrypte(messageCrypte);

		coffreFort.setKeystorePassword(pwd);

		//f2 = donne_fichier_data_hash();//new File(f.getParent(),"hash.asc");
		//enregistre_hash(texte, f2);
		Path path = getPathCoffreFort();

		toolsCoffreFort.save(coffreFort, path);

	}

	private Cipher getBlockCipher(boolean cryptage, char[] pwd) throws GeneralSecurityException {
		Preconditions.checkNotNull(pwd, "Le mot de passe ne peut pas être null");
		Preconditions.checkArgument(pwd.length > 0, "Le mot de passe ne peut pas être null");
		Preconditions.checkNotNull(coffreFort);
		Preconditions.checkNotNull(coffreFort.getKeystore());

		KeyStore keyStore = coffreFort.getKeystore();
		if (keyStore == null) {
			throw new IllegalArgumentException();
		}
		Cipher cipher = Cipher.getInstance("AES/CTR/PKCS7Padding", "BC");//new DESEngine();
		//Cipher cipher = Cipher.getInstance("AES/CTR/PKCS7Padding");//new DESEngine();
		//BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
		//String keyString="ABCDEF";
		//byte[] key = keyString.getBytes();
		SecretKeySpec key;
		//char[] pwd="abc".toCharArray();
		//byte clef[]=new byte[]{30,62,-23,41,27,8,61,6,70,111,-109,-39,88,-7,48,0};
		byte ivBytes[] = new byte[]{56, -35, 13, 84, 17, 21, 90, 39, 32, 112, 115, 41, -63, 33, -92, 64};
		Key clef = keyStore.getKey(CLEF_CRYPTAGE, pwd);
		//byte ivBytes[]=key_store.getKey(IV_CRYPTAGE, pwd);
		key = new SecretKeySpec(clef.getEncoded(), clef.getAlgorithm());
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		if (cryptage) {
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		}
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

		ConfigCrypt configCrypt = coffreFort.getConfig();

		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		//KeyStore keyStore = KeyStore.getInstance("PKCS12","BC");
		keyStore.load(null, null); // Initialize a blank keystore
		Random random = Tools.getSecureRandom();
		byte[] val = new byte[32];
		random.nextBytes(val);
		SecretKey key = new SecretKeySpec(val, "AES");
		//char[] password = "changeit".toCharArray();
		byte[] salt = new byte[20];
		random.nextBytes(salt);
		keyStore.setEntry(CLEF_CRYPTAGE, new KeyStore.SecretKeyEntry(key),
				new KeyStore.PasswordProtection(password,
						"PBEWithHmacSHA512AndAES_128",
						new PBEParameterSpec(salt, 100_000)));

		configCrypt.getKeyCrypt().setKeyIv(salt);

		coffreFort.setKeystore(keyStore);
	}

	@Override
	public void loadKeyStore(char[] key) throws GeneralSecurityException, IOException, KeyStoreHashException, CoffreFortException {
		Preconditions.checkNotNull(key, "Le mot de passe ne peut pas être null");
		Preconditions.checkArgument(key.length > 0, "Le mot de passe ne peut pas être null");
		ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
		Path fichier = getPathCoffreFort();
		coffreFort = toolsCoffreFort.load(fichier, key);
	}

	@Override
	public Resultat verifie_password(char[] password) {
		Resultat res;
		res = new Resultat();
		try {
			//KeyStore key_store0;
			if (password == null || password.length == 0) {
				res.addError("Mot de passe vide");
				return res;
			}
			//key_store0 = KeyStore.getInstance(KeyStoreFormat, "BC");
			//key_store0.load(new FileInputStream(KeyStoreFile()), password);
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
		return res;
	}

	private Path getPathCoffreFort() {
		Path path = Paths.get(config.getRep_data().getAbsolutePath(), "coffrefort.zip");
		LOGGER.debug("getPathCoffreFort()={}", path);
		return path;
	}


}
