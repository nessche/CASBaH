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

import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;

public class DownloadResource extends StreamResource {

	private static final long serialVersionUID = 1L;

	public DownloadResource(StreamSource source, String mimeType, String filename, Application application) {
		super(source, filename, application);
		setMIMEType(mimeType);
	}
	
	@Override
	public DownloadStream getStream() {
		
		DownloadStream stream = new DownloadStream(getStreamSource().getStream(),
				getMIMEType(), getFilename());
		stream.setParameter("Content-Disposition", "attachment;filename=" + getFilename());
		return stream;
	}
	
}
