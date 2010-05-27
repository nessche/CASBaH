package org.casbah.ui;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;

import org.casbah.common.CasbahException;
import org.casbah.provider.CertificateHelper;
import org.casbah.provider.SSLeayEncoder;

public class UiHelper {

	private UiHelper() {
		
	}
	
	public static ZipResource bundleKeyAndCertificateChain(String keypass, PrivateKey key, X509Certificate...certificateChain) throws IOException, GeneralSecurityException, CasbahException {
		ZipResource source = new ZipResource();
		
		if (key instanceof RSAPrivateCrtKey) {
			String encodedKey = SSLeayEncoder.encodeKey((RSAPrivateCrtKey) key, keypass);
			source.addEntry(encodedKey.getBytes(), "private.key");
		} else {
			throw new CasbahException("Unsupported key type", null);
		}
		int i = 0;
		for (X509Certificate cert : certificateChain) {
			String encodedCert = CertificateHelper.encodeCertificate(cert, true);
			source.addEntry(encodedCert.getBytes(), "cert_chain_" + Integer.toString(i++) + ".crt");
		}
		
		source.close();
		return source;
	}
	
}
