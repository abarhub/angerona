package org.abarhub.angerona.coffrefort;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.abarhub.angerona.config.ConfigCrypt;
import org.abarhub.angerona.exception.CoffreFortException;
import org.abarhub.angerona.security.Cryptage2;
import org.abarhub.angerona.security.Traitement;
import org.abarhub.angerona.utils.Config;
import org.abarhub.angerona.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
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

	public void save(CoffreFort coffreFort, Path fichier) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		coffreFort = Preconditions.checkNotNull(coffreFort);
		fichier = Preconditions.checkNotNull(fichier);

		LOGGER.info("sauvegarde du keystore ...");

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
			}
		}

		LOGGER.info("sauvegarde du keystore OK");
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

	public CoffreFort load(Path fichier, char[] key) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, CoffreFortException {

		LOGGER.info("chargement du coffre fort ...");

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

//						if (entryName.equalsIgnoreCase(MESSAGE_FILENAME)) {
//							byte[] buf = readContenu(zis);
//							Message message = new Message();
//							message.setMessageCrypte(buf);
//							coffreFort.setMessage(message);
//						} else if (entryName.equalsIgnoreCase(PARAM_JSON_FILENAME)) {
//							Gson gson;
//							ConfigCrypt configCrypt;
//							byte[] buf = readContenu(zis);
//							gson = Tools.createGson();
//							configCrypt = gson.fromJson(new String(buf, StandardCharsets.UTF_8), ConfigCrypt.class);
//							coffreFort.setConfig(configCrypt);
//						} else if (entryName.equalsIgnoreCase(KEYSTORE_P12_FILENAME)) {
//							byte[] buf = readContenu(zis);
//							KeyStore keyStore = KeyStore.getInstance("PKCS12");
//							keyStore.load(new ByteArrayInputStream(buf), key);
//							coffreFort.setKeystore(keyStore);
//						} else {
//							LOGGER.info("Fichier {} inclu dans le zip ignore");
//						}
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
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
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
			final String debutFichier = "backupbis";
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

//			liste_fichier = new ArrayList<>();
//
//			liste_fichier.add(donne_fichier_data());
//			liste_fichier.add(donne_fichier_data_hash());
//			liste_fichier.add(KeyStoreFile());
//			liste_fichier.add(KeyStoreFileHash());
			buffer = new byte[512];
			try (OutputStream fos = Files.newOutputStream(fichier_backup)) {
				try (ZipOutputStream zos = new ZipOutputStream(fos)) {
					for (Path f2 : liste_fichier) {
						ZipEntry ze = new ZipEntry(f2.getFileName().toString());
						zos.putNextEntry(ze);
						try (InputStream in = Files.newInputStream(f2)) {
							int len;
							while ((len = in.read(buffer)) > 0) {
								zos.write(buffer, 0, len);
							}
						}
						zos.closeEntry();
					}
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

			LOGGER.error("convertion ...");

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
//			CoffreFort coffreFort=new CoffreFort();
//			Message message=new Message();
//			message.setMessage(contenu);
//			coffreFort.setMessage(message);
//			coffreFort.setKeystorePassword(password);
//
//			save();

			LOGGER.error("convertion OK");
		}
	}
}
