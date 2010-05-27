package org.casbah.provider.openssl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

public class OpenSslWrapper {

	private static final String CASBAH_SSL_CA_ROOT = "CASBAH_SSL_CA_ROOT";
	private final String opensslExecutable;
	private final File caRootDir;
	private static final long TIMEOUT = 60;

	public OpenSslWrapper(String opensslExecutable, File caRootDir) {
		this.opensslExecutable = opensslExecutable;
		this.caRootDir = caRootDir;
		
	}
	
	public int executeCommand(String input, StringBuffer output, StringBuffer error, final List<String> parameters) throws IOException, InterruptedException {
		ByteArrayInputStream bais = null;
		ByteArrayOutputStream outputStream = null;
		ByteArrayOutputStream errorStream = null;
		if (input != null) {
			bais = new ByteArrayInputStream(input.getBytes());
		}
		try {
			outputStream = new ByteArrayOutputStream();
			errorStream = new ByteArrayOutputStream();
			int result = executeCommand(bais, outputStream, errorStream, parameters);
			output.append(outputStream.toString());
			error.append(errorStream.toString());
			return result;
		} finally {
			IOUtils.closeQuietly(bais);
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(errorStream);
		}
	}
	
	public int executeCommand(StringBuffer output, StringBuffer error, final List<String> parameters) throws IOException, InterruptedException {
		return executeCommand(null, output, error, parameters); 
	}
	
	public int executeCommand(InputStream input, OutputStream output, OutputStream error, final List<String> parameters) throws IOException, InterruptedException {
		List<String> fullParams = new ArrayList<String>(parameters);
		fullParams.add(0, opensslExecutable);
		ProcessBuilder processBuilder = new ProcessBuilder(fullParams);
		CyclicBarrier barrier = new CyclicBarrier(3);
		Map<String, String> env = processBuilder.environment();
		env.put(CASBAH_SSL_CA_ROOT, caRootDir.getAbsolutePath());
		Process proc = processBuilder.start();
		if (input != null) {
			BufferedOutputStream stdin = new BufferedOutputStream(proc.getOutputStream());
			IOUtils.copy(input, stdin);
			stdin.flush();
		}
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
