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
