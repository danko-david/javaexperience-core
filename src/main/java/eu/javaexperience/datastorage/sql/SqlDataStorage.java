package eu.javaexperience.datastorage.sql;

import java.sql.Connection;

import eu.javaexperience.database.ConnectionPool;
import eu.javaexperience.database.collection.JdbcMap.JdbcMapImplProvider;
import eu.javaexperience.datastorage.DataStorage;
import eu.javaexperience.datastorage.DataTransaction;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class SqlDataStorage implements DataStorage
{
	protected ConnectionPool pool;
	protected GetBy1<JdbcMapImplProvider<String, Object>, Connection> access;
	
	public SqlDataStorage(ConnectionPool pool, GetBy1<JdbcMapImplProvider<String, Object>, Connection> access)
	{
		 this.pool = pool;
		 this.access = access;
	}

	@Override
	public DataTransaction startTransaction(String key)
	{
		try
		{
			return new SqlDataTransaction(this, key);
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
}
