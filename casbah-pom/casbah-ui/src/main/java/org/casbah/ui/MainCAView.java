package org.casbah.ui;

import java.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateHelper;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class MainCAView extends CustomComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainCAView(X509Certificate cert) {
		
		Panel panel = new Panel("CA Details");
		panel.setContent(new VerticalLayout());
		
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
		
		panel.addComponent(name);
		panel.addComponent(issuer);
		panel.addComponent(expDate);
		panel.addComponent(serial);
		
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();
		setCompositionRoot(panel);
		
		try {
			System.out.println(CertificateHelper.encodeCertificate(cert, true));
		} catch (CAProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
}
