package eu.javaexperience.datastorage.javaImpl;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.collection.map.TransactionMap;
import eu.javaexperience.datastorage.DataTransaction;
import eu.javaexperience.datastorage.TransactionException;
import eu.javaexperience.reflect.Mirror;

public class JavaDataTransaction implements DataTransaction
{
	protected TransactionMap<String, Object> transactionMap;
	
	public JavaDataTransaction(Map<String, Object> origin)
	{
		transactionMap = new TransactionMap<String, Object>(origin);
	}
	
	protected boolean closed = false;
	
	protected void assertOpen()
	{
		if(closed)
		{
			throw new RuntimeException("DataStorage is already closed");
		}
	}
	
	@Override
	public int size()
	{
		assertOpen();
		return transactionMap.size();
	}

	@Override
	public boolean isEmpty()
	{
		assertOpen();
		return transactionMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		assertOpen();
		return transactionMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		assertOpen();
		return transactionMap.containsValue(value);
	}

	@Override
	public Object get(Object key)
	{
		assertOpen();
		return transactionMap.get(key);
	}

	@Override
	public Object put(String key, Object value)
	{
		assertOpen();
		return transactionMap.put(key, value);
	}

	@Override
	public Object remove(Object key)
	{
		assertOpen();
		return transactionMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m)
	{
		assertOpen();
		transactionMap.putAll(m);
	}

	@Override
	public void clear()
	{
		assertOpen();
		transactionMap.clear();
	}

	@Override
	public Set<String> keySet()
	{
		assertOpen();
		return transactionMap.keySet();
	}

	@Override
	public Collection<Object> values()
	{
		assertOpen();
		return transactionMap.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet()
	{
		assertOpen();
		return transactionMap.entrySet();
	}

	@Override
	public void rollback()
	{
		closed = true;
	}
	
	@Override
	public void close() throws IOException
	{
		closed = true;
	}
	
	@Override
	public void commit() throws TransactionException
	{
		Map<String, Object> origin = transactionMap.getOriginalMap();
		synchronized(origin)
		{
			Map<String, Object> access = transactionMap.getAccessMap();
			
			for(Entry<String, Object> kv:access.entrySet())
			{
				String k = kv.getKey();
				Object o = origin.get(k);
				Object a = transactionMap.getAccessTimeValue(k);
				
				if(!Mirror.equals(o, a))
				{
					throw new TransactionException("Data under key `"+k+"` modified by another thread since transaction started. Try again.");
				}
			}
			
			origin.putAll(transactionMap.getDiffMap());
		}
	}
}
