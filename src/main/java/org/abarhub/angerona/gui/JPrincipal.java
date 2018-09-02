/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.gui;

import com.google.gson.Gson;
import org.abarhub.angerona.coffrefort.ToolsCoffreFort;
import org.abarhub.angerona.config.ConfigCrypt;
import org.abarhub.angerona.config.ConfigFactory;
import org.abarhub.angerona.exception.CoffreFortException;
import org.abarhub.angerona.security.Traitement;
import org.abarhub.angerona.security.Traitement2;
import org.abarhub.angerona.utils.Tools;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author abarret
 */
public class JPrincipal extends javax.swing.JFrame implements WindowListener {

	final static Logger LOGGER = LoggerFactory.getLogger(JPrincipal.class);

	private char password[];
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton jButton5;
	private javax.swing.JButton jButton6;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextField jTextField1;

	/**
	 * Creates new form JPrincipal
	 */
	public JPrincipal() {
		initComponents();
	}

	public JPrincipal(char password[]) {
		initComponents();
		this.password = password;
		if (!lecture_fichier()) {
			LOGGER.error("Erreur pour décrypter les fichiers au démarrage");
			erreur("Erreur pour accéder aux fichiers");
			System.exit(1);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jButton1 = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		jButton5 = new javax.swing.JButton();
		jTextField1 = new javax.swing.JTextField();
		jButton6 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jButton1.setText("Quitter");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jScrollPane1.setViewportView(jTextArea1);

		jButton2.setText("Sauver");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jButton3.setText("Réinitialise");
		jButton3.setEnabled(false);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		jButton4.setText("Sauve / change clef");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		jButton5.setText("<");
		jButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton5ActionPerformed(evt);
			}
		});

		jButton6.setText(">");
		jButton6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton6ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButton5)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButton6)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButton4)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButton3)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButton2)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(jButton1))
										.addComponent(jScrollPane1))
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jButton1)
										.addComponent(jButton2)
										.addComponent(jButton3)
										.addComponent(jButton4)
										.addComponent(jButton5)
										.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jButton6))
								.addContainerGap())
		);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		sortie();
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		Traitement tr;
		String s, s2;
		s = jTextArea1.getText();
		try {
			tr = Tools.createTraitement();

			tr.load_keystore(password);

			tr.enregistre(s, password);

			s2 = tr.lecture(password);
			//System.out.println("s="+s+"!");
			//System.out.println("s2="+s2+"!");
			if (!s.equals(s2)) {
				erreur("Erreur pour relire le fichier !");
			} else {
				//enregistre2(s, password);
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			erreur("Erreur:" + ex.getLocalizedMessage());
		}
	}//GEN-LAST:event_jButton2ActionPerformed

