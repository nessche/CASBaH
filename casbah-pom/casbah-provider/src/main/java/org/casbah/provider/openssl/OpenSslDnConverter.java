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
