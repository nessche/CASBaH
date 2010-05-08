package org.casbah.ui;

import java.util.Date;
import java.util.List;

import org.casbah.provider.CertificateMetainfo;

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

	public IssuedCertificateList(List<CertificateMetainfo> certs) {
		Panel panel = new Panel("Issued Certificates");
		panel.setContent(new VerticalLayout());
		
		Table table = new Table();
		table.addContainerProperty("Serial Number", String.class, null);
		table.addContainerProperty("Distinguished Name", String.class, null);
		table.addContainerProperty("Expiration Date", Date.class, null);
		
		for (CertificateMetainfo cert : certs) {
			table.addItem(new Object[] { cert.getSerial(), cert.getDn(), cert.getExpDate()}, cert.getSerial());
		}
		
		panel.addComponent(table);
		
        panel.getContent().setSizeUndefined();
        panel.setSizeUndefined();
        setSizeUndefined();

		
		setCompositionRoot(panel);
	}
}
