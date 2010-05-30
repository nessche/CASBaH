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
package org.casbah.provider;

import java.util.HashMap;
import java.util.Map;

public class DNSplitter {

	private static final String CANONICAL_SPLITTER = "(?<!\\\\),";
	
	public static String[] splitCanonicalDN(String dn) {
		if (dn == null) {
			throw new IllegalArgumentException("dn cannot be null");
		}
		return dn.split(CANONICAL_SPLITTER);
		
	}
	
	public static Map<String, String> splitCanonicalDNToMap(String dn) {
		String[] fields = splitCanonicalDN(dn);
		Map<String, String> result = new HashMap<String, String>(); 
		for (String field : fields) {
			int index = field.indexOf('=');
			String key = field.substring(0, index).trim();
			String value = field.substring(index + 1).trim();
			result.put(key, value);
		}
		return result;
	}
}
