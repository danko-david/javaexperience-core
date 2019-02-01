package eu.javaexperience.datastorage;

public interface DataStorage
{
	public DataTransaction startTransaction(String key);
}
