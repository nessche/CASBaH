package org.casbah.provider;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;

public class CertificateHelper {

	private static final String CERTIFICATE_ARMOR_BEGIN = "-----BEGIN CERTIFICATE-----";
	private static final String CERTIFICATE_ARMOR_END = "-----END CERTIFICATE-----";
	
	public static String encodeCertificate(X509Certificate certificate, boolean addArmor) throws CAProviderException {
		try { 
			Base64 b64 = new Base64();
			StringBuffer buffer = new StringBuffer();
			if (addArmor) {
				buffer.append(CERTIFICATE_ARMOR_BEGIN);
			}
			buffer.append(b64.encodeToString(certificate.getEncoded()));
			if (addArmor) {
				buffer.append(CERTIFICATE_ARMOR_END);
			}
			return buffer.toString();
		} catch (CertificateEncodingException cee) {
			throw new CAProviderException("Could not encode certificate", cee);
		}
	}
	
}
