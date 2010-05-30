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

import static org.junit.Assert.assertArrayEquals;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class PKCS1EncodedKeyTest extends TestKeyValues {

	
	private RSAPrivateCrtKey wrappedKey;

	@Before
	public void setup() throws GeneralSecurityException {
		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPrivateCrtKeySpec keyspec = new RSAPrivateCrtKeySpec(MODULUS, PUBLIC_EXPONENT,
				PRIVATE_EXPONENT, PRIME1, PRIME2, EXPONENT1, EXPONENT2, COEFFICIENT);
		wrappedKey = (RSAPrivateCrtKey) kf.generatePrivate(keyspec);		
	}
	
	@Test
	public void testPkcs1EncodedKey() throws IOException {
		PKCS1EncodedKey key = new PKCS1EncodedKey(wrappedKey);
		byte[] derEncoded = key.getEncoded();
		
		byte[] expectedDerEncoded = FileUtils.readFileToByteArray(new File(this.getClass().getResource("/caplaintext.key").getFile()));
		System.out.println(Hex.encodeHexString(derEncoded));
		System.out.println(Hex.encodeHexString(expectedDerEncoded));
		
		assertArrayEquals(expectedDerEncoded,derEncoded);
	}
}
