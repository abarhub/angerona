/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.test;

import org.abarhub.angerona.security.Tools;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author abarret
 */
public class TestTools {


	@Test
	public void testEgauxBytes() {
		byte tab1[], tab2[];

		tab1 = null;
		tab2 = new byte[]{1, 2, 3, 4, 5};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{8, 50, -62, -45, 4, 99, 76, 13};
		tab2 = null;
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{6, -2, -63, 112, 65, 78, 10, 90, 32, 45};
		tab2 = new byte[]{-99, -10, -36, 29};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123, 100};
		tab2 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123, 100};
		assertTrue(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123, 100};
		tab2 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123, 100};
		tab2 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123, 100, 105};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123, 100, 47};
		tab2 = new byte[]{-1, -86, -95, -35, -89, 35, 45, 15, 36, 78, 123, 100, 47};
		assertTrue(Tools.egaux(tab1, tab2));
	}

	@Test
	public void testEgauxChar() {
		char tab1[], tab2[];

		tab1 = null;
		tab2 = new char[]{'a', 'b', 'c', 'd'};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new char[]{'1', '5', 'h', 'R'};
		tab2 = null;
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new char[]{'a', '\'', 'é', 'M'};
		tab2 = new char[]{'0', '1', '2', '3'};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9', '<'};
		tab2 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9', '<'};
		assertTrue(Tools.egaux(tab1, tab2));

		tab1 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9', '<'};
		tab2 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9'};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9', '<'};
		tab2 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9', '<', '6'};
		assertFalse(Tools.egaux(tab1, tab2));

		tab1 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9', '<', '#'};
		tab2 = new char[]{' ', ',', '(', '5', 'D', 'i', '*', '$', '9', '<', '#'};
		assertTrue(Tools.egaux(tab1, tab2));
	}
}
