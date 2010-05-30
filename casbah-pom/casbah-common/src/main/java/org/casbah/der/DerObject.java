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

import java.math.BigInteger;

public class DerObject {

	public static final int LENGTH_SINGLE_BYTE_MASK = 0x80;
	
    private final DerTag tag;
    private final Object value;
    
    public DerObject(DerTag tag, Object value) {
		this.tag = tag;
		this.value = value;
    	
    }

	public DerTag getTag() {
		return tag;
	}

	public Object getValue() {
		return value;
	}
	
	public BigInteger getInteger() throws DerException {
		if (!(value instanceof BigInteger)) {
			throw new DerException("Value is not of type BigInteger");
		}
		return ((BigInteger) value);
	}
	
	public Boolean getBoolean() throws DerException {
		if (!(value instanceof Boolean)) {
			throw new DerException("Value is not of type Boolean");
		}
		return ((Boolean) value);
	}
	
	public DerObject[] getChildren() throws DerException {
		if (!(value instanceof DerObject[])) {
			throw new DerException("Value is not of type DerObject[]");
		}
		return ((DerObject[]) value);
	}
	
	public byte[] getByteArray() throws DerException {
		if (!(value instanceof byte[])) {
			throw new DerException("Value is not of type byte[]");
		}
		return ((byte[]) value);
	}
	
}
