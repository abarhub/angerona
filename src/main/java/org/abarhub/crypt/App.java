package org.abarhub.crypt;

import java.io.File;
import java.io.IOException;
import java.security.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import org.abarhub.crypt.gui.JPrincipal;
import org.abarhub.crypt.security.Config;
import org.abarhub.crypt.security.Tools;
import org.abarhub.crypt.security.Traitement;
import org.slf4j.*;

/**
 * Hello world!
 *
 */
public class App 
{
    final static Logger logger = LoggerFactory.getLogger(App.class);

    
    public static void main( String[] args )
    {
        logger.info("Demarrage...");
        init_log();
        demarre_gui();
        //test1();
        //test2();
    }

    private static void demarre_gui() {
        String message;
        
        for(int i=0;i<3;i++)
        {
            JPasswordField pf = new JPasswordField();
            if(i==0)
            {
                message="Veuillez entrer le mot de passe : ";
            }
            else
            {
                message="Veuillez entrer le mot de passe ("+(i+1)+"ème tentatives) : ";
            }
            int okCxl = JOptionPane.showConfirmDialog(null, pf, message, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (okCxl == JOptionPane.OK_OPTION) {
                //String password = new String(pf.getPassword());
                //System.err.println("You entered: " + password);
                if(verifie(pf.getPassword()))
                {
                    ouvre_fenetre(pf.getPassword());
                    break;
                }
                else
                {
                    logger.info("Erreur password demarrage");
                    JOptionPane.showMessageDialog(null, "Erreur","Mot de passe incorrecte.",JOptionPane.ERROR_MESSAGE);
                }
            }
            else if(okCxl==JOptionPane.CANCEL_OPTION)
            {
                logger.info("Abandon démarrage");
                break;
            }
        }
        
    }

    private static void ouvre_fenetre(char password[]) {
        JPrincipal tmp;
        tmp=new JPrincipal(password);
        {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (    ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                //java.util.logging.Logger.getLogger(JPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                logger.error(ex.getLocalizedMessage(), ex);
            }
        }
        tmp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	tmp.setLocationRelativeTo(null);
        tmp.setVisible(true);
    }
    
    private static void test1(){
        System.out.println(Tools.toString(Tools.generate_random_bytes(16)));
    }

    private static void test2() {
        Traitement tr;
        try {
        tr=new Traitement();
            tr.initialise_keystore("abc".toCharArray());
        } catch (GeneralSecurityException | IOException ex) {
            //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    private static boolean verifie(char[] password) {
        try {
            Traitement tr;
            tr=new Traitement();
            return tr.verifie_password(password);
        } catch (IOException ex) {
            //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            logger.error(ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    private static void init_log() {
        Config config;
        File f;
        /*Handler handler;
        try{
            config=new Config();
            f=config.getRep_data();
            Logger.getLogger("").setLevel(Level.ALL);
            if(f!=null)
            {
                handler=new FileHandler(f.getAbsolutePath()+"/myapp.log",0,10);
            }
            else
            {
                handler=new FileHandler("myapp.log",0,10);
            }
            Logger.getLogger("").addHandler(handler);
        }catch(IOException ex){
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
         
}
