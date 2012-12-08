package org.abarhub.crypt;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.security.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import org.abarhub.crypt.gui.JPrincipal;
import org.abarhub.crypt.security.Config;
import org.abarhub.crypt.security.Resultat;
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
                Resultat res;
                //String password = new String(pf.getPassword());
                //System.err.println("You entered: " + password);
                res=verifie(pf.getPassword());
                if(!res.isError())
                {
                    ouvre_fenetre(pf.getPassword());
                    break;
                }
                else
                {
                    String msg_err;
                    msg_err=res.getMessageError();
                    if(msg_err!=null&&msg_err.contains("Illegal key size"))
                    {
                        msg_err+=". Pb de contrainte du key size pour ce JRE ?";
                        logger.info("Erreur key size pour le jre ? ( "
                                + "http://www.bouncycastle.org/wiki/display/JA1/Frequently+Asked+Questions "
                                + "classpath:"+System.getenv("CLASSPATH")+" )");
                    }
                    logger.info("Erreur password demarrage:"+msg_err);
                    JOptionPane.showMessageDialog(null, 
                            "Mot de passe incorrecte. "+((msg_err!=null)?"("+msg_err+")":""),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
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
        Console console = System.console();
        try {
        tr=new Traitement();
        char[] passwordChars = console.readPassword();
        String passwordString = new String(passwordChars);
            tr.initialise_keystore(passwordString.toCharArray());
        } catch (GeneralSecurityException | IOException ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    private static Resultat verifie(char[] password) {
        Resultat res=new Resultat();
        try {
            Traitement tr;
            tr=new Traitement();
            return tr.verifie_password(password);
        } catch (IOException ex) {
            //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            logger.error(ex.getLocalizedMessage(), ex);
            res.addError(ex.getLocalizedMessage());
        }
        
        return res;
    }
         
}
