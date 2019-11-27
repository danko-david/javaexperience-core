package eu.javaexperience.storage.warehouse;

import java.io.IOException;

public interface MultiDataWarehouse<T>
{
	public DataWarehouse<T> getWarehouse(String key) throws IOException;
}
