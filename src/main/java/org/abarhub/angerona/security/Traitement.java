/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.security;

import org.abarhub.angerona.exception.CoffreFortException;
import org.abarhub.angerona.exception.KeyStoreHashException;
import org.abarhub.angerona.utils.Config;
import org.abarhub.angerona.utils.Resultat;
import org.apache.commons.codec.DecoderException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author abarret
 */
public class Traitement implements ITraitement {

	private final static Logger LOGGER = LoggerFactory.getLogger(Traitement.class);

	protected ICryptage crypt;
	protected Config config;

	public Traitement() throws IOException {
		config = new Config();
		crypt = getCrypt();
	}

	protected ICryptage getCrypt() throws IOException {
		return new Cryptage(config);
	}

	@Override
	public void enregistre(String s, char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException {
		log("enregistrement");
		init(pwd);
		crypt.setContenu(s);
		//System.out.println("Ecriture de :"+s);
		crypt.ecriture(pwd);
		//System.out.println("Ecriture terminé");
		log("enregistrement ok");
	}

	@Override
	public void enregistre_changement_clef(String s, char new_password[]) throws GeneralSecurityException, IOException, DataLengthException, InvalidCipherTextException, DecoderException, KeyStoreHashException, CoffreFortException {

		log("debut change clef ...");
		init(new_password);
		crypt.setContenu(s);
		//System.out.println("changement de clef");
		crypt.init_keystore(new_password);
		crypt.loadKeyStore(new_password);
		//System.out.println("Ecriture de :"+s);
		crypt.ecriture(new_password);
		//System.out.println("Ecriture terminé");
		log("fin change clef");
	}

	@Override
	public String lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, DecoderException, CoffreFortException {
		String s;

		log("lecture");
		init(pwd);
		crypt.lecture(pwd);
		s = crypt.getContenu();
		log("lecture ok");
		return s;
	}

	@Override
	public void initialise_keystore(char[] key) throws GeneralSecurityException, IOException {
		init(key);
		crypt.init_keystore(key);
	}

	@Override
	public void load_keystore(char[] key) throws GeneralSecurityException, IOException, DecoderException, KeyStoreHashException, CoffreFortException {
		init(key);
		crypt.loadKeyStore(key);
	}

	@Override
	public Resultat verifie_password(char[] password) {
		init(password);
		return crypt.verifie_password(password);
	}

	private void log(String msg) throws IOException {
		LOGGER.info(msg);
	}

	protected void init(char[] password)  {

	}
}
