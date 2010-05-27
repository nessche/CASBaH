package org.casbah.provider;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.security.auth.x500.X500Principal;

public interface CAProvider {

	List<CertificateMetainfo> getIssuedCertificates() throws CAProviderException;
	
	X509Certificate sign(String csr) throws CAProviderException;
	
	X509Certificate getCACertificate() throws CAProviderException;
	
	X509Certificate getIssuedCertificateBySerialNumber(String serialNumber) throws CAProviderException;
	
	String getProviderVersion() throws CAProviderException;
	
	boolean isCASetup();
	
	boolean setUpCA(X500Principal principal, String keypass) throws CAProviderException;
	
	X509CRL getLatestCrl(boolean generateCrl) throws CAProviderException;
	
	KeyCertificateBundle getKeyCertificateBundle(X500Principal principal, String keypass) throws CAProviderException;
	
	
}
