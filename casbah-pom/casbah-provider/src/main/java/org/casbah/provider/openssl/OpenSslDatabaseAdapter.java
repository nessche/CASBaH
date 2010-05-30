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
package org.casbah.provider.openssl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(databaseFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.length() > 0) {
					CertificateMetainfo cm = parseLine(line);
					certs.add(cm);
				}
			}
			parsed = true;
			reader.close();
		} catch (FileNotFoundException fnfe) {
			throw new CAProviderException("Could not find database file", fnfe);
		} catch (IOException ioe) {
			throw new CAProviderException("Could not read database file", ioe);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
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
