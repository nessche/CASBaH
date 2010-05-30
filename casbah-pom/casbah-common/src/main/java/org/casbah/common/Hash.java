/*
 * Copyright (C) 2010 - Marco Sandrini
 * 
 * See file license.txt for licensing details
 */

package org.casbah.common;

public class Hash {

	public static final int SEED = 23;
	public static final int FACTOR = 37;
	
	private int hash = SEED;
	
	private void updateHashValue(int increase) {
		hash = ((hash * FACTOR) + increase);		
	}
	
	public Hash add(int i) {
		updateHashValue(i);
		return this;
	}
	
	public Hash add(boolean b) {
		return add(b ? 1 : 0);
	}
	
	public Hash add(long l) {
		return add((int) (l ^ (l >>> 32)));
	}
	
	public Hash add(float f) {
		return add(Float.floatToIntBits(f));
	}
	
	public Hash add(Object obj) {
		return add(obj == null ? 0 : obj.hashCode());
	}
	
	public Hash add(Object... objs) {
		for (Object obj : objs) {
			updateHashValue(obj == null ? 0 : obj.hashCode());
		}
		return this;
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Hash) {
			return (this.hash == ((Hash) other).hash);
		} else {
			return false;
		}
	}
	
}
