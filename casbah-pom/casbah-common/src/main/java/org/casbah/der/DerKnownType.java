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

import java.util.HashMap;
import java.util.Map;

public enum DerKnownType {

	BOOLEAN(new DerTag(Asn1Constants.TAG_BOOLEAN, 0, false), DerBooleanCodec.getInstance() ),
	INTEGER(new DerTag(Asn1Constants.TAG_INTEGER, 0, false), DerIntegerCodec.getInstance() ),
	SEQUENCE(new DerTag(Asn1Constants.TAG_SEQUENCE, 0, true), DerDefaultConstructedCodec.getInstance()),
	OCTETSTRING(new DerTag(Asn1Constants.TAG_OCTETSTRING, 0, false), DerDefaultPrimitiveCodec.getInstance());
	
	private final DerTag tag;
	private final DerCodec codec;
	private static final Map<DerTag, DerKnownType> typeMap = new HashMap<DerTag, DerKnownType>();
	
	static {
		for(DerKnownType type : DerKnownType.values()) {
			typeMap.put(type.getTag(), type);
		}
	}
	
	DerKnownType(DerTag tag, DerCodec codec) {
		this.tag = tag;
		this.codec = codec;
	}
	
	public DerTag getTag() {
		return tag;
	}
	
	public DerCodec getCodec() {
		return codec;
	}

	public static DerKnownType valueOf(DerTag tag) {
		return typeMap.get(tag);
	}
	
	public DerObject createInstance(Object value) {
		return new DerObject(tag, value);
	}
	
}
