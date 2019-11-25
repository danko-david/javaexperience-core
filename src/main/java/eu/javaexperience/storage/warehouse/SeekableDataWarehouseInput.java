package eu.javaexperience.storage.warehouse;

public interface SeekableDataWarehouseInput<T> extends DataWarehouseInput<T>
{
	public boolean seek(long offset);
}
