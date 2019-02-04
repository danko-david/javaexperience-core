package eu.javaexperience.datastorage.sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.collection.map.MappedMap;
import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.JdbcIssuedConnection;
import eu.javaexperience.database.collection.JdbcMap;
import eu.javaexperience.datastorage.DataTransaction;
import eu.javaexperience.datastorage.TransactionException;
import eu.javaexperience.interfaces.simple.WrapUnwarpTools;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.StringTools;

public class SqlDataTransaction implements DataTransaction
{
	protected SqlDataStorage storage;
	protected String key;
	
	Connection conn;
	
	protected Map<String, Object> map;
	
	public SqlDataTransaction(SqlDataStorage sqlDataStorage, String key) throws SQLException
	{
		storage = sqlDataStorage;
		conn = sqlDataStorage.pool.getConnection();
		conn.setAutoCommit(false);
		conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		this.key = key;
		
		map = new MappedMap<>(new JdbcMap<>(sqlDataStorage.access.getBy(conn)), StringTools.isNullOrTrimEmpty(key)?WrapUnwarpTools.noWrap:WrapUnwarpTools.withPrefix(key+"."));
	}

	@Override
	public int size()
	{
		try
		{
			return map.size();			
		}
		catch(Exception e)
		{
			checkException(e);
			return -1;
		}
	}
	
	protected void checkException(Exception ex)
	{
		if(null != ex)
		{
			SQLException e = null;
			if(ex instanceof SQLException)
			{
				e = (SQLException) ex;
			}
			else if(ex.getCause() instanceof SQLException)
			{
				e = (SQLException) ex.getCause();
			}
			
			if(null != e)
			{
				if(JDBC.isCommitFailed(e))
				{
					throw new TransactionException("Modification beneath transaction.");
				}
			}
			
			Mirror.propagateAnyway(ex);
		}
	}

	@Override
	public boolean isEmpty()
	{
		try
		{
			return map.isEmpty();
		}
		catch(Exception e)
		{
			checkException(e);
			return false;
		}
		
	}

	@Override
	public boolean containsKey(Object key)
	{
		try
		{
			return map.containsKey(key);			
		}
		catch(Exception e)
		{
			checkException(e);
			return false;
		}
	}

	@Override
	public boolean containsValue(Object value)
	{
		try
		{
			return map.containsValue(StringTools.toStringOrNull(value));			
		}
		catch(Exception e)
		{
			checkException(e);
			return false;
		}
	}

	@Override
	public Object get(Object key)
	{
		try
		{
			return map.get(key);			
		}
		catch(Exception e)
		{
			checkException(e);
			return -1;
		}
	}

	@Override
	public Object put(String key, Object value)
	{
		try
		{
			return map.put(key, StringTools.toStringOrNull(value));			
		}
		catch(Exception e)
		{
			checkException(e);
			return -1;
		}
	}

	@Override
	public Object remove(Object key)
	{
		try
		{
			return map.remove(key);			
		}
		catch(Exception e)
		{
			checkException(e);
			return -1;
		}
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m)
	{
		try
		{
			map.putAll(m);			
		}
		catch(Exception e)
		{
			checkException(e);
		}
	}

	@Override
	public void clear()
	{
		try
		{
			map.clear();			
		}
		catch(Exception e)
		{
			checkException(e);
		}
	}

	@Override
	public Set<String> keySet()
	{
		try
		{
			return map.keySet();			
		}
		catch(Exception e)
		{
			checkException(e);
			return null;
		}
	}

	@Override
	public Collection<Object> values()
	{
		try
		{
			return map.values();			
		}
		catch(Exception e)
		{
			checkException(e);
			return null;
		}
	}

	@Override
	public Set<Entry<String, Object>> entrySet()
	{
		try
		{
			return map.entrySet();
		}
		catch(Exception e)
		{
			checkException(e);
			return null;
		}
	}

	@Override
	public void close() throws IOException
	{
		try
		{
			conn.rollback();
			conn.setAutoCommit(true);
		}
		catch(SQLException e)
		{
			Mirror.propagateAnyway(e);
		}
		finally
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				Mirror.propagateAnyway(e);
			}
		}
	}

	@Override
	public void commit() throws TransactionException
	{
		try
		{
			conn.commit();
		}
		catch (SQLException e)
		{
			if(JDBC.isCommitFailed(e))
			{
				throw new TransactionException("Modification beneath transaction.");
			}
			Mirror.propagateAnyway(e);
		}
	}

	@Override
	public void rollback()
	{
		try
		{
			conn.rollback();
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
	}
}
