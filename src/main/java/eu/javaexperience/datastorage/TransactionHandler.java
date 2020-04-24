package eu.javaexperience.datastorage;

import java.io.IOException;

import eu.javaexperience.reflect.Mirror;

public abstract class TransactionHandler<T>
{
	protected int retry;
	protected DataStorage storage;
	protected String key;
	
	public TransactionHandler(DataStorage storage, String key, int retry)
	{
		this.storage = storage;
		this.key = key;
		this.retry = retry;
	}
	
	protected abstract T doExecute(DataTransaction tr) throws Exception;
	
	public T execute()
	{
		int retryCount = 0;
		do
		{
			DataTransaction tr = storage.startTransaction(key);
			try
			{
				T ret = doExecute(tr);
				tr.commit();
				return ret;
			}
			catch(TransactionException ex)
			{
				if(null != tr)
				{
					tr.rollback();
				}
				if(retryCount++ == retry)
				{
					Mirror.propagateAnyway(ex);
				}
			}
			catch(Exception e)
			{
				Mirror.propagateAnyway(e);
			}
			finally
			{
				if(null != tr)
				{
					try
					{
						tr.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		while(true);
	}
}
