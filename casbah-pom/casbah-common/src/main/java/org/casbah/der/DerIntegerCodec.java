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
package org.casbah.der;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class DerIntegerCodec implements DerCodec {

	private static final DerIntegerCodec instance = new DerIntegerCodec();
	
	public static DerIntegerCodec getInstance() {
		return instance;
	}
	
	@Override
	public Object decode(InputStream inputStream, int length) throws Exception {
		if (inputStream.available() < length) {
			throw new Exception("Unexpected end of data");
		}
		byte[] buffer = new byte[length];
		inputStream.read(buffer);
		BigInteger result =  new BigInteger(buffer);
		return result;
	}

	@Override
	public void encode(Object target, OutputStream outputStream) throws IOException, DerException {
		try {
			BigInteger bigInt = null;
			if (target instanceof Number) {
				bigInt = BigInteger.valueOf(((Number) target).longValue());
			} else if (target instanceof byte[]) {
				bigInt = new BigInteger((byte[]) target);
			} else if (target instanceof byte[]) {
				bigInt = new BigInteger((String) target);
			} else if (target instanceof BigInteger) {
				// no need to clone it, as it will be immediately encoded
				bigInt = (BigInteger) target;
			} else {
				throw new DerException("Unsupported type of target " + target.getClass().getCanonicalName());
			} 
			outputStream.write(bigInt.toByteArray());
		} catch (IOException ioe) {
			throw new DerException("An error occurred while encoding to stream", ioe);
		}
	}
}
