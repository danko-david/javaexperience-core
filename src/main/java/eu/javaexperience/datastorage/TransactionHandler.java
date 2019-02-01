package eu.javaexperience.datastorage;

import java.io.IOException;

import eu.javaexperience.reflect.Mirror;

public abstract class TransactionHandler
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
	
	protected abstract void doExecute(DataTransaction tr) throws Exception;
	
	public void execute()
	{
		int retryCount = 0;
		do
		{
			DataTransaction tr = storage.startTransaction(key);
			try
			{
				doExecute(tr);
				tr.commit();
				return;
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
