package org.casbah.configuration;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.casbah.common.CasbahException;
import org.junit.Test;

public class CasbahConfigurationHelperTest {

	@Test
	public void testReadFromFile() throws CasbahException {
		
		File configFile = new File(this.getClass().getResource("/test-configuration.xml").getFile());
		CasbahConfiguration casbahConfiguration = CasbahConfigurationHelper.loadFromFile(configFile);
		assertNotNull(casbahConfiguration);
		
		
	}
	
}
