package eu.javaexperience.datastorage.jdbcImpl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.collection.JdbcMap;
import eu.javaexperience.datastorage.DataTransaction;
import eu.javaexperience.datastorage.TransactionException;
import eu.javaexperience.reflect.Mirror;

/**
 * id,
 * key,
 * value
 * 
 * 
 * */
@Deprecated
public class JdbcDataTransaction extends JdbcMap<String, Object> implements DataTransaction
{
	public JdbcDataTransaction(Connection conn, String table, int group)
	{
		super(createAccessor(conn, table, group));
	}
	
	protected static JdbcMapImplProvider<String, Object> createAccessor(final Connection conn, final String table, final int grp)
	{
		return new JdbcMapImplProvider<String, Object>()
		{
			@Override
			public Connection getConnection()
			{
				return conn;
			}

			@Override
			public void releaseConnection(Connection conn)
			{
				try
				{
					conn.close();
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
				}
			}

			@Override
			public ResultSet selectWhereKey(Connection conn, String key)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("SELECT key, value FROM "+table+" where grp="+grp+" AND key = ?");
					ps.setString(1, key);
					return ps.executeQuery();
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}

			@Override
			public ResultSet selectWhereValue(Connection conn, Object val)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("SELECT key, value FROM "+table+" where grp="+grp+" AND value = ?");
					ps.setObject(1, val);
					return ps.executeQuery();
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}

			@Override
			public ResultSet selectAll(Connection conn)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("SELECT key, value FROM "+table+" where grp="+grp);
					return ps.executeQuery();
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}

			@Override
			public String extractKey(ResultSet rs)
			{
				try
				{
					return rs.getString("key");
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}

			@Override
			public Object extractValue(ResultSet rs)
			{
				try
				{
					return rs.getObject("value");
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}

			@Override
			public Object insertOrUpdate(Connection conn, String key, Object value)
			{
				
				return null;
			}

			@Override
			public Object removeByKey(Connection conn, Object key)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("DELETE FROM "+table+" where grp="+grp+" AND key = ?");
					ps.setString(1, (String) key);
					return ps.executeQuery();
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}

			@Override
			public int getMappingCount(Connection conn)
			{
				try
				{
					return JDBC.getInt(conn, "SELECT count(*) FROM "+table+" WHERE grp = "+grp);
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
					return -1;
				}
			}

			@Override
			public void emptyOutMapping(Connection conn)
			{
				try
				{
					JDBC.execute(conn, "DELETE FROM "+table+" WHERE grp = "+grp);
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
				}
			}
		};
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commit() throws TransactionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
}
