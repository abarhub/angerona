package org.abarhub.angerona.web.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

	public static String decode(String str){
		byte[] passwordByte = java.util.Base64.getDecoder().decode(str);
		return new String(passwordByte, StandardCharsets.UTF_8);
	}

	public static String encode(byte[] crypte) {
		return Base64.getEncoder().encodeToString(crypte);
	}
}
