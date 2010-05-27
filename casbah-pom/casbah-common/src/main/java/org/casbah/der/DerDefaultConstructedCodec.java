package org.casbah.der;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DerDefaultConstructedCodec implements DerCodec {

	private static final DerDefaultConstructedCodec instance = new DerDefaultConstructedCodec();
	
	public static DerDefaultConstructedCodec getInstance() {
		return instance;
	}
	
	@Override
	public Object decode(InputStream inputStream, int length) throws Exception {
		if (inputStream.available() < length) {
			throw new DerException("Not enough available data");
		}
		byte[] data = new byte[length];
		inputStream.read(data);
		List<DerObject> children = new ArrayList<DerObject>();
		DerInputStream dis = new DerInputStream(new ByteArrayInputStream(data));
		while (dis.available() > 0) {
			children.add(dis.readNextObject());
		}
		return children.toArray(new DerObject[0]);
	}

	@Override
	public void encode(Object target, OutputStream outputStream) throws Exception {
		if (!(target instanceof DerObject[])) {
			throw new Exception("Unsupported object type");
		}
		DerOuputStream dos = new DerOuputStream(outputStream);
		DerObject[] objs = (DerObject[]) target;
		for (DerObject obj : objs) {
			dos.writeObject(obj);
		}
	}	
	
}
