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
