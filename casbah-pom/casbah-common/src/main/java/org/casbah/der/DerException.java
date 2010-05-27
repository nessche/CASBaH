package org.casbah.der;

public class DerException extends Exception {

	private static final long serialVersionUID = 1L;

	public DerException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public DerException(String message) {
		super(message);
	}
	
}
