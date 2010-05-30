/*******************************************************************************
 * Copyright (C) 2010 Marco Sandrini
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.casbah.provider;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

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

	Map<Principal.PrincipalField, Principal.MatchingRule> getRuleMap();
	
}
