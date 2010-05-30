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

import java.security.Key;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.util.logging.Logger;

import org.casbah.common.CasbahException;

public class JavaKeystoreResource extends KeyStoreResource{

	private static final Logger logger = Logger.getLogger(JavaKeystoreResource.class.getCanonicalName());
	private static final String JKS_TYPE = "jks";
	
	public JavaKeystoreResource(char[] keypass, Key privateKey, Certificate...certificateChain) throws KeyStoreException, CasbahException {
		super(JKS_TYPE, keypass, privateKey, certificateChain);
		logger.info("Creating object of type JavaKeystoreResource");
	}

	
	
}
