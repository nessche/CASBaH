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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.spec.RSAPrivateCrtKeySpec;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class PKCS1EncodedKeySpecTest {

	
	@Test
	public void testToRsaKeySpec() throws IOException, CAProviderException {
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(this.getClass().getResourceAsStream("/caplaintext.key"), baos);
		byte[] encodedKey = baos.toByteArray();
		PKCS1EncodedKeySpec encodedKeySpec = new PKCS1EncodedKeySpec(encodedKey);
		RSAPrivateCrtKeySpec privateKeySpec = encodedKeySpec.toRsaKeySpec();
		assertNotNull(privateKeySpec);
		
		assertEquals(TestKeyValues.MODULUS, privateKeySpec.getModulus());
		assertEquals(TestKeyValues.PUBLIC_EXPONENT, privateKeySpec.getPublicExponent());
		assertEquals(TestKeyValues.PRIVATE_EXPONENT, privateKeySpec.getPrivateExponent());
		assertEquals(TestKeyValues.PRIME1, privateKeySpec.getPrimeP());
		assertEquals(TestKeyValues.PRIME2, privateKeySpec.getPrimeQ());
		assertEquals(TestKeyValues.EXPONENT1, privateKeySpec.getPrimeExponentP());
		assertEquals(TestKeyValues.EXPONENT2, privateKeySpec.getPrimeExponentQ());
		assertEquals(TestKeyValues.COEFFICIENT, privateKeySpec.getCrtCoefficient());
	
	}
}
