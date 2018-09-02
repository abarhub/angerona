package org.abarhub.angerona.coffrefort;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.abarhub.angerona.config.ConfigCrypt;
import org.abarhub.angerona.exception.CoffreFortException;
import org.abarhub.angerona.security.Cryptage2;
import org.abarhub.angerona.security.Traitement;
import org.abarhub.angerona.security.TypeHash;
import org.abarhub.angerona.utils.Config;
import org.abarhub.angerona.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ToolsCoffreFort {

	private final static Logger LOGGER = LoggerFactory.getLogger(ToolsCoffreFort.class);


	private static final String MESSAGE_FILENAME = "message.crp";
	private static final String PARAM_JSON_FILENAME = "param.json";
	private static final String KEYSTORE_P12_FILENAME = "keystore.p12";

	public void save(CoffreFort coffreFort, Path fichier) throws IOException, GeneralSecurityException, CoffreFortException {
		coffreFort = Preconditions.checkNotNull(coffreFort);
		fichier = Preconditions.checkNotNull(fichier);
		Preconditions.checkNotNull(coffreFort.getKeystorePassword());

		LOGGER.info("sauvegarde du coffre ...");

		//Config config=new Config();

		//File f=config.getRep_data();
		try (OutputStream dest = Files.newOutputStream(fichier)) {
			BufferedOutputStream buff = new BufferedOutputStream(dest);

			try (ZipOutputStream out = new ZipOutputStream(buff)) {

				String filename = MESSAGE_FILENAME;

				byte[] buf = null;
				if (coffreFort.getMessage() != null
						&& coffreFort.getMessage().getMessageCrypte() != null) {
					buf = coffreFort.getMessage().getMessageCrypte();
				}

				writeNextFile(out, filename, buf);

				filename = PARAM_JSON_FILENAME;
				buf = null;

				if (coffreFort.getConfig() != null) {
					Gson gson;
					//gson = gsonBuilder.create();
					gson = Tools.createGson();
					//gson = new Gson();
					String json = gson.toJson(coffreFort.getConfig());
					if (json != null && !json.isEmpty()) {
						buf = json.getBytes(StandardCharsets.UTF_8);
					}
				}

				writeNextFile(out, filename, buf);

				filename = KEYSTORE_P12_FILENAME;
				buf = null;

				if (coffreFort.getKeystore() != null) {

					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					coffreFort.getKeystore().store(outputStream, coffreFort.getKeystorePassword());

					buf = outputStream.toByteArray();
				}

				writeNextFile(out, filename, buf);

				out.finish();
				out.flush();
			}

			dest.flush();
		}

		LOGGER.info("enregistrement du hash ...");

		byte[] buf = Files.readAllBytes(fichier);

		Path p = getPathHash(fichier);

		enregistreHash(buf, p);

		try {
			verifieHash(fichier);
		} catch (CoffreFortException e) {
			LOGGER.error("Erreur pour la verification du hash", e);
			throw new CoffreFortException("Erreur pour la verification du hash", e);
		}

		LOGGER.info("enregistrement du hash OK");

		LOGGER.info("sauvegarde du coffre OK");
	}

	private Path getPathHash(Path fichier) {
		Path p;
		String filename = fichier.getFileName().toString();
		int i = filename.lastIndexOf('.');
		if (i > 0) {
			filename = filename.substring(0, i) + ".asc";
		}
		p = fichier.resolve("../" + filename).normalize();
		return p;
	}

	private void writeNextFile(ZipOutputStream out, String filename, byte[] buf) throws IOException {
		ZipEntry entry = new ZipEntry(filename);

		out.putNextEntry(entry);

		try {
			if (buf != null && buf.length > 0) {
				out.write(buf);
			}
		} finally {
			out.closeEntry();
		}
	}

	private void enregistreHash(byte[] texte, Path f) throws IOException, GeneralSecurityException {
		byte[] buf;
		List<String> liste;
		liste = new ArrayList<>();
		for (TypeHash t : TypeHash.values()) {
			buf = Tools.calcul_hash(texte, t.getAlgo());
			liste.add(t.getNom() + "=" + Tools.convHexString(buf));
		}
		Files.write(f, liste);
	}

	public CoffreFort load(Path fichier, char[] key) throws IOException, GeneralSecurityException,
			CoffreFortException {

		LOGGER.info("chargement du coffre fort ...");

		verifieHash(fichier);

		CoffreFort coffreFort = new CoffreFort();
		coffreFort.setKeystorePassword(key);

		Map<String, byte[]> contenuZip = new HashMap<>();

		try (InputStream fis = Files.newInputStream(fichier)) {
			BufferedInputStream buffi = new BufferedInputStream(fis);

			try (ZipInputStream zis = new ZipInputStream(buffi)) {

				ZipEntry entry;
				while ((entry = zis.getNextEntry()) != null) {

					String entryName = entry.getName();

					if (entryName != null) {

						byte[] buf = readContenu(zis);

						contenuZip.put(entryName, buf);

					} else {
						LOGGER.error("Le zip contient une entrée null");
					}

				}
			}
		}

		if (contenuZip.isEmpty()) {
			LOGGER.error("Le coffre fort est vide");
			throw new CoffreFortException("Le coffre fort est vide");
		}

		if (contenuZip.containsKey(PARAM_JSON_FILENAME)) {
			Gson gson;
			ConfigCrypt configCrypt;
			byte[] buf = contenuZip.get(PARAM_JSON_FILENAME);
			gson = Tools.createGson();
			configCrypt = gson.fromJson(new String(buf, StandardCharsets.UTF_8), ConfigCrypt.class);
			coffreFort.setConfig(configCrypt);
		} else {
			LOGGER.error("Le coffre fort ne contient pas le fichier {}", PARAM_JSON_FILENAME);
			throw new CoffreFortException("Le coffre fort ne contient pas le fichier " + PARAM_JSON_FILENAME);
		}

		if (contenuZip.containsKey(KEYSTORE_P12_FILENAME)) {
			byte[] buf = contenuZip.get(KEYSTORE_P12_FILENAME);
			KeyStore keyStore = KeyStore.getInstance(coffreFort.getConfig().getKeystoreAlgo());
			keyStore.load(new ByteArrayInputStream(buf), key);
			coffreFort.setKeystore(keyStore);
		} else {
			LOGGER.error("Le coffre fort ne contient pas le fichier {}", KEYSTORE_P12_FILENAME);
			throw new CoffreFortException("Le coffre fort ne contient pas le fichier " + KEYSTORE_P12_FILENAME);
		}

		if (contenuZip.containsKey(MESSAGE_FILENAME)) {
			byte[] buf = contenuZip.get(MESSAGE_FILENAME);
			Message message = new Message();
			message.setMessageCrypte(buf);
			coffreFort.setMessage(message);
		} else {
			LOGGER.error("Le coffre fort ne contient pas le fichier {}", MESSAGE_FILENAME);
			throw new CoffreFortException("Le coffre fort ne contient pas le fichier " + MESSAGE_FILENAME);
		}

		LOGGER.info("chargement du coffre fort OK");

		return coffreFort;
	}

	private void verifieHash(Path fichier) throws IOException, CoffreFortException {
		byte[] buf2 = Files.readAllBytes(fichier);

		Path fichierHash = getPathHash(fichier);

		try {
			if (!Files.exists(fichierHash)) {
				LOGGER.error("Le hash du coffre fort est absent");
			} else if (!verifie(fichierHash, buf2)) {
				LOGGER.error("Le hash du coffre fort est invalide");
				throw new CoffreFortException("Le hash du coffre fort est invalide");
			}
		} catch (Exception e) {
			LOGGER.error("Erreur pour calculer le hash du coffre fort", e);
			throw new CoffreFortException("Erreur pour calculer le hash du coffre fort", e);
		}
	}


	private boolean verifie(Path fichierHash, byte[] toByteArray) throws IOException, GeneralSecurityException {
		List<String> lignes;
		if (Files.exists(fichierHash)) {
			LOGGER.info("Vérification du fichier hash : {}", fichierHash);
			lignes = Files.readAllLines(fichierHash, StandardCharsets.UTF_8);
			if (!verifyHash(toByteArray, lignes)) {
				LOGGER.info("Vérification hash Ok");
				return true;
			} else {
				LOGGER.info("Vérification hash KO");
				return false;
			}
		} else {
			LOGGER.info("Vérification hash erreur : pas présent");
			return false;
		}
	}


	protected boolean verifyHash(byte[] toByteArray, List<String> lignes) throws IOException, GeneralSecurityException {
		TypeHash type_hash;
		String s2;
		byte[] buf;
		byte[] buf2;
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
								LOGGER.info("Vérification hash erreur : hash vide");
								return true;
							}
							buf = Tools.convHexByte(s2);
							buf2 = Tools.calcul_hash(toByteArray, type_hash.getAlgo());
							if (buf == null || buf.length == 0) {
								LOGGER.info("Vérification hash erreur : hash vide");
								return true;
							} else if (!Tools.egaux(buf, buf2)) {
								LOGGER.info("Vérification hash erreur : hash différent");
								return true;
							} else {
								LOGGER.info("Vérification hash " + type_hash + " : ok");
							}
						}
					}
				}
			}
		}
		return false;
	}


	private byte[] readContenu(ZipInputStream zis) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int count;
		byte[] data = new byte[100];
		while ((count = zis.read(data)) != -1) {
			outputStream.write(data, 0, count);
		}

		return outputStream.toByteArray();
	}

	public void backup() {
		Path fichier_backup, rep_backup;
		String nom_backup;
		int n;
		List<Path> liste_fichier;
		byte buffer[];
		LOGGER.info("Backup...");
		try {
			rep_backup = Paths.get("backup");
			if (!Files.exists(rep_backup)) {
				Files.createDirectories(rep_backup);
			}
			final String debutFichier = "backup_v2_";
			nom_backup = debutFichier + ".zip";
			fichier_backup = rep_backup.resolve(nom_backup);
			if (Files.exists(fichier_backup)) {
				n = 2;
				do {
					nom_backup = debutFichier + n + ".zip";
					fichier_backup = rep_backup.resolve(nom_backup);
					n++;
				} while (Files.exists(fichier_backup));
			}
			LOGGER.info("Backup vers le fichier : {}", fichier_backup);

			try (Stream<Path> paths = Files.walk(Paths.get("data"))) {
				liste_fichier = paths.filter(Files::isRegularFile).collect(Collectors.toList());
			}

			List<Path> fichierASupprimer = new ArrayList<>();

			buffer = new byte[512];
			try (OutputStream fos = Files.newOutputStream(fichier_backup)) {
				try (ZipOutputStream zos = new ZipOutputStream(fos)) {
					for (Path f2 : liste_fichier) {
						String filename = f2.getFileName().toString();
						ZipEntry ze = new ZipEntry(filename);
						zos.putNextEntry(ze);
						try (InputStream in = Files.newInputStream(f2)) {
							int len;
							while ((len = in.read(buffer)) > 0) {
								zos.write(buffer, 0, len);
							}
						}
						zos.closeEntry();

						if (filename.endsWith("coffrefort.zip")
								|| filename.endsWith("coffrefort.asc")) {
							// on ne supprime pas ce fichier
						} else if (filename.endsWith(".p12")
								|| filename.endsWith(".json")
								|| filename.endsWith(".zip")
								|| filename.endsWith(".asc")
								|| filename.endsWith(".bin")
								|| filename.endsWith(".crpt")
								|| filename.endsWith(".7z")
								|| filename.endsWith(".bin")) {
							fichierASupprimer.add(f2);
						}
					}
				}
			}

			if (!fichierASupprimer.isEmpty()) {
				LOGGER.info("suppression des fichiers");

				for (Path p : fichierASupprimer) {
					Files.delete(p);
				}
			}

			LOGGER.info("Backup termine");

		} catch (IOException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		}
		LOGGER.info("Fin de backup");
	}

	public void convertion(char[] password) throws CoffreFortException {
		Path coffreFortPath = Paths.get("data/coffrefort.zip");
		if (!Files.exists(coffreFortPath)) {

			String contenu = null;

			LOGGER.info("convertion ...");

			try {
				Traitement tr = new Traitement();
				tr.load_keystore(password);
				contenu = tr.lecture(password);
			} catch (Exception e) {
				LOGGER.error("Erreur pour lire le contenu du fichier", e);
				throw new CoffreFortException("Erreur pour convertir le fichier");
			}
			if (contenu == null) {
				LOGGER.error("Le contenu du fichier");
				throw new CoffreFortException("Erreur pour convertir le fichier");
			}

			try {
				Cryptage2 cryptage2 = new Cryptage2(new Config());

				cryptage2.init_keystore(password);
				cryptage2.setContenu(contenu);

				cryptage2.ecriture(password);

			} catch (Exception e) {
				LOGGER.error("Erreur pour enregistrer le coffre fort", e);
				throw new CoffreFortException("Erreur pour enregistrer le coffre fort");
			}

			LOGGER.info("convertion OK");

		} else {
			if (existeAnciensFichiers()) {

				LOGGER.info("backup des fichiers de l'ancien format ...");

				ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
				toolsCoffreFort.backup();

				LOGGER.info("backup des fichiers de l'ancien format ok");
			}
		}
	}

	private boolean existeAnciensFichiers() {
		Path fichierAncienFormat = Paths.get("data/keystore.bin");
		if (Files.exists(fichierAncienFormat)) {
			return true;
		}

		fichierAncienFormat = Paths.get("data/keystore.p12");
		if (Files.exists(fichierAncienFormat)) {
			return true;
		}

		return false;
	}
}
