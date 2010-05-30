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
