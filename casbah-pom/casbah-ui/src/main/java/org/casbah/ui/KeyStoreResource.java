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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;

import org.casbah.common.CasbahException;

import com.vaadin.terminal.StreamResource.StreamSource;

public class KeyStoreResource implements StreamSource {
	
	protected InputStream inputStream;
	
	public KeyStoreResource(String keystoreType, char[] keypass, Key privateKey, Certificate...certificateChain)  throws CasbahException {
		try {
			KeyStore ks = KeyStore.getInstance(keystoreType);
			ks.load(null, keypass);
			ks.setKeyEntry("mykey", privateKey, keypass, certificateChain);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ks.store(baos, keypass);
			byte[] data = baos.toByteArray();
			inputStream = new ByteArrayInputStream(data);
			baos.close();			
		} catch (Exception e) {
			throw new CasbahException("Could not create KeyStoreResource", e);
		}
	}
	
	
	@Override
	public InputStream getStream() {
		return inputStream;
	}
}
