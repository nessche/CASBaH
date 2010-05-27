package org.casbah.ui;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.x500.X500Principal;

import org.casbah.common.CasbahException;
import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateMetainfo;
import org.casbah.provider.KeyCertificateBundle;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class IssuedCertificateList extends CustomComponent
{
	private static final String ZIP_MIME_TYPE = "application/zip";
	private static final String ZIP_EXTENSION = ".zip";

	private static final Logger logger = Logger.getLogger(IssuedCertificateList.class.getCanonicalName());
	
	private static final String PKCS12_MIME_TYPE = "application/x-pkcs12";
	private static final String PKCS12_EXTENSION = ".p12";
	/**
	 * 
	 */
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
			
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					createKeyCertificatePair();
				} catch (Exception e) {
					e.printStackTrace();
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
	
	private void createKeyCertificatePair() throws CasbahException, IOException {
		X500Principal principal = new X500Principal("C=FI, ST=Uusimaa, L=Helsinki, O=Harhaanjohtaja.com, CN=Certificate " + System.currentTimeMillis());
		logger.info("Generating key/cert bundle");
		KeyCertificateBundle bundle = provider.getKeyCertificateBundle(principal, "password");
		/*
		logger.info("Create PKCS12 keystore");
		PKCS12Resource source = new PKCS12Resource("password".toCharArray(), bundle.getPrivateKey(), bundle.getCertificate(), provider.getCACertificate());
		logger.info("Sending PKCS12 to client");
		DownloadResource dr = new DownloadResource(source, PKCS12_MIME_TYPE, "mykey" + PKCS12_EXTENSION, parentApplication);
		parentApplication.getMainWindow().open(dr,"_new");*/
		
		ZipResource source = UiHelper.bundleKeyAndCertificateChain("password".toCharArray(), bundle.getPrivateKey(), bundle.getCertificate(), provider.getCACertificate());
		DownloadResource dr = new DownloadResource(source, ZIP_MIME_TYPE, "mykey" + ZIP_EXTENSION, parentApplication);
		parentApplication.getMainWindow().open(dr,"_new");
		logger.info("Refreshing table");
		refreshTable();
	}
}
