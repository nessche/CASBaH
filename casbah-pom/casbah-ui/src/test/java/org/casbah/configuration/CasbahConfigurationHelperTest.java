package org.casbah.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.casbah.common.CasbahException;
import org.junit.Test;

public class CasbahConfigurationHelperTest {

	@Test
	public void testReadFromFile() throws CasbahException {
		
		File configFile = new File(this.getClass().getResource("/test-configuration.xml").getFile());
		CasbahConfiguration casbahConfiguration = CasbahConfigurationHelper.loadFromFile(configFile);
		assertNotNull(casbahConfiguration);
		ProviderConfiguration pc = casbahConfiguration.getProviderConfiguration();
		assertTrue(pc instanceof OpenSslProviderConfiguration);
		OpenSslProviderConfiguration opensslConfig = (OpenSslProviderConfiguration) pc;
		assertEquals("caroot", opensslConfig.getCaroot());
		assertEquals("casbah", opensslConfig.getKeypass());
		
	}
	
	@Test
	public void testWriteToFile() throws CasbahException, IOException {
		
		File tempFile = File.createTempFile("config", "xml");
		tempFile.deleteOnExit();
		
		CasbahConfiguration casbahConfiguration = CasbahConfiguration.getDefaultConfiguration();
		CasbahConfigurationHelper.writeToFile(casbahConfiguration, tempFile);
		
		
		CasbahConfiguration cc2 = CasbahConfigurationHelper.loadFromFile(tempFile);
		assertNotNull(cc2);
		assertEquals(casbahConfiguration, cc2);
		
	}
	
}
