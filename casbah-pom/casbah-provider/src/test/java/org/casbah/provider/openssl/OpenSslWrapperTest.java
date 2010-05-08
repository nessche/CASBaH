package org.casbah.provider.openssl;

import static org.junit.Assert.*;

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
		OpenSslWrapper wrapper = new OpenSslWrapper("openssl", targetDir + "/caroot" );

		int result = wrapper.executeCommand(output, error, Arrays.asList("req", "-noout", "-text", "-in", targetDir + "/client/requests/01.csr"));
		assertEquals("Checking result value", 0, result);
		assertTrue("Checking output", output.length() > 0);
		assertTrue("Checking output2", output.toString().startsWith("Certificate Request"));
	}
	
}