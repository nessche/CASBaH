package org.casbah.provider;

import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

import org.casbah.common.PemEncoder;

public class CertificateHelper {

	private static final String CERTIFICATE_ARMOR_NAME = "CERTIFICATE";
	private static final String CRL_ARMOR_NAME = "X509 CRL";
	
	public static String encodeCertificate(X509Certificate certificate, boolean addArmor) throws CAProviderException {
		try { 
			return PemEncoder.encodeToPem(certificate.getEncoded(), CERTIFICATE_ARMOR_NAME);

		} catch (CertificateEncodingException cee) {
			throw new CAProviderException("Could not encode certificate", cee);
		}
	}
	
	public static String encodeCrlList(X509CRL crl, boolean addArmor) throws CAProviderException {
		try { 
			return PemEncoder.encodeToPem(crl.getEncoded(), CRL_ARMOR_NAME);
		} catch (CRLException ce) {
			throw new CAProviderException("Could not encode crl", ce);
		}		
	}
	
	
}
