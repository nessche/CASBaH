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
