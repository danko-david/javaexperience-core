package eu.javaexperience.storage.warehouse;

import java.io.Closeable;
import java.io.IOException;

public interface DataWarehouse<T> extends Closeable
{
	public DataWarehouseOutput<T> openOutput() throws IOException;
	public DataWarehouseInput<T> openInput() throws IOException;
}
