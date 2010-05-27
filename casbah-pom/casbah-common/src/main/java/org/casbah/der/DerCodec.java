package org.casbah.der;

import java.io.InputStream;
import java.io.OutputStream;

public interface DerCodec {

	Object decode(InputStream inputStream, int length) throws Exception;
	
	void encode(Object target, OutputStream outputStream) throws Exception;
	
	
}
