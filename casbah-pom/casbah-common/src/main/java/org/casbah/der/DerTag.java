package org.casbah.der;

import java.io.InputStream;

import org.casbah.common.EqualsUtil;
import org.casbah.common.Hash;

public class DerTag {

    public static final int CLASS_UNIVERSAL = 0x00;
    public static final int CLASS_APPLICATION = 0x40;
    public static final int CLASS_CONTEXT = 0x80;
    public static final int CLASS_PRIVATE = 0xc0;
    public static final int CLASS_MASK = 0xc0;
    
    public static final int COMPLEX_MASK = 0x20;
    
    public static final int TAG_SINGLE_BYTE_MASK = 0x1f;	
	
	private final int tag;
	private final int tagClass;
	private final boolean constructed;
	
	public DerTag(final int tag, final int tagClass, final boolean constructed) {
		this.tag = tag;
		this.tagClass = tagClass;
		this.constructed = constructed;
		
	}

	public boolean isConstructed() {
		return constructed;
	}

	public int getTagClass() {
		return tagClass;
	}

	public int getTag() {
		return tag;
	}
	
	@Override
	public int hashCode() {
		return new Hash().add(tag).add(tagClass).add(constructed).hashCode();
		
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DerTag)) {
			return false;
		}
		DerTag otherTag = (DerTag) other;
		return (EqualsUtil.areEqual(tag, otherTag.tag) &&
				EqualsUtil.areEqual(tagClass, otherTag.tagClass) &&
				EqualsUtil.areEqual(constructed, otherTag.constructed));
	}
	
	public static DerTag decode(InputStream inputStream) throws Exception {
		int firstByte = inputStream.read();
		if (firstByte == -1) {
			throw new Exception("Unexpected end of data");
		}
		int tagClass = firstByte & CLASS_MASK;
		boolean complex = ((firstByte & COMPLEX_MASK) != 0);
		int tag = firstByte &TAG_SINGLE_BYTE_MASK;
		if (tag == TAG_SINGLE_BYTE_MASK) {
			// TODO implement this case
		}
		return new DerTag(tag, tagClass, complex);
	}
	
}
