package org.abarhub.angerona.web.services;

import org.abarhub.angerona.web.dto.ReponseDTO;
import org.abarhub.angerona.web.util.Base64Util;
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

	@PostConstruct
	public void init() {
		Security.addProvider(new BouncyCastleProvider());
		LOGGER.info("BouncyCastle provider added.");
	}

	public ReponseDTO getMessage(String password, String cle) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		ReponseDTO reponseDTO=new ReponseDTO();

		LOGGER.info("password={}", password);


		KeyFactory factory = KeyFactory.getInstance("RSA", "BC");

		RSAPublicKey pub = (RSAPublicKey) extractPublicKey(factory, cle);


		String message = "message0-" + System.currentTimeMillis();

		SecretKey secretKey = generateSecretKey();

		Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");

		byte[] iv = new byte[aesCipher.getBlockSize()];
		randomService.nextBytes(iv);
		IvParameterSpec ivParams = new IvParameterSpec(iv);

		reponseDTO.setIv(Base64Util.encode(iv));

		aesCipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParams);

		byte[] byteCipherText = aesCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));

		reponseDTO.setReponse(Base64Util.encode(byteCipherText));

		byte[] crypte = encrypt(pub, secretKey.getEncoded());

		reponseDTO.setCle(Base64Util.encode(crypte));

		return reponseDTO;
	}

	private SecretKey generateSecretKey() throws NoSuchAlgorithmException, NoSuchProviderException {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
		keyGen.init(128);
		return keyGen.generateKey();
	}

	public byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		//Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(plaintext);
	}


	private PublicKey extractPublicKey(KeyFactory factory, String s) throws InvalidKeySpecException, IOException {
		byte[] content = getPemObject(s).getContent();
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
		return factory.generatePublic(pubKeySpec);
	}


	private PemObject getPemObject(String s) throws IOException {
		PemObject pemReader2;
		try (PemReader pemReader = new PemReader(new StringReader(s))) {
			pemReader2 = pemReader.readPemObject();
		}
		return pemReader2;
	}
}
