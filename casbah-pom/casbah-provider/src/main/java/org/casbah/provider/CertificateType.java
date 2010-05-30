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

import org.casbah.provider.CAProviderException;

public enum CertificateType {
	VALID, EXPIRED, REVOKED; 
	
	private static final String REVOKED_TOKEN = "R";
	private static final String EXPIRED_TOKEN = "E";
	private static final String VALID_TOKEN = "V";
	
	public static CertificateType fromToken(String token) throws CAProviderException {
		if (token.equals(VALID_TOKEN)) {
			return VALID;
		} else if (token.equals(EXPIRED_TOKEN)) {
			return EXPIRED;
		} else if (token.equals(REVOKED_TOKEN)) {
			return REVOKED;
		} else {
			throw new CAProviderException("Unknown certificate type " + token, null);
		}
	}
	
	public static String toToken(CertificateType type) throws CAProviderException {
		switch (type) {
		case VALID:
			return VALID_TOKEN;
		case EXPIRED:
			return EXPIRED_TOKEN;
		case REVOKED:
			return EXPIRED_TOKEN;
		default:
			throw new CAProviderException("Unknown Certificate Type", null);
		}
	}
	
}
