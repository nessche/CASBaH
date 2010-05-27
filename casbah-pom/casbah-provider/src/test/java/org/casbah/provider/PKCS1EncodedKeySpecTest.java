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
