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
