package org.casbah.provider.openssl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.PrivateKey;

import org.casbah.provider.CAProviderException;
import org.junit.Before;
import org.junit.Test;

public class OpenSslKeyHelperTest {

	private File targetDir;

	@Before
	public void setup() throws IOException {
		targetDir = new File(System.getProperty("basedir") + File.separatorChar + "target" +
				File.separatorChar + "test-classes");
	}
	
	@Test
	public void testGetSSLeayKey() throws CAProviderException {
		OpenSslKeyHelper keyHelper = new OpenSslKeyHelper("openssl", new File(targetDir, "caroot"));
		File keyFile = new File(this.getClass().getResource("/caroot/keys/ca.key").getFile());
		assertNotNull(keyFile);
		System.out.println(keyFile.length());
		assertTrue(keyFile.length() > 0);
		PrivateKey privateKey = keyHelper.readKeyFromSSLeayFile("casbah", keyFile);
		assertNotNull(privateKey);
		assertEquals("PKCS#8",privateKey.getFormat());
	}
}
