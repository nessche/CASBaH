package org.casbah.provider.openssl;

import org.casbah.provider.CAProviderException;

public class OpenSslNativeException extends CAProviderException {

	private static final long serialVersionUID = 1L;

	public OpenSslNativeException(String error) {
		super(error, null);
	}
}
