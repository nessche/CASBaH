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

import java.io.InputStream;
import java.io.OutputStream;

public class DerDefaultPrimitiveCodec implements DerCodec {

	private static final DerDefaultPrimitiveCodec instance = new DerDefaultPrimitiveCodec();
	
	public static DerDefaultPrimitiveCodec getInstance() {
		return instance;
	}
	
	@Override
	public Object decode(InputStream inputStream, int length) throws Exception {
		if (inputStream.available() < length) {
			throw new Exception("Not enough data");
		}
		byte[] result = new byte[length];
		inputStream.read(result);
		return result;
	}

	@Override
	public void encode(Object target, OutputStream outputStream) throws Exception {
		if (!(target instanceof byte[])) {
			throw new Exception("Unsupported target type");
		}
		outputStream.write((byte[]) target);
	}	
	
}
