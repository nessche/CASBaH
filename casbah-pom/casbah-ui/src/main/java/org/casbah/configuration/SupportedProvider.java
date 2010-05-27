package org.casbah.configuration;

import org.casbah.common.CasbahException;

public enum SupportedProvider {
	
	OPENSSL("OpenSSL");
	
	private String userFriendlyName;
	
	private SupportedProvider(String userFriendlyName) {
		this.userFriendlyName = userFriendlyName;
		
	}
	
	public static SupportedProvider getSupportedProviderByClass(Class<? extends ProviderConfiguration> providerClass) throws CasbahException {
		if (OpenSslProviderConfiguration.class.equals(providerClass)) {
			return OPENSSL;
		} else {
			throw new CasbahException("Unsupported provider", null);
		}
	}
	
	public String getUserFriendlyName() {
		return userFriendlyName;
	}
}
