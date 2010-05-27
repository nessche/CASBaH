package org.casbah.provider;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class SSLeayEncoderTest extends TestKeyValues {
	
	@Test
	public void testDecryptKey() throws IOException, GeneralSecurityException, DecoderException {
		byte[] salt = Hex.decodeHex("73EE9A1CEEFA817D".toCharArray());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(this.getClass().getResourceAsStream("/caplaintext.key"), baos);
		byte[] expected = baos.toByteArray();
		String pemData = IOUtils.toString(this.getClass().getResourceAsStream("/caencrypted.key"));
		byte[] decData = SSLeayEncoder.decryptKey(pemData, salt, "casbah");
		System.out.println(Hex.encodeHexString(decData));
		assertArrayEquals(expected, decData);
	}
	
	@Test
	public void testDecodeKey() throws CAProviderException, IOException {
		 
		File keyFile = new File(this.getClass().getResource("/ca.key").getFile());
		String pemData = FileUtils.readFileToString(keyFile);
		PrivateKey key = SSLeayEncoder.decodeKey(pemData, "casbah");
		assertTrue(key instanceof RSAPrivateKey);
		assertEquals(TestKeyValues.PRIVATE_EXPONENT, ((RSAPrivateKey) key).getPrivateExponent());
	}
	
	private static RSAPrivateCrtKey generateKey() throws GeneralSecurityException {
		KeyFactory kf = KeyFactory.getInstance("RSA");
		RSAPrivateCrtKeySpec keyspec = new RSAPrivateCrtKeySpec(MODULUS, PUBLIC_EXPONENT,
				PRIVATE_EXPONENT, PRIME1, PRIME2, EXPONENT1, EXPONENT2, COEFFICIENT);
		return (RSAPrivateCrtKey) kf.generatePrivate(keyspec);		
	}
	
	@Test
	public void testEncodeKey() throws CAProviderException, GeneralSecurityException, IOException {
		
		RSAPrivateCrtKey key = generateKey();
		String encodedKey = SSLeayEncoder.encodeKey(key, "casbah");
		RSAPrivateCrtKey key2 = SSLeayEncoder.decodeKey(encodedKey, "casbah");
		assertEquals(key, key2);	
	}
	
	@Test
	public void testEncryptKey() throws GeneralSecurityException, DecoderException, IOException {
		
		PKCS1EncodedKey key = new PKCS1EncodedKey(generateKey());
		byte[] derEncoded = key.getEncoded();
		
		byte[] salt = Hex.decodeHex("73EE9A1CEEFA817D".toCharArray());
		String expected = FileUtils.readFileToString(new File(this.getClass().getResource("/caencrypted.key").getFile()));
		
		String pemData = SSLeayEncoder.encryptKey(derEncoded, salt, "casbah");
		assertEquals(expected, pemData);
		
	}
	
}
