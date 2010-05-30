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
