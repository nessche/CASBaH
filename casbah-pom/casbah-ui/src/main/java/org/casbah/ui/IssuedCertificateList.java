package org.casbah.ui;

import java.util.Date;
import java.util.List;

import org.casbah.provider.CAProvider;
import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateMetainfo;

import com.vaadin.Application;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class IssuedCertificateList extends CustomComponent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Application parentApplication;
	private final CAProvider provider;

	public IssuedCertificateList(Application parentApplication, CAProvider provider) {
		
		this.parentApplication = parentApplication;
		this.provider = provider;	
	}
	
	public void init() throws CAProviderException {
		
		Panel panel = new Panel("Issued Certificates");
		panel.setContent(new VerticalLayout());
		
		Table table = new Table();
		table.addContainerProperty("Serial Number", String.class, null);
		table.addContainerProperty("Distinguished Name", String.class, null);
		table.addContainerProperty("Expiration Date", Date.class, null);
		
		List<CertificateMetainfo> certs = provider.getIssuedCertificates();
		
		for (CertificateMetainfo cert : certs) {
			table.addItem(new Object[] { cert.getSerial(), cert.getDn(), cert.getExpDate()}, cert.getSerial());
		}
		
		panel.addComponent(table);
		table.setSizeFull();
        panel.setSizeFull();
        setSizeFull();
		
		setCompositionRoot(panel);
	}
}
