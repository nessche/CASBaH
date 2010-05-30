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

package org.casbah.common;

public final class EqualsUtil {

	  static public boolean areEqual(boolean bool1, boolean bool2){
	    return bool1 == bool2;
	  }

	  static public boolean areEqual(char c1, char c2){
	    return c1 == c2;
	  }

	  static public boolean areEqual(long long1, long long2){
	    /*
	    * Implementation Note
	    * Note that byte, short, and int are handled by this method, through
	    * implicit conversion.
	    */
	    return long1 == long2;
	  }

	  static public boolean areEqual(float float1, float float2){
	    return Float.floatToIntBits(float1) == Float.floatToIntBits(float2);
	  }

	  static public boolean areEqual(double double1, double double2){
	    return Double.doubleToLongBits(double1) == Double.doubleToLongBits(double2);
	  }

	  static public boolean areEqual(Object obj1, Object obj2){
	    //System.out.println("Object");
	    return obj1 == null ? obj2 == null : obj1.equals(obj2);
	  }
	}

