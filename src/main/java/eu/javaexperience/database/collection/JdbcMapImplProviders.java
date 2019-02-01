package eu.javaexperience.database.collection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.collection.JdbcMap.JdbcMapImplProvider;
import eu.javaexperience.reflect.Mirror;

public class JdbcMapImplProviders
{
	public static void sqliteCreateKeyValTable(Connection conn, String table) throws SQLException
	{
		JDBC.execute
		(
			conn,
			"CREATE TABLE IF NOT EXISTS `"+table+"` (`key` VARCHAR(500) PRIMARY KEY, `val` VARCHAR(500))"
		);
	}
	
	public static void mysqlCreateKeyValTable(Connection conn, String table) throws SQLException
	{
		JDBC.execute
		(
			conn,
			"CREATE TABLE IF NOT EXISTS `"+table+"` (`key` CHAR(120) PRIMARY KEY, val CHAR(120))"
		);
	}
	
	public static JdbcMapImplProvider<String, String> fromPrepared
	(
		final Connection conn,
		final String table,
		final String insertOrUpdate
		
	)
	{
		return new JdbcMapImplProvider<String, String>()
		{
			@Override
			public Connection getConnection()
			{
				try
				{
					return conn;
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public void releaseConnection(Connection conn)
			{
			}

			@Override
			public ResultSet selectWhereKey(Connection conn, String key)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("SELECT `key`, `val` FROM "+table+" WHERE `key` = ?");
					ps.setObject(1, key);
					return ps.executeQuery();
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public ResultSet selectWhereValue(Connection conn, String val)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("SELECT `key`, `val` FROM "+table+" WHERE `val` = ?");
					ps.setObject(1, val);
					return ps.executeQuery();
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public ResultSet selectAll(Connection conn)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("SELECT `key`, `val` FROM "+table);
					return ps.executeQuery();
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public String extractKey(ResultSet rs)
			{
				try
				{
					return (String) rs.getObject("key");
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public String extractValue(ResultSet rs)
			{
				try
				{
					return (String) rs.getObject("val");
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public String insertOrUpdate(Connection conn, String key, String value)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement(insertOrUpdate);
					ps.setObject(1, key);
					ps.setObject(2, value);
						
					ps.execute();
					return null;
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public String removeByKey(Connection conn, Object key)
			{
				try
				{
					PreparedStatement ps = conn.prepareStatement("DELETE FROM "+table+" WHERE `key` = ?");
					ps.setObject(1, key);
					ps.execute();
					return null;
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return null;
				}
			}

			@Override
			public int getMappingCount(Connection conn)
			{
				try
				{
					return JDBC.getInt(conn, "SELECT count(1) FROM "+table);
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
					return -1;
				}
			}

			@Override
			public void emptyOutMapping(Connection conn)
			{
				try
				{
					JDBC.execute(conn, "DELETE FROM "+table);
				}
				catch(Exception e)
				{
					Mirror.throwSoftOrHardButAnyway(e);
				}
			}
		};
	}
	
	
	public static JdbcMapImplProvider<String, String> sqlite(final Connection conn, final String table)
	{
		return fromPrepared(conn, table, "INSERT OR REPLACE INTO "+table+" (`key`, `val`) VALUES (?, ?);");
	}
	
	public static JdbcMapImplProvider<String, String> mysql(final Connection conn, final String table)
	{
		return fromPrepared(conn, table, "REPLACE INTO "+table+" (`key`, `val`) VALUES (?, ?);");
	}

}
