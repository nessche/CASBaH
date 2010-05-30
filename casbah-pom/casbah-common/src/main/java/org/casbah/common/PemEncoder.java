/*
 * Copyright (C) 2010 - Marco Sandrini
 * 
 * See file license.txt for licensing details
 */

package org.casbah.common;

import org.apache.commons.codec.binary.Base64;

public class PemEncoder {
	
	private static final String ARMOR_PATTERN="(?m)^[-=]{5}(.)+[=-]{5}$";
	private static final String ARMOR_MARK = "-----";
	private static final String ARMOR_BEGIN = "BEGIN";
	private static final String ARMOR_END = "END";
	
	public static String encodeToPem(byte[] data, String armorName) {
		StringBuffer buffer = new StringBuffer();
		appendArmorToBuffer(buffer, armorName, true);
		buffer.append(Base64.encodeBase64String(data));
		appendArmorToBuffer(buffer, armorName, false);
		return buffer.toString();
	}
	
	private static void appendArmorToBuffer(StringBuffer buffer, String armorName, boolean begin) {
		if (armorName != null) {
			buffer.append(ARMOR_MARK);
			buffer.append(begin ? ARMOR_BEGIN : ARMOR_END );
			buffer.append(' ');
			buffer.append(armorName);
			buffer.append(ARMOR_MARK);
			buffer.append('\n');
		}
	}
	
	public static String stripArmor(String pemData) {
		return ((pemData == null) ? null : pemData.replaceAll(ARMOR_PATTERN, ""));
	}
	
	public static byte[] decodeFromPem(String pemData) {
		String strippedData = stripArmor(pemData);
		return Base64.decodeBase64(strippedData);
	}


}
