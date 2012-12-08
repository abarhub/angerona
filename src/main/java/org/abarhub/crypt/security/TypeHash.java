/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.crypt.security;

/**
 *
 * @author abarret
 */
public enum TypeHash {
    SHA1("SHA1","SHA-1"),SHA256("SHA256","SHA-256"),SHA512("SHA512","SHA-512");
    
    private TypeHash(String nom,String algo){
        this.nom=nom;
        this.algo=algo;
    }
    
    private final String nom;
    private final String algo;

    public String getAlgo() {
        return algo;
    }

    public String getNom() {
        return nom;
    }
}
