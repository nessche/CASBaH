package org.casbah.ui;

import java.security.Key;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

import org.casbah.common.CasbahException;

public class JavaKeystoreResource extends KeyStoreResource{

	private static final String JKS_TYPE = "jks";
	
	public JavaKeystoreResource(char[] keypass, Key privateKey, Certificate...certificateChain) throws KeyStoreException, CasbahException {
		super(JKS_TYPE, keypass, privateKey, certificateChain);
	}

	
	
}
