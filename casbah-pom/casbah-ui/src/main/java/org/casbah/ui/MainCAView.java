package org.casbah.ui;

import java.awt.TextArea;
import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateHelper;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Window.CloseEvent;

public class MainCAView extends CustomComponent{

	private static final String X509_CERT_MIME_TYPE = "application/x-x509-ca-cert";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Application application;

	public MainCAView(final X509Certificate cert, Application application) {
		
		this.application = application;
		Panel panel = new Panel("CA Details");
		panel.setContent(new VerticalLayout());
		VerticalLayout caInfo = new VerticalLayout();		
		TextField name = new TextField("Distinguished Name");
		String nameValue = cert.getSubjectX500Principal().getName();
		name.setValue(nameValue);
		name.setColumns(50);
		name.setReadOnly(true);
		
		TextField issuer = new TextField("Issuer");
		issuer.setColumns(50);
		issuer.setValue(cert.getIssuerX500Principal().getName());
		issuer.setReadOnly(true);
		
		DateField expDate = new DateField("Expiration Date");
		expDate.setResolution(DateField.RESOLUTION_SEC);
		expDate.setValue(cert.getNotAfter());
		expDate.setReadOnly(true);
		
		TextField serial = new TextField("Serial");
		serial.setValue(cert.getSerialNumber().toString(16));
		serial.setReadOnly(true);
		
		caInfo.addComponent(name);
		caInfo.addComponent(issuer);
		caInfo.addComponent(expDate);
		caInfo.addComponent(serial);
		caInfo.setSizeUndefined();
		
		HorizontalLayout caButtons = new HorizontalLayout();
		caButtons.addComponent(new Button("View Certificate",
				new Button.ClickListener() {
			
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							showEncodedCertificate(cert, cert.getSerialNumber().toString(16));
						} catch (CAProviderException e) {
							e.printStackTrace();
						}	
						
					}
				}));
		caButtons.addComponent(new Button("Download Certificate",
				new Button.ClickListener() {
					
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							downloadEncodedCertificate(cert, cert.getSerialNumber().toString(16));
						} catch (CAProviderException e) {
							e.printStackTrace();
						}				
					}
				}));
		
		panel.addComponent(caInfo);
		panel.addComponent(caButtons);

        setSizeUndefined();
		setCompositionRoot(panel);

	}

	private void showEncodedCertificate(X509Certificate cert, String serialNumber) throws CAProviderException {
		final Window certWindow = new Window(serialNumber);
		certWindow.setPositionX(200);
		certWindow.setPositionY(100);
		certWindow.setWidth("800px");
		certWindow.setHeight("300px");
		certWindow.addListener(new Window.CloseListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void windowClose(CloseEvent e) {
				application.getMainWindow().removeWindow(certWindow);
				
			}
		});
		String certData = CertificateHelper.encodeCertificate(cert, true);
		TextField encodedCert = new TextField("Encoded Certificate", certData);
		encodedCert.setReadOnly(true);
		encodedCert.setColumns(80);
		encodedCert.setRows(certData.split("\n").length);
		encodedCert.setWordwrap(false);
		certWindow.addComponent(encodedCert);
		certWindow.addComponent(new Button("Close",new Button.ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				application.getMainWindow().removeWindow(certWindow);
			}
		}));
		certWindow.setModal(true);
		application.getMainWindow().addWindow(certWindow);
		
	}
	
	private void downloadEncodedCertificate(X509Certificate cert, String serialNumber) throws CAProviderException {
		StringResource source = new StringResource(CertificateHelper.encodeCertificate(cert, true));
		DownloadResource dr = new DownloadResource(source, X509_CERT_MIME_TYPE, serialNumber + ".crt", application);
		application.getMainWindow().open(dr,"_new");
	}
	
	
}
