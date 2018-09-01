/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.security;

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
public class Traitement {

	private final static Logger LOGGER = LoggerFactory.getLogger(Traitement.class);

	private Cryptage crypt;
	private Config config;

	public Traitement() throws IOException {
		config = new Config();
		crypt = new Cryptage(config);
	}

	public void enregistre(String s, char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException {
		log("enregistrement");
		crypt.setContenu(s);
		//System.out.println("Ecriture de :"+s);
		crypt.ecriture(pwd);
		//System.out.println("Ecriture terminé");
	}

	public void enregistre_changement_clef(String s, char new_password[]) throws GeneralSecurityException, IOException, DataLengthException, InvalidCipherTextException, DecoderException, KeyStoreHashException {

		log("debut change clef ...");
		crypt.setContenu(s);
		//System.out.println("changement de clef");
		crypt.init_keystore(new_password);
		crypt.loadKeyStore(new_password);
		//System.out.println("Ecriture de :"+s);
		crypt.ecriture(new_password);
		//System.out.println("Ecriture terminé");
		log("fin change clef");
	}

	public String lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, DecoderException {
		String s;

		log("lecture");
		crypt.lecture(pwd);
		s = crypt.getContenu();
		return s;
	}

	public void initialise_keystore(char[] key) throws GeneralSecurityException, IOException {
		crypt.init_keystore(key);
	}

	public void load_keystore(char[] key) throws GeneralSecurityException, IOException, DecoderException, KeyStoreHashException {
		crypt.loadKeyStore(key);
	}

	public Resultat verifie_password(char[] password) {
		return crypt.verifie_password(password);
	}

	public void log(String msg) throws IOException {
		LOGGER.info(msg);
	}
}
