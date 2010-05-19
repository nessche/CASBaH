package org.casbah.provider.openssl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.casbah.provider.CAProviderException;
import org.casbah.provider.CertificateMetainfo;
import org.casbah.provider.CertificateType;

public class OpenSslDatabaseAdapter {

	private static final String DEFAULT_TIMEZONE = "GMT";
	private static final String UNKNOWN = "unknown";
	private final File databaseFile;
	private boolean parsed = false;
	private final List<CertificateMetainfo> certs = new ArrayList<CertificateMetainfo>();
	private SimpleDateFormat sdf;

	public OpenSslDatabaseAdapter(File databaseFile) {
		this.databaseFile = databaseFile;
		sdf = new SimpleDateFormat("yyMMddHHmmss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIMEZONE));
	}
	
	public synchronized void parse() throws CAProviderException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(databaseFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					CertificateMetainfo cm = parseLine(line);
					certs.add(cm);
				}
			}
			parsed = true;
		} catch (FileNotFoundException fnfe) {
			throw new CAProviderException("Could not find database file", fnfe);
		} catch (IOException ioe) {
			throw new CAProviderException("Could not read database file", ioe);
		}
	}
	
	private CertificateMetainfo parseLine(String line) throws CAProviderException {
		try {
			StringTokenizer st = new StringTokenizer(line, "\t");
			if (st.countTokens() != 5) {
				throw new CAProviderException("Could not parse line " + line, null);
			}
			CertificateType type = CertificateType.fromToken(st.nextToken());
			Date expDate = parseDate(st.nextToken());
			String serial = st.nextToken();
			Date revDate = null;
			String revDateString = st.nextToken();
			if (!revDateString.equals(UNKNOWN)) {
				revDate = parseDate(revDateString);
			}
			String dn = st.nextToken();
			return new CertificateMetainfo(type, expDate, revDate, serial, dn);
			
		} catch (ParseException pe) {
			throw new CAProviderException("Could not parse line", pe);
		}
	}
	
	private Date parseDate(String dateToken) throws ParseException {
		return sdf.parse(dateToken);	
	}
	
	public synchronized List<CertificateMetainfo> getIssuedCertificates() throws CAProviderException {
		if (!parsed) {
			parse();
		}
 		return new ArrayList<CertificateMetainfo>(certs);
	}
	
	public void createEmptyDatabase() throws CAProviderException {
		try {
			if (databaseFile.exists()) {
				databaseFile.delete();
			}
			databaseFile.createNewFile();
		} catch (IOException e) {
			throw new CAProviderException("Could not initialize database", e);
		}	
	}
	
}
