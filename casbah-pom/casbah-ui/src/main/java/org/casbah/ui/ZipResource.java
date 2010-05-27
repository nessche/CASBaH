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
