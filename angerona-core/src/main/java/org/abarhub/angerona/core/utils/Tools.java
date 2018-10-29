/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.core.utils;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.abarhub.angerona.core.json.ByteArrayDeserializer;
import org.abarhub.angerona.core.json.ByteArraySerializer;
import org.abarhub.angerona.core.json.LocalDateTimeDeserializer;
import org.abarhub.angerona.core.json.LocalDateTimeSerializer;
import org.abarhub.angerona.core.security.Traitement;
import org.abarhub.angerona.core.security.Traitement2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author abarret
 */
public class Tools {

	private final static Logger LOGGER = LoggerFactory.getLogger(Tools.class);

	public static SecureRandom getSecureRandom() {
		SecureRandom rand;
		LOGGER.debug("debut getSecureRandom");
		int nb = 16;
		byte b, tab[];
		rand = new SecureRandom();
		tab = rand.generateSeed(100);
		rand = new SecureRandom(tab);
		LOGGER.debug("fin getSecureRandom");
		return rand;
	}

	public static byte[] generate_random_bytes(int size) {
		byte tab[];
		SecureRandom rand;
		LOGGER.debug("debut generate_random_bytes");
		rand = getSecureRandom();
		tab = new byte[size];
		rand.nextBytes(tab);
		LOGGER.debug("fin generate_random_bytes");
		return tab;
	}

	public static String toString(byte[] tab) {
		byte b;
		String res = "";
		LOGGER.debug("debut toString");
		if (tab != null && tab.length > 0) {
			for (int i = 0; i < tab.length; i++) {
				b = tab[i];
				if (i > 0)
					res += ",";
				res += b;
			}
		}
		LOGGER.debug("fin toString");
		return res;
	}

	public static byte[] lecture(Path p) throws IOException {
		LOGGER.debug("debut lecture");
		final byte[] bytes = Files.readAllBytes(p);
		LOGGER.debug("fin lecture");
		return bytes;
	}

	public static void ecriture(Path p, byte[] contenu) throws IOException {
		LOGGER.debug("debut ecriture");
		Files.write(p, contenu, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		LOGGER.debug("fin ecriture");
	}

	public static byte[] calcul_hash(byte[] contenu, String algo) throws GeneralSecurityException {
		MessageDigest hash;
		LOGGER.debug("debut calcul_hash");
		hash = MessageDigest.getInstance(algo, "BC");
		hash.update(contenu);
		final byte[] digest = hash.digest();
		LOGGER.debug("fin calcul_hash");
		return digest;
	}

	public static String convHexString(byte[] tab) {
		String result;
		LOGGER.debug("debut convHexString");
		result = BaseEncoding.base16().lowerCase().encode(tab);
		LOGGER.debug("fin convHexString");
		return result;
	}

	public static byte[] convHexByte(String s) {
		byte[] result;
		LOGGER.debug("debut convHexByte");
		result = BaseEncoding.base16().lowerCase().decode(s);
		LOGGER.debug("fin convHexByte");
		return result;
	}

	public static boolean egaux(byte[] tab1, byte[] tab2) {
		if (tab1 == null && tab2 == null) {
			return true;
		}
		if (tab1 == null || tab2 == null) {
			return false;
		}
		if (tab1.length != tab2.length) {
			return false;
		}
		for (int i = 0; i < tab1.length; i++) {
			if (tab1[i] != tab2[i]) {
				return false;
			}
		}
		return true;
	}

	public static boolean egaux(char[] tab1, char[] tab2) {
		if (tab1 == null && tab2 == null) {
			return true;
		}
		if (tab1 == null || tab2 == null) {
			return false;
		}
		if (tab1.length != tab2.length) {
			return false;
		}
		for (int i = 0; i < tab1.length; i++) {
			if (tab1[i] != tab2[i]) {
				return false;
			}
		}
		return true;
	}

	public static void ecriture(Path p, List<String> list) throws IOException {
		LOGGER.debug("debut ecriture");
		Files.write(p, list, Charset.forName("UTF-8"),
				StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		LOGGER.debug("fin ecriture");
	}

	public static Gson createGson() {
		LOGGER.debug("debut createGson");
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
		gsonBuilder.registerTypeAdapter(byte[].class, new ByteArraySerializer());
		gsonBuilder.registerTypeAdapter(byte[].class, new ByteArrayDeserializer());


		final Gson gson = gsonBuilder.create();
		LOGGER.debug("fin createGson");
		return gson;
	}

	public static Traitement createTraitement() throws IOException {
		if (false) {
			return new Traitement();
		} else {
			return new Traitement2();
		}
	}
}
