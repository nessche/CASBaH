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

public class DerBooleanCodec implements DerCodec {

	private static final int DER_BOOLEAN_TRUE = 0xFF;
	private static final int DER_BOOLEAN_FALSE = 0x00;
	
	private static final DerBooleanCodec instance = new DerBooleanCodec();
	
	public static DerBooleanCodec getInstance() {
		return instance;
	}
	
	
	@Override
	public Object decode(InputStream inputStream, int length) throws Exception {
		if (length != 1) {
			throw new Exception("Length of boolean type must be one");
		}
		if (inputStream.available() < 1) {
			throw new Exception("Unexpected end of data");
		}
		int b = inputStream.read();
		if (b == -1) {
			throw new Exception("Unexpected end of data");
		}
		switch (b) {
		case DER_BOOLEAN_FALSE:
			return Boolean.FALSE;
		case DER_BOOLEAN_TRUE:
			return Boolean.TRUE;
		default:
			throw new Exception("Unknown boolean value");
		}
	}

	@Override
	public void encode(Object target, OutputStream outputStream) throws  DerException {
		try {
			if (!(target instanceof Boolean)) {
				throw new DerException("Unsupported type of object");
			}
			outputStream.write((Boolean) target ? DER_BOOLEAN_TRUE : DER_BOOLEAN_FALSE);
		} catch (IOException ioe) {
			throw new DerException("Could not encode boolean", ioe);
		}
	}
}
