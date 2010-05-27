package org.casbah.provider.openssl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class StreamConsumer extends Thread {
	
	final BufferedInputStream input;
	final OutputStream output;
	private final CyclicBarrier barrier;
	private final long timeout;
	
	public StreamConsumer(OutputStream output, InputStream input, CyclicBarrier barrier, long timeout) {
		this.barrier = barrier;
		this.output = output;
		this.timeout = timeout;
		this.input = new BufferedInputStream(input);
	}
	
	@Override
	public void run() {
		try {
			int i = 0;
			while ((i = input.read()) != -1) {
				output.write(i);
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
