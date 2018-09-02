/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.security;

import org.abarhub.angerona.exception.KeyStoreHashException;
import org.abarhub.angerona.utils.Config;
import org.abarhub.angerona.utils.Resultat;
import org.abarhub.angerona.utils.Tools;
import org.apache.commons.codec.DecoderException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author abarret
 */
public class Cryptage {

	private final static Logger LOGGER = LoggerFactory.getLogger(Cryptage.class);

	private static final String CLEF_CRYPTAGE = "clef_cryptage";
	private static final String FormatString = "UTF-8";
	private static final String IV_CRYPTAGE = "iv_cryptage";
	private static final String KeyStoreFormat = "UBER";

	private String contenu;
	private KeyStore key_store;
	private Config config;

	public Cryptage(Config config) {
		if (Security.getProvider("BC") == null) {
			Security.addProvider(new BouncyCastleProvider());
		}
		if (config == null) {
			throw new IllegalArgumentException();
		}
		this.config = config;
		if (config.getRep_data() == null) {
			throw new IllegalArgumentException();
		}
	}

	public void lecture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException, DecoderException {
		File f;
		Cipher cipher;
		BufferedInputStream in = null;
		byte buf[];
		int len;
		String buf3;
		log("lecture data");
		f = donne_fichier_data();
		if (f == null || !f.exists()) {
			throw new FileNotFoundException();
		}
		buf = new byte[512];
		cipher = getBlockCipher(false, pwd);
		CipherInputStream in2;
		ByteArrayOutputStream buf2;

		try {
			in = new BufferedInputStream(new FileInputStream(f));
			in2 = new CipherInputStream(in, cipher);
			buf2 = new ByteArrayOutputStream();
			while ((len = in2.read(buf)) != -1) {
				buf2.write(buf, 0, len);
			}
			in2.close();
			buf3 = buf2.toString(FormatString);
			if (verifie(buf2.toByteArray(), true)) {
				contenu = buf3;
			} else {
				log("Erreur dans le fichier avec les hash");
				throw new IllegalArgumentException();
			}
		} finally {
			if (in != null)
				in.close();
			in = null;
		}
	}

