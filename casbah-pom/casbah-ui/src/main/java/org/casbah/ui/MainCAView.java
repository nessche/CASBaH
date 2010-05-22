package org.casbah.ui;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.casbah.provider.CAProvider;
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
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

public class MainCAView extends CustomComponent{

	private static final String X509_CERT_MIME_TYPE = "application/x-x509-ca-cert";
	private static final String X509_CRL_MIME_TYPE = "application/x-x509-crl";
	
	private static final Logger logger = Logger.getLogger(MainCAView.class.getCanonicalName());
	private static final long serialVersionUID = 1L;

	private final Application application;
	private final CAProvider provider;

	public MainCAView(final CAProvider provider, Application application) {
		
		this.provider = provider;
		this.application = application;
		
	}
	
	public void init() throws CAProviderException {

		final X509Certificate caCert = provider.getCACertificate();
		Panel panel = new Panel("CA Details");
		VerticalLayout mainLayout = new VerticalLayout();
		panel.setContent(mainLayout);
		mainLayout.setSizeFull();
		VerticalLayout caInfo = new VerticalLayout();		
		TextField name = new TextField("Distinguished Name");
		String nameValue = caCert.getSubjectX500Principal().getName();
		name.setValue(nameValue);
		name.setColumns(50);
		name.setReadOnly(true);
		
		TextField issuer = new TextField("Issuer");
		issuer.setColumns(50);
		issuer.setValue(caCert.getIssuerX500Principal().getName());
		issuer.setReadOnly(true);
		
		DateField expDate = new DateField("Expiration Date");
		expDate.setResolution(DateField.RESOLUTION_SEC);
		expDate.setValue(caCert.getNotAfter());
		expDate.setReadOnly(true);
		
		TextField serial = new TextField("Serial");
		serial.setValue(caCert.getSerialNumber().toString(16));
		serial.setReadOnly(true);
		
		caInfo.addComponent(name);
		caInfo.addComponent(issuer);
		caInfo.addComponent(expDate);
		caInfo.addComponent(serial);
		caInfo.setSizeFull();
		
		HorizontalLayout caButtons = new HorizontalLayout();
		caButtons.addComponent(new Button("View Certificate",
				new Button.ClickListener() {
			
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							showEncodedCertificate(caCert, caCert.getSerialNumber().toString(16));
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
							downloadEncodedCertificate(caCert, caCert.getSerialNumber().toString(16));
						} catch (CAProviderException e) {
							e.printStackTrace();
						}				
					}
				}));
		
		caButtons.addComponent(new Button("Sign a CSR",
				new Button.ClickListener() {
					
					private static final long serialVersionUID = 1L;

					public void buttonClick(ClickEvent event) {
						try {
							uploadAndSignCsr();
						} catch (CAProviderException pe) {
							pe.printStackTrace();
						}
						
					}
					
				}));
		
		caButtons.addComponent(new Button("Get CRL",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							downloadCrlList(provider.getLatestCrl(false));
						} catch (CAProviderException pe) {
							logger.log(Level.SEVERE, "Could not retrieve CRL", pe);
							getWindow().showNotification("An error occurred while retrieving the CRL",
									Notification.TYPE_ERROR_MESSAGE);
						}
						
					}
					
				}));
		
		panel.addComponent(caInfo);
		panel.addComponent(caButtons);
		panel.setSizeFull();
        setSizeFull();
		setCompositionRoot(panel);

	}
	
	private void uploadAndSignCsr() throws CAProviderException {
		final Window csrWindow = new Window("Upload CSR");
		csrWindow.setPositionX(200);
		csrWindow.setPositionY(100);
		csrWindow.setWidth("800px");
		csrWindow.setHeight("300px");
		csrWindow.addListener(new Window.CloseListener() {
			
			private static final long serialVersionUID = 1L;

			public void windowClose(CloseEvent e) {
				application.getMainWindow().removeWindow(csrWindow);
				
			}
		});
		
		final TextField csrData = new TextField("DER Encoded CSR");
		csrData.setColumns(80);
		csrData.setRows(20);
		csrData.setWordwrap(false);
		csrWindow.addComponent(csrData);
		HorizontalLayout hl = new HorizontalLayout();
		csrWindow.addComponent(hl);
		hl.addComponent(new Button("Cancel", new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				application.getMainWindow().removeWindow(csrWindow);
				
			}
		}));
		hl.addComponent(new Button("Upload", new Button.ClickListener() {
			
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
				String csr = (String) csrData.getValue();
				try {
					X509Certificate result = provider.sign(csr);
					
				} catch (CAProviderException cpe) {
					cpe.printStackTrace();
				}
			}
		}));
		csrWindow.setModal(true);
		application.getMainWindow().addWindow(csrWindow);
	}	

	private void showEncodedCertificate(X509Certificate cert, String serialNumber) throws CAProviderException {
		final Window certWindow = new Window(serialNumber);
		certWindow.setPositionX(200);
		certWindow.setPositionY(100);
		certWindow.setWidth("800px");
		certWindow.setHeight("300px");
		certWindow.addListener(new Window.CloseListener() {
			
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
	
	private void downloadCrlList(X509CRL crl) throws CAProviderException {
		StringResource source = new StringResource(CertificateHelper.encodeCrlList(crl, true));
		DownloadResource dr = new DownloadResource(source, X509_CRL_MIME_TYPE, "ca.crl", application );
		application.getMainWindow().open(dr,"_new");
	}
	
	
}
