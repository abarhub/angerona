package org.abarhub.angerona.core.security;

import org.abarhub.angerona.core.utils.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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

	@Override
	protected void init(char[] password) {
		try {
			LOGGER.debug("debut init");
			crypt.loadKeyStore(password);
			LOGGER.debug("fin init");
		} catch (Exception e) {
			LOGGER.error("Erreur", e);
			throw new RuntimeException(e);
		}

	}

}
