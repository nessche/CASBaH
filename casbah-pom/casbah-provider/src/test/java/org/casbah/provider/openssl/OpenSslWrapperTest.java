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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class OpenSslWrapperTest {

	private String targetDir;
	private StringBuffer output;
	private StringBuffer error;

	@Before
	public void setup() {
		targetDir = System.getProperty("basedir") + "/target/test-classes";
		output = new StringBuffer();
		error = new StringBuffer();
	}
	
	@Test
	public void testExecute() throws IOException, InterruptedException {
		OpenSslWrapper wrapper = new OpenSslWrapper("openssl", new File(targetDir + "/caroot"));

		int result = wrapper.executeCommand(output, error, Arrays.asList("req", "-noout", "-text", "-in", targetDir + "/client/requests/01.csr"));
		System.out.println(error.toString());
		System.out.println(output.toString());
		
		assertEquals("Checking result value", 0, result);

		assertTrue("Checking output", output.length() > 0);
		assertTrue("Checking output2", output.toString().startsWith("Certificate Request"));
	}
	
	@Test
	public void testExecuteWithInput() throws IOException, InterruptedException {
		
		File outputFile = new File(targetDir, "client/keys/04.key");
		if (outputFile.exists()) {
			assertTrue(outputFile.delete());
		}
		OpenSslWrapper wrapper = new OpenSslWrapper("openssl", new File(targetDir + "/caroot"));

		String input = new String("casbah\n");
		int result = wrapper.executeCommand(input, output, error, Arrays.asList("genrsa", "-passout", "stdin", "-des3", "-out",
				outputFile.getAbsolutePath(), "2048"));
		System.out.println("ERROR: " + error.toString());
		System.out.println("OUTPUT: " + output.toString());
		
		assertEquals("Checking result value", 0, result);

		assertTrue(outputFile.exists());		
	}
	
}
