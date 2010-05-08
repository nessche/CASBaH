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
