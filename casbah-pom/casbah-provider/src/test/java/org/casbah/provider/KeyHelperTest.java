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
