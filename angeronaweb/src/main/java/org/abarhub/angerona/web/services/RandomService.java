package org.abarhub.angerona.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;

@Service
public class RandomService {

	private static Logger LOGGER = LoggerFactory.getLogger(RandomService.class);

	private SecureRandom randomSecureRandom;

	@PostConstruct
	public void init() {
		LOGGER.debug("RandomService.init ...");
		randomSecureRandom = new SecureRandom();
		byte[] seed = new byte[30];
		randomSecureRandom.nextBytes(seed);

		randomSecureRandom = new SecureRandom(seed);
		LOGGER.debug("RandomService.init ok");
	}

	public void nextBytes(byte[] buf) {
		LOGGER.debug("RandomService.nextBytes ...");
		if (buf == null || buf.length == 0) {
			throw new IllegalArgumentException("Le parametre est nul ou vide");
		}
		randomSecureRandom.nextBytes(buf);
		LOGGER.debug("RandomService.nextBytes ok");
	}

}
