package org.casbah.provider;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class KeyCertificateBundle {

	private final PrivateKey key;
	private final X509Certificate certificate;

	public KeyCertificateBundle(final PrivateKey key, final X509Certificate certificate) {
		this.key = key;
		this.certificate = certificate;	
	}
	
	public PrivateKey getPrivateKey() {
		return key;
	}
	
	public X509Certificate getCertificate() {
		return certificate;
	}
	
}
