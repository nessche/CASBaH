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
import java.math.BigInteger;

public class DerInputStream extends InputStream {
	
	InputStream inputStream;
	
	public DerInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public DerObject readNextObject() throws DerException {
		try {
			DerTag tag = DerTag.decode(inputStream);
			DerKnownType type = DerKnownType.valueOf(tag);
			DerCodec codec = (type == null ? getDefaultCodec(tag) : type.getCodec());
			return new DerObject(tag, codec.decode(inputStream, readLength()));
		} catch (Exception e) {
			throw new DerException("Could not read next DerObject", e);
		}
	}
	
	private static DerCodec getDefaultCodec(DerTag tag) {
		return (tag.isConstructed() ? DerDefaultConstructedCodec.getInstance() : 
			DerDefaultPrimitiveCodec.getInstance());
	}
	
	private int readLength() throws Exception {
		int length = 0;
		int firstByte = inputStream.read();
		if (firstByte == -1) {
			throw new Exception("Unexpected end of data");
		}
		if ((firstByte & DerObject.LENGTH_SINGLE_BYTE_MASK) == 0) {
			length = firstByte;
		} else {
			byte[] lengthBuffer = new byte[firstByte & 0x7F];
			inputStream.read(lengthBuffer);
			length = new BigInteger(1,lengthBuffer).intValue();
		}
		return length;
	}



	@Override
	public int read() throws IOException {
		return inputStream.read();
	}
	
	@Override
	public int available() throws IOException {
		return inputStream.available();
	}
	
	
	
	
	
}
