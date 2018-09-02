/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.utils;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.abarhub.angerona.json.ByteArrayDeserializer;
import org.abarhub.angerona.json.ByteArraySerializer;
import org.abarhub.angerona.json.LocalDateTimeDeserializer;
import org.abarhub.angerona.json.LocalDateTimeSerializer;
import org.abarhub.angerona.security.Traitement;
import org.abarhub.angerona.security.Traitement2;

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

	public static SecureRandom getSecureRandom() {
		SecureRandom rand;
		int nb = 16;
		byte b, tab[];
		rand = new SecureRandom();
		tab = rand.generateSeed(100);
		rand = new SecureRandom(tab);
		return rand;
	}

	public static byte[] generate_random_bytes(int size) {
		byte tab[];
		SecureRandom rand;
		rand = getSecureRandom();
		tab = new byte[size];
		rand.nextBytes(tab);
		return tab;
	}

	public static String toString(byte[] tab) {
		byte b;
		String res = "";
		if (tab != null && tab.length > 0) {
			for (int i = 0; i < tab.length; i++) {
				b = tab[i];
				if (i > 0)
					res += ",";
				res += b;
			}
		}
		return res;
	}

	public static byte[] lecture(Path p) throws IOException {
		return Files.readAllBytes(p);
	}

	public static void ecriture(Path p, byte[] contenu) throws IOException {
		Files.write(p, contenu, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
	}

	public static byte[] calcul_hash(byte[] contenu, String algo) throws GeneralSecurityException {
		MessageDigest hash;
		hash = MessageDigest.getInstance(algo, "BC");
		hash.update(contenu);
		return hash.digest();
	}

	public static String convHexString(byte[] tab) {
		String result;
		result = BaseEncoding.base16().lowerCase().encode(tab);
		return result;
	}

	public static byte[] convHexByte(String s) {
		byte[] result;
		result = BaseEncoding.base16().lowerCase().decode(s);
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
		Files.write(p, list, Charset.forName("UTF-8"),
				StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
	}

	public static Gson createGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
		gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
		gsonBuilder.registerTypeAdapter(byte[].class, new ByteArraySerializer());
		gsonBuilder.registerTypeAdapter(byte[].class, new ByteArrayDeserializer());


		return gsonBuilder.create();
	}

	public static Traitement createTraitement() throws IOException {
		if (false) {
			return new Traitement();
		} else {
			return new Traitement2();
		}
	}
}
