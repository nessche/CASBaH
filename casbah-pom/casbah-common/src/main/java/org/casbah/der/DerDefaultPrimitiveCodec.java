package org.casbah.der;

import java.io.InputStream;
import java.io.OutputStream;

public class DerDefaultPrimitiveCodec implements DerCodec {

	private static final DerDefaultPrimitiveCodec instance = new DerDefaultPrimitiveCodec();
	
	public static DerDefaultPrimitiveCodec getInstance() {
		return instance;
	}
	
	@Override
	public Object decode(InputStream inputStream, int length) throws Exception {
		if (inputStream.available() < length) {
			throw new Exception("Not enough data");
		}
		byte[] result = new byte[length];
		inputStream.read(result);
		return result;
	}

	@Override
	public void encode(Object target, OutputStream outputStream) throws Exception {
		if (!(target instanceof byte[])) {
			throw new Exception("Unsupported target type");
		}
		outputStream.write((byte[]) target);
	}	
	
}
