package org.casbah.provider;

import java.security.cert.X509Certificate;
import java.util.List;

public interface CAProvider {

	List<CertificateMetainfo> getIssuedCertificates() throws CAProviderException;
	
	X509Certificate sign(String csr) throws CAProviderException;
	
	X509Certificate getCACertificate() throws CAProviderException;
	
	X509Certificate getIssuedCertificateBySerialNumber(String serialNumber) throws CAProviderException;
	
	String getProviderVersion() throws CAProviderException;
	
	boolean isCASetup();
	
	
	
}
