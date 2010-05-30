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
