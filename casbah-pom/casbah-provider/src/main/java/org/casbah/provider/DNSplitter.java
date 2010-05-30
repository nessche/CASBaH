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
