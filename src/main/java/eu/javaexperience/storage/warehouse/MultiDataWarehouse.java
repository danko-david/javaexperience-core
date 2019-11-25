package eu.javaexperience.storage.warehouse;

public interface MultiDataWarehouse<T>
{
	public DataWarehouse<T> getWarehouse(String key);
}
