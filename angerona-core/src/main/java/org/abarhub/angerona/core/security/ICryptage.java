package org.abarhub.angerona.core.security;

import org.abarhub.angerona.core.exception.CoffreFortException;
import org.abarhub.angerona.core.exception.KeyStoreHashException;
import org.abarhub.angerona.core.utils.Resultat;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface ICryptage {
	void lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, CoffreFortException;

	void ecriture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, CoffreFortException;

	String getContenu();

	void setContenu(String contenu);

	void init_keystore(char[] key) throws GeneralSecurityException, IOException;

	void loadKeyStore(char[] key) throws GeneralSecurityException, IOException, KeyStoreHashException, CoffreFortException;

	Resultat verifie_password(char[] password);

}
