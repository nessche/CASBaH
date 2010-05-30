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
package org.casbah.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.casbah.common.CasbahException;
import org.junit.Test;

public class CasbahConfigurationHelperTest {

	@Test
	public void testReadFromFile() throws CasbahException, FileNotFoundException {
		
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
