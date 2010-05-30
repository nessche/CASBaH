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

import org.casbah.common.CasbahException;
import org.casbah.configuration.CasbahConfiguration;
import org.casbah.configuration.OpenSslProviderConfiguration;
import org.casbah.configuration.SupportedProvider;
import org.casbah.provider.CAProvider;

import com.vaadin.Application;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ConfigComponent extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private final Application parentApplication;
	private final CasbahConfiguration configuration;
	private final CAProvider provider;

	public ConfigComponent(Application parentApplication, CAProvider provider, CasbahConfiguration configuration) {
		this.parentApplication = parentApplication;
		this.provider = provider;
		this.configuration = configuration;
		
	}
	
	public void init() throws CasbahException {
		VerticalLayout configInfo = new VerticalLayout();
		
		configInfo.addComponent(new Label("CASBaH Configuration"));
		
		TextField providerType = new TextField("Provider Type",	
			SupportedProvider.getSupportedProviderByClass(
					configuration.getProviderConfiguration().getClass()).getUserFriendlyName());
		providerType.setReadOnly(true);
		configInfo.addComponent(providerType);
		
		TextField providerVersion = new TextField("Provider Version",
				provider.getProviderVersion());
		providerVersion.setReadOnly(true);
		providerVersion.setWidth("400px");
		configInfo.addComponent(providerVersion);
		
		OpenSslProviderConfiguration openSslConfig = (OpenSslProviderConfiguration) configuration.getProviderConfiguration();
		
		TextField pathToExecutable = new TextField("Path to OpenSSL executable (empty if executable is in system path)",
				openSslConfig.getExecutablePath());
		pathToExecutable.setReadOnly(true);
		configInfo.addComponent(pathToExecutable);
		
		TextField carootDir = new TextField("Certificate Authority root",
				openSslConfig.getCaroot());
		carootDir.setReadOnly(true);
		configInfo.addComponent(carootDir);
		
		TextField keypass = new TextField("Password to Certificate Authority private key",
				openSslConfig.getKeypass());
		keypass.setReadOnly(true);
		keypass.setSecret(true);
		configInfo.addComponent(keypass);
		
		configInfo.setSizeFull();
		setSizeFull();
		setCompositionRoot(configInfo);
	}
	
}
