/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.abarhub.angerona.test;

import org.abarhub.angerona.utils.Tools;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * @author abarret
 */
public class TestConvHex {

	// TODO add test methods here. The name must begin with 'test'. For example:
	@Test
	public void test1() {
		byte[] tab1, tab2;

		tab1 = new byte[]{1, 2, 3};
		tab2 = new byte[]{1, 2, 3};
		assertTrue(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{1, 2, 3};
		tab2 = new byte[]{1, 2, 4};
		assertTrue(!Tools.egaux(tab1, tab2));

		tab1 = new byte[]{1, 2, 3};
		tab2 = new byte[]{2, 2, 3};
		assertTrue(!Tools.egaux(tab1, tab2));

		tab1 = new byte[]{1, 2, 3};
		tab2 = new byte[]{1, 2, 3, 4};
		assertTrue(!Tools.egaux(tab1, tab2));

		tab1 = new byte[]{1, 2, 3};
		tab2 = null;
		assertTrue(!Tools.egaux(tab1, tab2));

		tab1 = null;
		tab2 = new byte[]{1, 2, 3};
		assertTrue(!Tools.egaux(tab1, tab2));

		tab1 = null;
		tab2 = null;
		assertTrue(Tools.egaux(tab1, tab2));

		tab1 = new byte[]{9, 9, 9, 9};
		tab2 = new byte[]{7, 7, 7, 7};
		assertTrue(!Tools.egaux(tab1, tab2));

		tab1 = new byte[]{1, 2, 3};
		tab2 = new byte[]{1, 2, 3};
		assertTrue(Tools.egaux(tab1, tab2));
	}

	@Test
	public void test2() throws DecoderException, UnsupportedEncodingException {
		byte[] tab1, tab2;
		String s;

		tab1 = new byte[]{1, 2, 3};
		s = Tools.convHexString(tab1);
		assertNotNull(s);
		assertTrue(s.length() == 6);
		assertEquals("010203", s);
		tab2 = Tools.convHexByte(s);
		assertTrue(Tools.egaux(tab1, tab2));
	}

	@Test
	public void test3() throws DecoderException, UnsupportedEncodingException {
		byte[] tab1, tab2;
		String s;

		tab1 = new byte[]{6, 2, -8, 9, 127, -128, 115, 19, -69};
		s = Tools.convHexString(tab1);
		assertNotNull(s);
		assertTrue(s.length() == tab1.length * 2);
		assertEquals("0602f8097f807313bb", s);
		tab2 = Tools.convHexByte(s);
		assertTrue(Tools.egaux(tab1, tab2));
	}

	@Test
	public void test4() throws DecoderException, UnsupportedEncodingException {
		byte[] tab1, tab2;
		String s;
		int i;
		byte min, max;

		min = Byte.MIN_VALUE;
		max = Byte.MAX_VALUE;
		tab1 = new byte[max - min + 1];
		//i=tab1[256];
		i = 0;
		for (byte b = min; b <= max; b++) {
			tab1[i] = b;
			i++;
			if (b == max)
				break;
		}
		s = Tools.convHexString(tab1);
		assertNotNull(s);
		assertTrue(s.length() == tab1.length * 2);
		//assertEquals("0602f8097f807313bb",s);
		tab2 = Tools.convHexByte(s);
		assertTrue(Tools.egaux(tab1, tab2));
	}

	@Test
	public void test5() throws DecoderException, UnsupportedEncodingException {
		byte[] tab1, tab2;
		String s;
		int nb = 20;
		byte min, max;

		for (int i = 0; i < nb; i++) {
			tab1 = Tools.generate_random_bytes(350);
			s = Tools.convHexString(tab1);
			assertNotNull(s);
			assertTrue(s.length() == tab1.length * 2);
			//assertEquals("0602f8097f807313bb",s);
			tab2 = Tools.convHexByte(s);
			assertTrue(Tools.egaux(tab1, tab2));
		}
	}
}
