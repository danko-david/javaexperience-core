package eu.javaexperience.storage.warehouse;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;

public interface DataWarehouseInput<T> extends Closeable
{
	public long getPosition();
	public boolean isSeekSupported();
	public T read() throws EOFException, IOException;
}
