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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.casbah.common.CasbahException;
import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateMetainfo;
import org.casbah.provider.KeyCertificateBundle;
import org.casbah.provider.Principal;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

public class IssuedCertificateList extends CustomComponent
{
	private static enum BundleType {
		PKCS12, OPENSSL, JKS;
	}
	


	private static final Logger logger = Logger.getLogger(IssuedCertificateList.class.getCanonicalName());
	
	private static final String PKCS12_MIME_TYPE = "application/x-pkcs12";
	private static final String PKCS12_EXTENSION = ".p12";
	private static final String ZIP_MIME_TYPE = "application/zip";
	private static final String ZIP_EXTENSION = ".zip";
	private static final String JKS_MIME_TYPE = "application/octet-stream";
	private static final String JKS_EXTENSION = ".jks";

	private static final long serialVersionUID = 1L;
	private final Application parentApplication;
	private final CAProvider provider;
	private Table table;

	public IssuedCertificateList(Application parentApplication, CAProvider provider) {
		
		this.parentApplication = parentApplication;
		this.provider = provider;	
	}
	
	public void init() throws CAProviderException {
		
		VerticalLayout layout =  new VerticalLayout();
		
		table = new Table();
		table.addContainerProperty("Serial Number", String.class, null);
		table.addContainerProperty("Distinguished Name", String.class, null);
		table.addContainerProperty("Expiration Date", Date.class, null);
		
		refreshTable();

		
		layout.addComponent(table);
		table.setSizeFull();
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponent(new Button("Create Key/Certificate Pair", new ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
				collectKeyCertificateInfo();
				} catch (CasbahException e) {
					parentApplication.getMainWindow().showNotification(
							"An error prevents the creation of the certificate", Notification.TYPE_ERROR_MESSAGE);
				}
			}


		}));
		
		
		buttons.setSizeFull();
		layout.addComponent(buttons);
		
        layout.setSizeFull();
        setSizeFull();
		
		setCompositionRoot(layout);
	}
	
	private void refreshTable() throws CAProviderException {
		table.removeAllItems();
		List<CertificateMetainfo> certs = provider.getIssuedCertificates();
		
		for (CertificateMetainfo cert : certs) {
			table.addItem(new Object[] { cert.getSerial(), cert.getDn(), cert.getExpDate()}, cert.getSerial());
		}		
	}
	
	private void createKeyCertificatePair(Principal principal, String keypass, BundleType type) throws CasbahException, IOException, GeneralSecurityException {

		logger.info("Generating key/cert bundle");
		KeyCertificateBundle bundle = provider.getKeyCertificateBundle(principal.toX500Principal(), keypass);
		DownloadResource dr = null;
		switch (type) {
		case PKCS12:
			logger.info("Create PKCS12 keystore");
			PKCS12Resource pkcs12Source = new PKCS12Resource(keypass.toCharArray(), bundle.getPrivateKey(), bundle.getCertificate(), provider.getCACertificate());
			logger.info("Sending PKCS12 to client");
			dr = new DownloadResource(pkcs12Source, PKCS12_MIME_TYPE, "mykey" + PKCS12_EXTENSION, parentApplication);
			break;
		case OPENSSL:
			ZipResource zipSource = UiHelper.bundleKeyAndCertificateChain(keypass, bundle.getPrivateKey(), bundle.getCertificate(), provider.getCACertificate());
			dr = new DownloadResource(zipSource, ZIP_MIME_TYPE, "mykey" + ZIP_EXTENSION, parentApplication);
			break;
		case JKS:
			JavaKeystoreResource jksSource = new JavaKeystoreResource(keypass.toCharArray(), bundle.getPrivateKey(), bundle.getCertificate(), provider.getCACertificate());
			dr = new DownloadResource(jksSource, JKS_MIME_TYPE, "mykey" + JKS_EXTENSION, parentApplication);
			break;
		}
	
		parentApplication.getMainWindow().open(dr,"_new");	
		logger.info("Refreshing table");
		refreshTable();
	}
	
	private void collectKeyCertificateInfo() throws CAProviderException {
		final Window principalWindow = new Window("Specify Certificate Details");
		principalWindow.setPositionX(200);
		principalWindow.setPositionY(100);
		principalWindow.setWidth("600px");
		principalWindow.setHeight("500px");
		principalWindow.addListener(new Window.CloseListener() {
			
			private static final long serialVersionUID = 1L;

			public void windowClose(CloseEvent e) {
				parentApplication.getMainWindow().removeWindow(principalWindow);
				
			}
		});
		
		VerticalLayout vl = new VerticalLayout();
		final PrincipalComponent pc = new PrincipalComponent();
		Principal parentPrincipal = new Principal(provider.getCACertificate().getSubjectX500Principal());
		pc.init(new Principal(parentPrincipal, provider.getRuleMap()));
		vl.addComponent(pc);
		HorizontalLayout passLayout = new HorizontalLayout();
		vl.addComponent(passLayout);
		final TextField pass1 = new TextField("Private key/keystore passphrase");
		pass1.setSecret(true);
		final TextField pass2 = new TextField();
		pass2.setSecret(true);
		passLayout.addComponent(pass1);
		passLayout.addComponent(pass2);
		passLayout.setComponentAlignment(pass1, Alignment.BOTTOM_CENTER);
		passLayout.setComponentAlignment(pass2, Alignment.BOTTOM_CENTER);
		
		final OptionGroup type = new OptionGroup("Bundle Type");
		type.addItem(BundleType.OPENSSL);
		type.addItem(BundleType.PKCS12);
		type.addItem(BundleType.JKS);
		type.setValue(BundleType.OPENSSL);
		vl.addComponent(type);
		
		HorizontalLayout buttonsLayout = new HorizontalLayout();
		buttonsLayout.addComponent(new Button("Create", new Button.ClickListener()  {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				if (pass1.getValue().equals(pass2.getValue())) {
					try {
						createKeyCertificatePair(pc.toPrincipal(), (String) pass1.getValue(),
								(BundleType) type.getValue());
						parentApplication.getMainWindow().removeWindow(principalWindow);
					} catch (Exception e) {
						logger.severe(e.getMessage());
						parentApplication.getMainWindow().showNotification("An error prevented the correct creation of the key/certificate pair",
								Notification.TYPE_ERROR_MESSAGE);
					}
				} else {
					parentApplication.getMainWindow().showNotification("Passphrases do not match",
							Notification.TYPE_ERROR_MESSAGE);
				}
			}
		}));
		buttonsLayout.addComponent(new Button("Cancel", new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				parentApplication.getMainWindow().removeWindow(principalWindow);
			}
		}));

		vl.addComponent(buttonsLayout);
		principalWindow.setContent(vl);
		parentApplication.getMainWindow().addWindow(principalWindow);
	}
}
