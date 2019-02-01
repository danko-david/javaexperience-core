package eu.javaexperience.datastorage;

public class PrefixDataStorage implements DataStorage
{
	protected final String prefix;
	protected final DataStorage storage;
	
	public PrefixDataStorage(String prefix, DataStorage origin)
	{
		this.prefix = prefix;
		this.storage = origin;
	}
	
	@Override
	public DataTransaction startTransaction(String key)
	{
		return storage.startTransaction(prefix+key);
	}
}
