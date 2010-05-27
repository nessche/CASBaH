package org.casbah.provider;

import java.security.Key;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPrivateCrtKeySpec;

public class PKCS1EncodedKey implements Key {
	
	private static String FORMAT = "PKCS#1";
	private static final int VERSION = 0;
	
	private static final long serialVersionUID = 1L;
	private final RSAPrivateCrtKey key;

	public PKCS1EncodedKey(RSAPrivateCrtKey key) {
		this.key = key;
		
	}

	@Override
	public String getAlgorithm() {
		return key.getAlgorithm();
	}

	@Override
	public byte[] getEncoded() {
		
		return null;
	}

	@Override
	public String getFormat() {
		return FORMAT;
	}

}
