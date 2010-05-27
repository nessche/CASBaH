package org.casbah.provider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.casbah.common.StringUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SSLeayEncoderTest {
	
	@Test
	public void testDecryptKey() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		byte[] salt = StringUtils.hexStringToByteArray("73EE9A1CEEFA817D");
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
	
}
