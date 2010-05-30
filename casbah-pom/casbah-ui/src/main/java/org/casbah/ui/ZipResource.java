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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.vaadin.terminal.StreamResource.StreamSource;

public class ZipResource implements StreamSource{

	private final ByteArrayOutputStream baos;
	private final ZipOutputStream zos;
	private InputStream inputStream;
	
	public ZipResource() {
		baos = new ByteArrayOutputStream();
		zos = new ZipOutputStream(baos);
	}
	
	public void addEntry(byte[] data, String name) throws IOException {
		zos.putNextEntry(new ZipEntry(name));
		zos.write(data);
		zos.closeEntry();
	}
	
	public void close() throws IOException {
		zos.finish();
		inputStream = new ByteArrayInputStream(baos.toByteArray());
		baos.close();
	}
	
	@Override
	public InputStream getStream() {
		return inputStream;
	}

}
