/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author abarret
 */
public class Config {

	final static Logger logger = LoggerFactory.getLogger(Config.class);
	private File rep_data;

	public Config() throws IOException {
		init();
	}

	private Properties load(String filename) throws IOException, FileNotFoundException {
		Properties properties = new Properties();
		logger.info("Chargement du fichier de config ...");
		FileInputStream input = new FileInputStream(filename);
		try {
			properties.load(input);
		} finally {
			input.close();
		}
		logger.info("Fin de chargement du fichier de config");
		return properties;
	}

	private void init() throws IOException {
		Properties p;
		String s;
		p = load("config.properties");
		if (p != null) {
			if (p.containsKey("dir")) {
				s = p.getProperty("dir");
				if (s != null && !s.trim().isEmpty()) {
					rep_data = new File(s);
				}
			}
		}
	}

	public File getRep_data() {
		return rep_data;
	}
}
