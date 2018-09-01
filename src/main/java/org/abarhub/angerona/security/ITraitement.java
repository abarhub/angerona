package org.abarhub.angerona.security;

import org.abarhub.angerona.exception.KeyStoreHashException;
import org.abarhub.angerona.utils.Resultat;
import org.apache.commons.codec.DecoderException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface ITraitement {
	void enregistre(String s, char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException;

	void enregistre_changement_clef(String s, char new_password[]) throws GeneralSecurityException, IOException, DataLengthException, InvalidCipherTextException, DecoderException, KeyStoreHashException;

	String lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, DecoderException;

	void initialise_keystore(char[] key) throws GeneralSecurityException, IOException;

	void load_keystore(char[] key) throws GeneralSecurityException, IOException, DecoderException, KeyStoreHashException;

	Resultat verifie_password(char[] password);
}
