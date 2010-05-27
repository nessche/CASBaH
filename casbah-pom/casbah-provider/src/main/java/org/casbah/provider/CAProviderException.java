package org.casbah.provider;

import org.casbah.common.CasbahException;

public class CAProviderException extends CasbahException {

	private static final long serialVersionUID = 318881955466985649L;

	public CAProviderException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CAProviderException(String message) {
		super(message, null);
	}
	
}