//	private void enregistre2(String s, char[] password) throws IOException, GeneralSecurityException, InvalidCipherTextException, CoffreFortException {
//		Traitement2 traitement2 = new Traitement2();
//		traitement2.enregistre(s, password);
//	}
//
//	private void enregistre20(String s, char[] password) {
//		try {
//			Path fichier = Paths.get("C:\\projet\\angerona\\data/keystore.p12");
//
//			LOGGER.info("Enregistrement de {} ...", fichier);
//			KeyStore keyStore = KeyStore.getInstance("PKCS12");
//			keyStore.load(null, null); // Initialize a blank keystore
//			Random random = Tools.getSecureRandom();
//			byte[] val = new byte[32];
//			random.nextBytes(val);
//			SecretKey key = new SecretKeySpec(val, "AES");
//			//char[] password = "changeit".toCharArray();
//			byte[] salt = new byte[20];
//			random.nextBytes(salt);
//			keyStore.setEntry("clef_cryptage", new KeyStore.SecretKeyEntry(key),
//					new KeyStore.PasswordProtection(password,
//							"PBEWithHmacSHA512AndAES_128",
//							new PBEParameterSpec(salt, 100_000)));
//			//keyStore.store(Files.newOutputStream(fichier,StandardOpenOption.CREATE_NEW,
//			//		StandardOpenOption.TRUNCATE_EXISTING), password);
//			keyStore.store(new FileOutputStream(fichier.toFile()), password);
//
//			LOGGER.info("Enregistrement de {} OK", fichier);
//
//			LOGGER.info("Fichier {} existe : {}", fichier, Files.exists(fichier));
//
//			ConfigCrypt configCrypt;// = new ConfigCrypt();
////			configCrypt.setDateCreation(new Date());
////			configCrypt.setKeystoreAlgo("PKCS12");
////			KeyCrypt keyCrypt = new KeyCrypt();
////			keyCrypt.setSecretKeyCryptage("AES");
////			keyCrypt.setSecretKeyEntry("clef_cryptage");
////			keyCrypt.setProtectionAlgo("PBEWithHmacSHA512AndAES_128");
////			keyCrypt.setProtectionIteration(100_000);
////			keyCrypt.setKeyIv(salt);
////			configCrypt.setKeyCrypt(keyCrypt);
//			configCrypt = ConfigFactory.createNewConfigCrypt();
//			configCrypt.getKeyCrypt().setKeyIv(salt);
//
//			//GsonBuilder gsonBuilder = new GsonBuilder();
//			//gsonBuilder.registerTypeAdapter(Date.class, new LocalDateTimeSerializer());
//			//gsonBuilder.registerTypeAdapter(byte[].class, new ByteArraySerializer());
//
//			Gson gson;
//			//gson = gsonBuilder.create();
//			gson = Tools.createGson();
//			//gson = new Gson();
//			String json = gson.toJson(configCrypt);
//
//			Files.write(fichier.getParent().resolve("param.json"), json.getBytes(StandardCharsets.UTF_8));
//
//		} catch (Exception e) {
//			LOGGER.error(e.getLocalizedMessage(), e);
//		}
//	}

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		Traitement tr;
		try {
			tr = Tools.createTraitement();
			tr.initialise_keystore(password);
		} catch (GeneralSecurityException | IOException ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			erreur("Erreur:" + ex.getLocalizedMessage());
		} finally {

		}
	}//GEN-LAST:event_jButton3ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		Traitement tr;
		String s, s2;
		char pwd[];
		s = jTextArea1.getText();

		pwd = new_password();
		if (pwd != null && pwd.length > 0) {
			try {
				tr = Tools.createTraitement();

				tr.enregistre_changement_clef(s, pwd);

                /*tr.load_keystore(password);

                tr.enregistre(s);*/

				s2 = tr.lecture(pwd);
				//System.out.println("s="+s+"!");
				//System.out.println("s2="+s2+"!");
				if (s.equals(s2)) {
					password = pwd;
				} else {
					erreur("Impossible de lire le fichier !");
				}
			} catch (Exception ex) {
				LOGGER.error(ex.getLocalizedMessage(), ex);
				erreur("Erreur:" + ex.getLocalizedMessage());
			}
		}
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
		recherche(false);
	}//GEN-LAST:event_jButton5ActionPerformed

	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
		recherche(true);
	}//GEN-LAST:event_jButton6ActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public void main(String args[]) {
		if (2 == 3 - 1) {
			throw new IllegalArgumentException();
		}
		/*
		 * Set the Nimbus look and feel
		 */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         * /
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        /*java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new JPrincipal().setVisible(true);
            }
        });*/
	}
	// End of variables declaration//GEN-END:variables

	private boolean lecture_fichier() {
		Traitement tr;
		String s;
		try {

			ToolsCoffreFort toolsCoffreFort = new ToolsCoffreFort();
			toolsCoffreFort.convertion(password);

			tr = Tools.createTraitement();
			tr.load_keystore(password);
			s = tr.lecture(password);
			if (s != null) {
				jTextArea1.setText(s);
			}
		} catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
			erreur("Erreur:" + ex.getLocalizedMessage());
			return false;
		}
		return true;
	}

	private char[] new_password() {
		char[] res = null, res1, res2;
		JPasswordField pf = new JPasswordField();
		int okCxl = JOptionPane.showConfirmDialog(this, pf, "Saisissez le nouveau mot de passe", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (okCxl == JOptionPane.OK_OPTION) {
			//String password = new String(pf.getPassword());
			//System.err.println("You entered: " + password);
			res1 = pf.getPassword();
			if (res1 == null || res1.length < 10) {
				erreur("Mot de passe incorrecte");
			} else {
				JPasswordField pf2 = new JPasswordField();
				okCxl = JOptionPane.showConfirmDialog(this, pf2, "Resaisissez le nouveau mot de passe une seconde fois", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

				if (okCxl == JOptionPane.OK_OPTION) {
					res2 = pf.getPassword();
					if (res2 == null || res2.length < 10 || res2.length != res1.length
							|| !Tools.egaux(res1, res2)) {
						erreur("Mot de passe incorrecte");
					} else {
						res = res1;
					}
				}
			}
		}
		return res;
	}

	private void erreur(String msg) {
		alerte("Erreur", msg, JOptionPane.ERROR_MESSAGE);
	}

	private void alerte(String titre, String msg, int option) {
		JOptionPane.showMessageDialog(this, msg, titre, option);
	}

	private void recherche(boolean suivant) {
		final String str_recherche, str_contenu;
		final int pos_debut, pos_traitement, pos_traitement_fin, new_pos;
		int pos, i;
		str_recherche = jTextField1.getText();
		str_contenu = jTextArea1.getText();
		if (str_recherche != null && !str_recherche.trim().isEmpty()) {
			pos = jTextArea1.getCaretPosition();
			pos_debut = pos;
			if (suivant) {
				pos = str_contenu.indexOf(str_recherche, pos_debut);
				new_pos = pos + str_recherche.length();
			} else {
				pos = str_contenu.lastIndexOf(str_recherche, pos_debut);
				new_pos = Math.max(0, pos - 1);
			}
			if (pos != -1) {
				pos_traitement = pos;
				pos_traitement_fin = pos_traitement + str_recherche.length();
				SwingUtilities.invokeLater(
						new Runnable() {

							@Override
							public void run() {
								if (false) {
									jTextArea1.setCaretPosition(pos_traitement_fin);
									jTextArea1.moveCaretPosition(pos_traitement);
								} else {
									Highlighter h = jTextArea1.getHighlighter();
									h.removeAllHighlights();
									try {
										h.addHighlight(pos_traitement,
												pos_traitement_fin,
												DefaultHighlighter.DefaultPainter);
									} catch (BadLocationException ex) {
										LOGGER.error("Erreur:" + ex.getLocalizedMessage(), ex);
									}
								}
								jTextArea1.setCaretPosition(new_pos);
							}
						}
				);

			} else {
				erreur("Impossible de trouver le texte : " + str_recherche);
			}
			LOGGER.info("pos=" + pos);
		}
	}

	private void sortie() {
		String message, message2;
		List<Object> options = new ArrayList<>();
		Object defaultOption;
		message = "Sortie";
		message2 = "Etes vous sur de vouloir sortir ?";
		options.add(UIManager.getString("OptionPane.yesButtonText"));
		options.add(UIManager.getString("OptionPane.noButtonText"));
		defaultOption = UIManager.getString("OptionPane.noButtonText");
		int okCxl = JOptionPane.showOptionDialog(this, message2, message,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, options.toArray(), defaultOption);

		if (okCxl == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		sortie();
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}
}
