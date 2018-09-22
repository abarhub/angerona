package org.abarhub.angerona.web.services;

import com.google.common.base.Verify;
import org.abarhub.angerona.core.exception.CoffreFortException;
import org.abarhub.angerona.core.exception.KeyStoreHashException;
import org.abarhub.angerona.core.security.Traitement;
import org.abarhub.angerona.core.utils.Tools;
import org.abarhub.angerona.web.dto.ReponseDTO;
import org.abarhub.angerona.web.util.Base64Util;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service
public class CryptageService {

	private static Logger LOGGER = LoggerFactory.getLogger(CryptageService.class);

	@Autowired
	private RandomService randomService;

	private KeyFactory factoryRSA;
	private KeyGenerator keyGenerator;
	private Cipher cipher;

	@PostConstruct
	public void init() {
		Security.addProvider(new BouncyCastleProvider());
		LOGGER.info("BouncyCastle provider added.");
	}

	public ReponseDTO getMessage(String password, String cle) throws GeneralSecurityException, IOException, KeyStoreHashException, CoffreFortException, InvalidCipherTextException {

		Verify.verifyNotNull(password);
		Verify.verify(!password.isEmpty());
		Verify.verifyNotNull(cle);
		Verify.verify(!cle.isEmpty());

		LOGGER.debug("debut getMessage");

		ReponseDTO reponseDTO = new ReponseDTO();

		//LOGGER.info("password={}", password);

		KeyFactory factory = getKeyFactory();

		RSAPublicKey pub = (RSAPublicKey) extractPublicKey(factory, cle);


		String message = "message0-" + System.currentTimeMillis();

		LOGGER.debug("lectureFichier ...");
		message = lectureFichier(password);
		LOGGER.debug("lectureFichier ok");

		LOGGER.debug("generateSecretKey ...");
		SecretKey secretKey = generateSecretKey();
		LOGGER.debug("generateSecretKey ok");

		LOGGER.debug("getInstance aes ...");
		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
		LOGGER.debug("getInstance aes ok");

		LOGGER.debug("generate iv ...");
		byte[] iv = new byte[aesCipher.getBlockSize()];
		randomService.nextBytes(iv);
		IvParameterSpec ivParams = new IvParameterSpec(iv);

		reponseDTO.setIv(Base64Util.encode(iv));
		LOGGER.debug("generate iv ok");

		LOGGER.debug("crypt ...");
		aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

		byte[] byteCipherText = aesCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

		reponseDTO.setReponse(Base64Util.encode(byteCipherText));
		LOGGER.debug("crypt ok");

		LOGGER.debug("crypt2 ...");
		byte[] crypte = encrypt(pub, secretKey.getEncoded());

		reponseDTO.setCle(Base64Util.encode(crypte));
		LOGGER.debug("crypt2 ok");

		LOGGER.debug("fin getMessage");

		return reponseDTO;
	}

	private KeyFactory getKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
		if(factoryRSA==null) {
			LOGGER.debug("getInstance rsa ...");
			factoryRSA = KeyFactory.getInstance("RSA", "BC");
			LOGGER.debug("getInstance rsa ok");
		}
		return factoryRSA;
	}

	private String lectureFichier(String password) throws IOException, KeyStoreHashException, CoffreFortException, GeneralSecurityException, InvalidCipherTextException {

		Traitement tr = Tools.createTraitement();
		char[] passwordBytes=conv(password);
		tr.load_keystore(passwordBytes);
		String s = tr.lecture(passwordBytes);
		if (s != null) {
			return s;
		}

		return null;
	}

	private char[] conv(String password) {
		Verify.verifyNotNull(password);
		Verify.verify(!password.isEmpty());

		char[] buf = new char[password.length()];

		for (int i = 0; i < password.length(); i++) {
			buf[i] = password.charAt(i);
		}

		return buf;
	}

	private SecretKey generateSecretKey() throws NoSuchAlgorithmException, NoSuchProviderException {
		LOGGER.debug("generateSecretKey ...");
		KeyGenerator keyGen = getKeyGeneratorAES();
		keyGen.init(128);
		final SecretKey secretKey = keyGen.generateKey();
		LOGGER.debug("generateSecretKey ok");
		return secretKey;
	}

	private KeyGenerator getKeyGeneratorAES() throws NoSuchAlgorithmException, NoSuchProviderException {
		if(keyGenerator==null) {
			LOGGER.debug("getKeyGeneratorAES ...");
			keyGenerator = KeyGenerator.getInstance("AES", "BC");
			LOGGER.debug("getKeyGeneratorAES ok");
		}
		return keyGenerator;
	}

	public byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		//Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
		if(cipher==null) {
			LOGGER.debug("getCipherInstance RSA ...");
			cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
			LOGGER.debug("getCipherInstance RSA ok");
		}
		LOGGER.debug("cipher.init ENCRYPT_MODE ...");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		LOGGER.debug("cipher.init ENCRYPT_MODE ok");
		return cipher.doFinal(plaintext);
	}


	private PublicKey extractPublicKey(KeyFactory factory, String s) throws InvalidKeySpecException, IOException {
		LOGGER.debug("extractPublicKey ...");
		byte[] content = getPemObject(s).getContent();
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
		final PublicKey publicKey = factory.generatePublic(pubKeySpec);
		LOGGER.debug("extractPublicKey ok");
		return publicKey;
	}


	private PemObject getPemObject(String s) throws IOException {
		PemObject pemReader2;
		LOGGER.debug("getPemObject ...");
		try (PemReader pemReader = new PemReader(new StringReader(s))) {
			pemReader2 = pemReader.readPemObject();
		}
		LOGGER.debug("getPemObject ok");
		return pemReader2;
	}
}
