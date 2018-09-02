package org.abarhub.angerona.security;

import org.abarhub.angerona.exception.CoffreFortException;
import org.abarhub.angerona.exception.KeyStoreHashException;
import org.abarhub.angerona.utils.Resultat;
import org.apache.commons.codec.DecoderException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface ICryptage {
	void lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, DecoderException, CoffreFortException;

	void ecriture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException;

	String getContenu();

	void setContenu(String contenu);

	void init_keystore(char[] key) throws GeneralSecurityException, IOException;

	void loadKeyStore(char[] key) throws GeneralSecurityException, IOException, DecoderException, KeyStoreHashException, CoffreFortException;

	Resultat verifie_password(char[] password);

}
