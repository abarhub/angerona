package org.abarhub.angerona.web.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;

@Service
public class RandomService {

	private SecureRandom randomSecureRandom;

	@PostConstruct
	public void init() {
		randomSecureRandom = new SecureRandom();
		byte[] seed = new byte[30];
		randomSecureRandom.nextBytes(seed);

		randomSecureRandom = new SecureRandom(seed);
	}

	public void nextBytes(byte[] buf) {
		if (buf == null || buf.length == 0) {
			throw new IllegalArgumentException("Le parametre est nul ou vide");
		}
		randomSecureRandom.nextBytes(buf);
	}

}
