package org.casbah.provider.openssl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class OpenSslWrapper {

	private static final String CASBAH_SSL_CA_ROOT = "CASBAH_SSL_CA_ROOT";
	private final String opensslExecutable;
	private final File caRootDir;
	private static final long TIMEOUT = 60;

	public OpenSslWrapper(String opensslExecutable, File caRootDir) {
		this.opensslExecutable = opensslExecutable;
		this.caRootDir = caRootDir;
		
	}
	
	public int executeCommand(StringBuffer output, StringBuffer error, final List<String> parameters) throws IOException, InterruptedException {
		List<String> fullParams = new ArrayList<String>(parameters);
		fullParams.add(0, opensslExecutable);
		ProcessBuilder processBuilder = new ProcessBuilder(fullParams);
		CyclicBarrier barrier = new CyclicBarrier(3);
		Map<String, String> env = processBuilder.environment();
		env.put(CASBAH_SSL_CA_ROOT, caRootDir.getAbsolutePath());
		Process proc = processBuilder.start();
		StreamConsumer outputConsumer = new StreamConsumer(output, proc.getInputStream(), barrier, TIMEOUT);
		StreamConsumer errorConsumer = new StreamConsumer(error, proc.getErrorStream(), barrier, TIMEOUT);
		outputConsumer.start();
		errorConsumer.start();
		int returnValue = proc.waitFor();
		try {
			barrier.await(TIMEOUT, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	
	
}
