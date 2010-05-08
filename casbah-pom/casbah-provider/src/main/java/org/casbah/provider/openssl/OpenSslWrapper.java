package org.casbah.provider.openssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenSslWrapper {

	private static final String CASBAH_SSL_CA_ROOT = "CASBAH_SSL_CA_ROOT";
	private final String opensslExecutable;
	private final String caRootDir;

	public OpenSslWrapper(String opensslExecutable, String caRootDir) {
		this.opensslExecutable = opensslExecutable;
		this.caRootDir = caRootDir;
		
	}
	
	public int executeCommand(StringBuffer output, StringBuffer error, final List<String> parameters) throws IOException, InterruptedException {
		List<String> fullParams = new ArrayList<String>(parameters);
		fullParams.add(0, opensslExecutable);
		ProcessBuilder processBuilder = new ProcessBuilder(fullParams);
		Map<String, String> env = processBuilder.environment();
		env.put(CASBAH_SSL_CA_ROOT, caRootDir);
		Process proc = processBuilder.start();
		BufferedReader outputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		int returnValue = proc.waitFor();
		readerToStringBuffer(output, outputReader);
		readerToStringBuffer(error, errorReader); 
		return returnValue;
	}

	private void readerToStringBuffer(StringBuffer buffer, BufferedReader reader)
			throws IOException {
		if (buffer != null) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append('\n');
			}
		}
	}
	
	
}
