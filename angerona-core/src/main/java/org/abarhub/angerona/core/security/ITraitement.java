package org.abarhub.angerona.core.security;

import org.abarhub.angerona.core.exception.CoffreFortException;
import org.abarhub.angerona.core.exception.KeyStoreHashException;
import org.abarhub.angerona.core.utils.Resultat;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface ITraitement {
	void enregistre(String s, char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, CoffreFortException;

	void enregistre_changement_clef(String s, char new_password[]) throws GeneralSecurityException, IOException, DataLengthException, InvalidCipherTextException, KeyStoreHashException, CoffreFortException;

	String lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, CoffreFortException;

	void initialise_keystore(char[] key) throws GeneralSecurityException, IOException;

	void load_keystore(char[] key) throws GeneralSecurityException, IOException, KeyStoreHashException, CoffreFortException;

	Resultat verifie_password(char[] password);
}