	public void ecriture(char[] pwd) throws IOException, DataLengthException, InvalidCipherTextException, GeneralSecurityException {
		File f, f2;
		Cipher cipher;
		BufferedOutputStream out = null;
		backup();
		log("ecriture data");
		f = donne_fichier_data();
		cipher = getBlockCipher(true, pwd);
		CipherOutputStream out2 = null;
		byte texte[];
		texte = contenu.getBytes(FormatString);

		try {
			out = new BufferedOutputStream(new FileOutputStream(f));
			out2 = new CipherOutputStream(out, cipher);
			out2.write(texte);
		} finally {
			if (out2 != null) {
				out2.close();
			} else if (out != null) {
				out.close();
			}
		}
		f2 = donne_fichier_data_hash();//new File(f.getParent(),"hash.asc");
		enregistre_hash(texte, f2);

		//f2=new File(f.getParent(),"hash_sha512.asc");
		//enregistre_hash(texte,f2,"SHA-512");
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	private File KeyStoreFile() {
		if (config.getRep_data() != null) {
			return new File(config.getRep_data(), "keystore.bin");
		} else {
			return new File("keystore.bin");
		}
	}

	private File KeyStoreFileHash() {
		return new File(config.getRep_data(), "keystore.asc");
	}

	private File donne_fichier_data() {
		if (config.getRep_data() != null) {
			return new File(config.getRep_data(), "secret.crpt");
		} else {
			return new File("secret.crpt");
		}
	}

	private Cipher getBlockCipher(boolean cryptage, char[] pwd) throws GeneralSecurityException {
		if (key_store == null) {
			throw new IllegalArgumentException();
		}
		Cipher cipher = Cipher.getInstance("AES/CTR/PKCS7Padding", "BC");//new DESEngine();
		//BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
		//String keyString="ABCDEF";
		//byte[] key = keyString.getBytes();
		SecretKeySpec key;
		//char[] pwd="abc".toCharArray();
		//byte clef[]=new byte[]{30,62,-23,41,27,8,61,6,70,111,-109,-39,88,-7,48,0};
		byte ivBytes[] = new byte[]{56, -35, 13, 84, 17, 21, 90, 39, 32, 112, 115, 41, -63, 33, -92, 64};
		Key clef = key_store.getKey(CLEF_CRYPTAGE, pwd);
		//byte ivBytes[]=key_store.getKey(IV_CRYPTAGE, pwd);
		key = new SecretKeySpec(clef.getEncoded(), clef.getAlgorithm());
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		if (cryptage) {
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		} else {
			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		}
		return cipher;
	}

	public void init_keystore(char[] key) throws GeneralSecurityException, IOException {
		KeyStore store;
		Key key2;
		KeyGenerator generator;
		backup();
		log("init key");
		store = KeyStore.getInstance("UBER", "BC");
		store.load(null, null);

		generator = KeyGenerator.getInstance("AES", "BC");

		generator.init(128, Tools.getSecureRandom());

		key2 = generator.generateKey();
		//System.out.println("key="+Tools.toString(key2.getEncoded()));

		store.setKeyEntry(CLEF_CRYPTAGE, key2, key, null);
		//store.setKeyEntry(IV_CRYPTAGE, Tools.generate_random_bytes(30), null);

		FileOutputStream out;
		File f;

		f = KeyStoreFile();
		if (f.exists())
			f.delete();
		out = new FileOutputStream(f);

		store.store(out, key);

		enregistre_hash_keystore(f);
	}

	public void loadKeyStore(char[] key) throws GeneralSecurityException, IOException, DecoderException, KeyStoreHashException {
		verifie_hash_keystore();
		key_store = KeyStore.getInstance(KeyStoreFormat, "BC");
		key_store.load(new FileInputStream(KeyStoreFile()), key);
	}

	public Resultat verifie_password(char[] password) {
		Resultat res;
		res = new Resultat();
		try {
			KeyStore key_store0;
			if (password == null || password.length == 0) {
				res.addError("Mot de passe vide");
				return res;
			}
			key_store0 = KeyStore.getInstance(KeyStoreFormat, "BC");
			key_store0.load(new FileInputStream(KeyStoreFile()), password);
		} catch (GeneralSecurityException | IOException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			res.addError(ex.getLocalizedMessage());
		}
		return res;
	}

	public void log(String msg) {
        /*File f;
        Date d;
        f=new File(config.getRep_data(),"divers.log");
        if(!f.exists())
        {
            f.createNewFile();
        }
        try(FileWriter out=new FileWriter(f,true);PrintWriter out2=new PrintWriter(out))
        {
            d=new Date();
            out2.printf("%tY-%tm-%td %tl-%tM %tp %s\n",d,d,d,d,d,d ,msg);
        }*/
		LOGGER.info(msg);
	}

	private void enregistre_hash(byte[] texte, File f) throws IOException, GeneralSecurityException {
		byte[] buf;
		List<String> liste;
		liste = new ArrayList<>();
		for (TypeHash t : TypeHash.values()) {
			buf = Tools.calcul_hash(texte, t.getAlgo());
			liste.add(t.getNom() + "=" + Tools.convHexString(buf));
		}
		Tools.ecriture(f.toPath(), liste);
	}

	private void backup() {
		File fichier_backup, rep_backup;
		String nom_backup;
		int n;
		List<File> liste_fichier;
		byte buffer[];
		log("Backup...");
		rep_backup = new File("backup");
		if (!rep_backup.exists()) {
			rep_backup.mkdirs();
		}
		nom_backup = "backup.zip";
		fichier_backup = new File(rep_backup, nom_backup);
		if (fichier_backup.exists()) {
			n = 2;
			do {
				nom_backup = "backup" + n + ".zip";
				fichier_backup = new File(rep_backup, nom_backup);
				n++;
			} while (fichier_backup.exists());
		}
		log("Backup vers le fichier : " + fichier_backup.getAbsolutePath());
		liste_fichier = new ArrayList<>();
		liste_fichier.add(donne_fichier_data());
		liste_fichier.add(donne_fichier_data_hash());
		liste_fichier.add(KeyStoreFile());
		liste_fichier.add(KeyStoreFileHash());
		buffer = new byte[512];
		try {
			FileOutputStream fos = new FileOutputStream(fichier_backup);
			try (ZipOutputStream zos = new ZipOutputStream(fos)) {
				for (File f2 : liste_fichier) {
					ZipEntry ze = new ZipEntry(f2.getName());
					zos.putNextEntry(ze);
					try (FileInputStream in = new FileInputStream(f2.getAbsoluteFile())) {
						int len;
						while ((len = in.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
					}
					zos.closeEntry();
				}
			}

			log("Backup termine");

		} catch (IOException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		log("Fin de backup");
	}

	private boolean verifie(byte[] toByteArray, boolean data) throws IOException, DecoderException, GeneralSecurityException {
		Path p;
		File f;
		List<String> lignes;
		String s2;
		byte[] buf, buf2;
		TypeHash type_hash;
		if (data) {
			f = donne_fichier_data_hash();
		} else {
			f = KeyStoreFileHash();
		}
		if (f.exists()) {
			log("Vérification du fichier hash : " + f.getAbsolutePath());
			p = f.toPath();
			lignes = Files.readAllLines(p, Charset.forName(FormatString));
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
									return false;
								}
								buf = Tools.convHexByte(s2);
								buf2 = Tools.calcul_hash(toByteArray, type_hash.getAlgo());
								if (buf == null || buf.length == 0) {
									log("Vérification hash erreur : hash vide");
									return false;
								} else if (!Tools.egaux(buf, buf2)) {
									log("Vérification hash erreur : hash différent");
									return false;
								} else {
									log("Vérification hash " + type_hash + " : ok");
								}
							}
						}
					}
				}
			}
			log("Vérification hash Ok");
			return true;
		} else {
			log("Vérification hash erreur : pas présent");
			return false;
		}
	}

	private File donne_fichier_data_hash() {
		return new File(config.getRep_data(), "hash.asc");
	}

	private void enregistre_hash_keystore(File f) throws IOException, GeneralSecurityException {
		byte[] buf;
		File f2;
		buf = Files.readAllBytes(f.toPath());
		f2 = KeyStoreFileHash();
		enregistre_hash(buf, f2);
	}

	private void verifie_hash_keystore() throws IOException, DecoderException, GeneralSecurityException, KeyStoreHashException {
		File f;
		byte[] buf;
		f = KeyStoreFile();
		buf = Tools.lecture(f.toPath());
		if (!verifie(buf, false)) {
			throw new KeyStoreHashException();
		}
	}
}