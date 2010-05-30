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
