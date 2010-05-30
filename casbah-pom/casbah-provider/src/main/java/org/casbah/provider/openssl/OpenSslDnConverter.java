package org.casbah.provider.openssl;

import java.util.logging.Logger;

import org.casbah.provider.DNSplitter;

public class OpenSslDnConverter {

	private static final Logger logger = Logger.getLogger(OpenSslDnConverter.class.getCanonicalName());
	
	private OpenSslDnConverter() {
		// no instances of this class
	}
	
	public static String convertToOpenSsl(String canonicalDn) {
		if (canonicalDn == null) {
			return null;
		}
		String[] fields = DNSplitter.splitCanonicalDN(canonicalDn);
		logger.info("Detected " + fields.length + " fields");
		StringBuffer result = new StringBuffer();	
		for (String field : fields) {
			result.append('/');
			result.append(field.trim().replace("\\,",",").replace("/","\\/"));
		}
		return result.toString();
		
	}
	
}
