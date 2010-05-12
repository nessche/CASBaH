package org.casbah.provider.openssl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class StreamConsumer extends Thread {
	
	final BufferedReader reader;
	final StringBuffer buffer;
	private final CyclicBarrier barrier;
	private final long timeout;
	
	public StreamConsumer(StringBuffer buffer, InputStream stream, CyclicBarrier barrier, long timeout) {
		this.barrier = barrier;
		this.buffer = buffer;
		this.timeout = timeout;
		reader = new BufferedReader(new InputStreamReader(stream));
	}
	
	@Override
	public void run() {
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append('\n');
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				barrier.await(timeout, TimeUnit.SECONDS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
