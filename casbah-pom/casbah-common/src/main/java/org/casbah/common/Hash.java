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
