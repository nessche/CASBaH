/*
 * Copyright (C) 2010 - Marco Sandrini
 * 
 * See file license.txt for licensing details
 */

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

