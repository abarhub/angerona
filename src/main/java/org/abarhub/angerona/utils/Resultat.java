/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abarret
 */
public class Resultat {

	private List<String> listMsg;

	public Resultat() {

	}

	public boolean isError() {
		return listMsg != null && !listMsg.isEmpty();
	}

	public String getMessageError() {
		if (!isError()) {
			return null;
		}
		return listMsg.get(0);
	}

	public void addError(String msg) {
		if (listMsg == null) {
			listMsg = new ArrayList<>();
		}
		listMsg.add(msg);
	}
}
