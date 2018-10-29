package org.abarhub.angerona.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Util {

	private static Logger LOGGER = LoggerFactory.getLogger(Base64Util.class);

	public static String decode(String str) {
		LOGGER.debug("Base64Util.decode ...");
		byte[] passwordByte = java.util.Base64.getDecoder().decode(str);
		String res = new String(passwordByte, StandardCharsets.UTF_8);
		LOGGER.debug("Base64Util.decode ok");
		return res;
	}

	public static String encode(byte[] crypte) {
		LOGGER.debug("Base64Util.encode ...");
		String res = Base64.getEncoder().encodeToString(crypte);
		LOGGER.debug("Base64Util.encode ok");
		return res;
	}
}
