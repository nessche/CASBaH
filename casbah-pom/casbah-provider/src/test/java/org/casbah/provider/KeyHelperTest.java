/*******************************************************************************
 * Copyright (C) 2010 Marco Sandrini
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.casbah.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.security.PrivateKey;

import org.junit.Test;

public class KeyHelperTest {

	
	@Test
	public void testGetPkcs8Key() throws CAProviderException {
		File keyFile = new File(this.getClass().getResource("/04.pkcs8").getFile());
		assertNotNull(keyFile);
		System.out.println(keyFile.length());
		assertTrue(keyFile.length() > 0);
		PrivateKey privateKey = KeyHelper.readKeyFromPkcs8File("password", keyFile);
		assertNotNull(privateKey);
		assertEquals("PKCS#8",privateKey.getFormat());
	}
	

}
