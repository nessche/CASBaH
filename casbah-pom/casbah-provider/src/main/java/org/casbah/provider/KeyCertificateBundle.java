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