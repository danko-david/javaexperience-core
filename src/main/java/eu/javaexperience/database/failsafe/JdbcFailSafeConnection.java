package eu.javaexperience.database.failsafe;

import static eu.javaexperience.log.LogLevel.DEBUG;
import static eu.javaexperience.log.LoggingTools.*;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import eu.javaexperience.database.ConnectionBuilder;
import eu.javaexperience.database.ConnectionCreator;
import eu.javaexperience.io.IOTools;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;

public class JdbcFailSafeConnection implements Connection
{
	public static Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("SQLDB"));
	
	protected final ConnectionCreator cc;
	
	protected Connection conn;
	protected int att;
	
	protected LinkedList<AutoCloseable> openedz;//= new LinkedList<>();
	
	public JdbcFailSafeConnection(ConnectionBuilder cb,String host, int port, String user, String password, String db, int attempt) throws SQLException
	{
		this(ConnectionCreator.fromConnectionBuilder(cb, host, port, user, password, db), attempt);
	}
	
	public JdbcFailSafeConnection(ConnectionCreator cc, int attempt) throws SQLException
	{
		this.cc = cc;
		reconnect(null);
		this.att = attempt;
	}
	
	protected void reconnect(Throwable t) throws SQLException
	{
		if(null != t)
		{
			tryLogFormatException(JdbcFailSafeConnection.LOG, LogLevel.WARNING, t, "void JdbcFailSafeConnection.reconnect()");
		}
		IOTools.silentClose(conn);
		conn = createNewConnection();
	}
	
	protected Connection createNewConnection() throws SQLException
	{
		return cc.get();
	}

	/**
	 * TODO enable collecton again only if we release resource after it's closed
	 * and properly tested.
	 * */
	<T extends AutoCloseable> T publishClosable(T p)
	{
		if(null != openedz)
		{
			openedz.add(p);
		}
		return p;
	}
	
	@Override
	public Statement createStatement() throws SQLException
	{
		try
		{
			return publishClosable(new FailSafeStatement(this));
		}
		catch(SQLException e)
		{
			for(int i=1;i!=att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(new FailSafeStatement(this));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException
	{
		try
		{
			return new FailSafeDatabaseMetadata(this);
		}
		catch(SQLException e)
		{
			for(int i=1;i!=att;i++)
			{
				reconnect(e);
				try
				{
					return new FailSafeDatabaseMetadata(this);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}

	}
	
	
	/**************************************************************************/
	
	@Override
	public void abort(Executor a) throws SQLException
	{
		try
		{
			conn.abort(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.abort(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Statement createStatement(int a, int b, int c) throws SQLException
	{
		try
		{
			return publishClosable(conn.createStatement(a, b, c));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(conn.createStatement(a, b, c));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Statement createStatement(int a, int b) throws SQLException
	{
		try
		{
			return publishClosable(conn.createStatement(a, b));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(conn.createStatement(a, b));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String a, String[] b) throws SQLException
	{
		try
		{
			return publishClosable(FailSafePreparedStatement.create(this, a, b));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(FailSafePreparedStatement.create(this, a, b));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String a) throws SQLException
	{
		try
		{
			return publishClosable(FailSafePreparedStatement.create(this, a));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(FailSafePreparedStatement.create(this, a));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String a, int b, int c, int d) throws SQLException
	{
		try
		{
			return publishClosable(FailSafePreparedStatement.create(this, a, b, c, d));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(FailSafePreparedStatement.create(this, a, b, c, d));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String a, int b) throws SQLException
	{
		try
		{
			return publishClosable(FailSafePreparedStatement.create(this, a, b));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(FailSafePreparedStatement.create(this, a, b));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String a, int b, int c) throws SQLException
	{
		try
		{
			return publishClosable(FailSafePreparedStatement.create(this, a, b, c));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(FailSafePreparedStatement.create(this, a, b, c));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public PreparedStatement prepareStatement(String a, int[] b) throws SQLException
	{
		try
		{
			return publishClosable(FailSafePreparedStatement.create(this, a, b));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(FailSafePreparedStatement.create(this, a, b));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public CallableStatement prepareCall(String a, int b, int c) throws SQLException
	{
		try
		{
			return publishClosable(conn.prepareCall(a, b, c));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(conn.prepareCall(a, b, c));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public CallableStatement prepareCall(String a, int b, int c, int d) throws SQLException
	{
		try
		{
			return publishClosable(conn.prepareCall(a, b, c, d));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(conn.prepareCall(a, b, c, d));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public CallableStatement prepareCall(String a) throws SQLException
	{
		try
		{
			return publishClosable(conn.prepareCall(a));
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return publishClosable(conn.prepareCall(a));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String nativeSQL(String a) throws SQLException
	{
		try
		{
			return conn.nativeSQL(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.nativeSQL(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setAutoCommit(boolean a) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s void setAutoCommit(%s)", conn.hashCode(), a);
		try
		{
			conn.setAutoCommit(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setAutoCommit(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean getAutoCommit() throws SQLException
	{
		try
		{
			return conn.getAutoCommit();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getAutoCommit();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void commit() throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s void commit()", conn.hashCode());
		try
		{
			conn.commit();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.commit();
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void rollback() throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s void rollback()", conn.hashCode());
		try
		{
			conn.rollback();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.rollback();
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void rollback(Savepoint a) throws SQLException
	{
		try
		{
			conn.rollback(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.rollback(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean isClosed() throws SQLException
	{
		try
		{
			return conn.isClosed();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.isClosed();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setCatalog(String a) throws SQLException
	{
		try
		{
			conn.setCatalog(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setCatalog(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getCatalog() throws SQLException
	{
		try
		{
			return conn.getCatalog();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getCatalog();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setTransactionIsolation(int a) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s void setTransactionIsolation(%s)", conn.hashCode(), a);
		try
		{
			conn.setTransactionIsolation(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setTransactionIsolation(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getTransactionIsolation() throws SQLException
	{
		try
		{
			return conn.getTransactionIsolation();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getTransactionIsolation();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public SQLWarning getWarnings() throws SQLException
	{
		try
		{
			return conn.getWarnings();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getWarnings();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void clearWarnings() throws SQLException
	{
		try
		{
			conn.clearWarnings();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.clearWarnings();
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Map getTypeMap() throws SQLException
	{
		try
		{
			return conn.getTypeMap();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getTypeMap();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setTypeMap(Map a) throws SQLException
	{
		try
		{
			conn.setTypeMap(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setTypeMap(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setHoldability(int a) throws SQLException
	{
		try
		{
			conn.setHoldability(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setHoldability(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getHoldability() throws SQLException
	{
		try
		{
			return conn.getHoldability();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getHoldability();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Savepoint setSavepoint() throws SQLException
	{
		try
		{
			return conn.setSavepoint();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.setSavepoint();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Savepoint setSavepoint(String a) throws SQLException
	{
		try
		{
			return conn.setSavepoint(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.setSavepoint(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void releaseSavepoint(Savepoint a) throws SQLException
	{
		try
		{
			conn.releaseSavepoint(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.releaseSavepoint(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Clob createClob() throws SQLException
	{
		try
		{
			return conn.createClob();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.createClob();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Blob createBlob() throws SQLException
	{
		try
		{
			return conn.createBlob();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.createBlob();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public NClob createNClob() throws SQLException
	{
		try
		{
			return conn.createNClob();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.createNClob();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public SQLXML createSQLXML() throws SQLException
	{
		try
		{
			return conn.createSQLXML();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.createSQLXML();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean isValid(int a) throws SQLException
	{
		try
		{
			return conn.isValid(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.isValid(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setClientInfo(Properties a) throws SQLClientInfoException
	{
		conn.setClientInfo(a);
	}

	@Override
	public void setClientInfo(String a, String b) throws SQLClientInfoException
	{
		conn.setClientInfo(a, b);
	}

	@Override
	public String getClientInfo(String a) throws SQLException
	{
		try
		{
			return conn.getClientInfo(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getClientInfo(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Properties getClientInfo() throws SQLException
	{
		try
		{
			return conn.getClientInfo();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getClientInfo();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Array createArrayOf(String a, Object[] b) throws SQLException
	{
		try
		{
			return conn.createArrayOf(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.createArrayOf(a, b);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Struct createStruct(String a, Object[] b) throws SQLException
	{
		try
		{
			return conn.createStruct(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.createStruct(a, b);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setSchema(String a) throws SQLException
	{
		try
		{
			conn.setSchema(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setSchema(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getSchema() throws SQLException
	{
		try
		{
			return conn.getSchema();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getSchema();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setNetworkTimeout(Executor a, int b) throws SQLException
	{
		try
		{
			conn.setNetworkTimeout(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setNetworkTimeout(a, b);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getNetworkTimeout() throws SQLException
	{
		try
		{
			return conn.getNetworkTimeout();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.getNetworkTimeout();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void setReadOnly(boolean a) throws SQLException
	{
		try
		{
			conn.setReadOnly(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.setReadOnly(a);
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public void close() throws SQLException
	{
		try
		{
			conn.close();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					conn.close();
			return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean isReadOnly() throws SQLException
	{
		try
		{
			return conn.isReadOnly();
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.isReadOnly();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean isWrapperFor(Class a) throws SQLException
	{
		try
		{
			return conn.isWrapperFor(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.isWrapperFor(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Object unwrap(Class a) throws SQLException
	{
		try
		{
			return conn.unwrap(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<att;i++)
			{
				reconnect(e);
				try
				{
					return conn.unwrap(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}
	
	public void closeOpenedResources() throws Exception
	{
		for(Iterator<AutoCloseable> it = openedz.iterator();it.hasNext();)
		{
			it.next().close();
			it.remove();
		}
	}
	
	public void closeOpenedResourcesSilent()
	{
		for(Iterator<AutoCloseable> it = openedz.iterator();it.hasNext();)
		{
			try
			{
				it.next().close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			it.remove();
		}
	}
	
}