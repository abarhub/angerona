package org.abarhub.angerona.coffrefort;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import org.abarhub.angerona.utils.Tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ToolsCoffreFort {


	public void save(CoffreFort coffreFort, Path fichier) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException {
		coffreFort = Preconditions.checkNotNull(coffreFort);
		fichier = Preconditions.checkNotNull(fichier);

		//Config config=new Config();

		//File f=config.getRep_data();
		try (OutputStream dest = Files.newOutputStream(fichier)) {
			BufferedOutputStream buff = new BufferedOutputStream(dest);

			try (ZipOutputStream out = new ZipOutputStream(buff)) {

				String filename = "message.crp";

				byte[] buf = null;
				if (coffreFort.getMessage() != null
						&& coffreFort.getMessage().getMessageCrypte() != null) {
					buf = coffreFort.getMessage().getMessageCrypte();
				}

				writeNextFile(out, filename, buf);

				filename = "param.json";
				buf = null;

				if(coffreFort.getConfig()!=null) {
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

				filename = "keystore.p12";
				buf = null;

				if(coffreFort.getKeystore()!=null) {

					ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
					coffreFort.getKeystore().store(outputStream, coffreFort.getKeystorePassword());

					buf=outputStream.toByteArray();
				}

				writeNextFile(out, filename, buf);
			}
		}
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

	public CoffreFort load(Path fichier) {
		return null;
	}

}
