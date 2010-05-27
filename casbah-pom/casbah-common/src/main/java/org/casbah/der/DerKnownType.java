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
