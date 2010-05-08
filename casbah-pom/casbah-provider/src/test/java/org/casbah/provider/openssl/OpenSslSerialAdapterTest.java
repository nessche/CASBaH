package org.casbah.provider.openssl;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.casbah.provider.CAProviderException;
import org.junit.Before;
import org.junit.Test;

public class OpenSslSerialAdapterTest {

	private String targetDir;

	@Before
	public void setup() {
		targetDir = System.getProperty("basedir") + "/target/test-classes";		
	}
	
	@Test
	public void testGetNextSerialNumber() throws CAProviderException {
		OpenSslSerialAdapter serialAdapter = new OpenSslSerialAdapter(new File(targetDir + "/caroot/serial.txt"));
		assertEquals("03",serialAdapter.getNextSerialNumber());
	}

}
