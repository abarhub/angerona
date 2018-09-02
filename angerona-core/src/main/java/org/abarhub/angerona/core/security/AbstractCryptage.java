package org.abarhub.angerona.core.security;

import org.abarhub.angerona.core.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public abstract class AbstractCryptage {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractCryptage.class);

	protected boolean verifyHash(byte[] toByteArray, List<String> lignes) throws IOException, GeneralSecurityException {
		TypeHash type_hash;
		String s2;
		byte[] buf;
		byte[] buf2;
		if (lignes != null && !lignes.isEmpty()) {
			for (String s : lignes) {
				if (s != null && !s.isEmpty()) {
					s = s.trim();
					if (s != null && s.length() > 0 && s.contains("=")) {
						type_hash = null;
						for (TypeHash t : TypeHash.values()) {
							if (s.startsWith(t.getNom() + "=")) {
								type_hash = t;
								break;
							}
						}
						if (type_hash != null) {
							s2 = s.substring((type_hash.getNom() + "=").length());
							if (s2.isEmpty()) {
								log("Vérification hash erreur : hash vide");
								return true;
							}
							buf = Tools.convHexByte(s2);
							buf2 = Tools.calcul_hash(toByteArray, type_hash.getAlgo());
							if (buf == null || buf.length == 0) {
								log("Vérification hash erreur : hash vide");
								return true;
							} else if (!Tools.egaux(buf, buf2)) {
								log("Vérification hash erreur : hash différent");
								return true;
							} else {
								log("Vérification hash " + type_hash + " : ok");
							}
						}
					}
				}
			}
		}
		return false;
	}

	private void log(String message) {
		LOGGER.info(message);
	}
}
