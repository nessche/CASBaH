package org.casbah.provider;

import java.util.Date;

public class CertificateMetainfo {

	private final CertificateType type;
	private final Date expDate;
	private final Date revDate;
	private final String serial;
	private final String dn;

	public CertificateMetainfo(CertificateType type, Date expDate, Date revDate, String serial, String dn) {
		this.type = type;
		this.expDate = expDate;
		this.revDate = revDate;
		this.serial = serial;
		this.dn = dn;		
	}

	public CertificateType getType() {
		return type;
	}

	public Date getExpDate() {
		return expDate;
	}

	public Date getRevDate() {
		return revDate;
	}

	public String getSerial() {
		return serial;
	}

	public String getDn() {
		return dn;
	}
}
