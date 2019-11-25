package eu.javaexperience.storage.warehouse;

import java.io.Closeable;
import java.io.IOException;

public interface DataWarehouseOutput<T> extends Closeable
{
	public void write(T elem) throws IOException;
}
