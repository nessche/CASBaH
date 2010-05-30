package org.casbah.ui;

import java.security.Key;
import java.security.cert.Certificate;

import org.casbah.common.CasbahException;

public class PKCS12Resource extends KeyStoreResource {

	private static final String PKCS12_TYPE = "pkcs12";

	
	public PKCS12Resource(char[] keypass, Key privateKey, Certificate...certificateChain)  throws CasbahException {
		super (PKCS12_TYPE, keypass, privateKey, certificateChain);
	}


}
