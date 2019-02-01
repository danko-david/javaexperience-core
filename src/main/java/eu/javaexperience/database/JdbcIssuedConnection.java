package eu.javaexperience.database;

import java.io.Closeable;
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
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import eu.javaexperience.database.JdbcConnectionPool.ConnectionData;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.LoggingTools;

public class JdbcIssuedConnection implements Closeable, AutoCloseable, Connection
{
	private final Connection origin;
	private final ConnectionData data;
	private boolean closed = false;
	
	Connection getOrigin()
	{
		return origin;
	}
	
	JdbcIssuedConnection(ConnectionData c)
	{
		if(!c.free)
			throw new RuntimeException("Kiadott kapcsolat újra ki lett adva");
		c.lastGet = System.currentTimeMillis();
		c.free = false;
		origin = (data = c).conn;
	}
	
	protected void assertNotClosed() throws SQLException
	{
		if(closed)
			throw new SQLException("IssuedConnection closed");
	}
	
	@Override
	public void close()
	{
		LoggingTools.tryLogFormat(JdbcConnectionPool.LOG, LogLevel.DEBUG, "Close issued connection: %s", origin);
		data.free = closed = true;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException
	{
		assertNotClosed();
		return origin.isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException
	{
		assertNotClosed();
		return origin.unwrap(arg0);
	}

	@Override
	public void abort(Executor arg0) throws SQLException
	{
		assertNotClosed();
		origin.abort(arg0);
	}

	@Override
	public void clearWarnings() throws SQLException
	{
		assertNotClosed();
		origin.clearWarnings();
	}

	@Override
	public void commit() throws SQLException
	{
		assertNotClosed();
		origin.commit();
	}

	@Override
	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException
	{
		assertNotClosed();
		return origin.createArrayOf(arg0, arg1);
	}

	@Override
	public Blob createBlob() throws SQLException
	{
		assertNotClosed();
		return origin.createBlob();
	}

	@Override
	public Clob createClob() throws SQLException
	{
		assertNotClosed();
		return origin.createClob();
	}

	@Override
	public NClob createNClob() throws SQLException
	{
		assertNotClosed();
		return origin.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException
	{
		assertNotClosed();
		return origin.createSQLXML();
	}

	@Override
	public Statement createStatement() throws SQLException
	{
		assertNotClosed();
		return origin.createStatement();
	}

	@Override
	public Statement createStatement(int arg0, int arg1) throws SQLException
	{
		assertNotClosed();
		return origin.createStatement(arg0, arg1);
	}

	@Override
	public Statement createStatement(int arg0, int arg1, int arg2) throws SQLException
	{
		assertNotClosed();
		return origin.createStatement(arg0, arg1);
	}

	@Override
	public Struct createStruct(String arg0, Object[] arg1) throws SQLException
	{
		assertNotClosed();
		return origin.createStruct(arg0, arg1);
	}

	@Override
	public boolean getAutoCommit() throws SQLException
	{
		assertNotClosed();
		return origin.getAutoCommit();
	}

	@Override
	public String getCatalog() throws SQLException
	{
		assertNotClosed();
		return origin.getCatalog();
	}

	@Override
	public Properties getClientInfo() throws SQLException
	{
		assertNotClosed();
		return origin.getClientInfo();
	}

	@Override
	public String getClientInfo(String arg0) throws SQLException
	{
		assertNotClosed();
		return origin.getClientInfo(arg0);
	}

	@Override
	public int getHoldability() throws SQLException
	{
		assertNotClosed();
		return origin.getHoldability();
	}

	@Override
	public DatabaseMetaData getMetaData() throws SQLException
	{
		assertNotClosed();
		return origin.getMetaData();
	}

	@Override
	public int getNetworkTimeout() throws SQLException
	{
		assertNotClosed();
		return origin.getNetworkTimeout();
	}

	@Override
	public String getSchema() throws SQLException
	{
		assertNotClosed();
		return origin.getSchema();
	}

	@Override
	public int getTransactionIsolation() throws SQLException
	{
		assertNotClosed();
		return origin.getTransactionIsolation();
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException
	{
		assertNotClosed();
		return origin.getTypeMap();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException
	{
		assertNotClosed();
		return origin.getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException
	{
		return closed;
	}

	@Override
	public boolean isReadOnly() throws SQLException
	{
		assertNotClosed();
		return origin.isReadOnly();
	}

	@Override
	public boolean isValid(int arg0) throws SQLException
	{
		assertNotClosed();
		return origin.isValid(arg0);
	}

	@Override
	public String nativeSQL(String arg0) throws SQLException
	{
		assertNotClosed();
		return origin.nativeSQL(arg0);
	}

	@Override
	public CallableStatement prepareCall(String arg0) throws SQLException
	{
		assertNotClosed();
		return origin.prepareCall(arg0);
	}

	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2) throws SQLException
	{
		assertNotClosed();
		return origin.prepareCall(arg0, arg1, arg2);
	}

	@Override
	public CallableStatement prepareCall(String arg0, int arg1, int arg2, int arg3) throws SQLException
	{
		assertNotClosed();
		return origin.prepareCall(arg0, arg1, arg2, arg3);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0) throws SQLException
	{
		assertNotClosed();
		return origin.prepareStatement(arg0);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1) throws SQLException
	{
		assertNotClosed();
		return origin.prepareStatement(arg0, arg1);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int[] arg1) throws SQLException
	{
		assertNotClosed();
		return origin.prepareStatement(arg0,arg1);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, String[] arg1) throws SQLException
	{
		assertNotClosed();
		return origin.prepareStatement(arg0, arg1);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2) throws SQLException
	{
		assertNotClosed();
		return origin.prepareStatement(arg0,arg1,arg2);
	}

	@Override
	public PreparedStatement prepareStatement(String arg0, int arg1, int arg2, int arg3) throws SQLException
	{
		assertNotClosed();
		return origin.prepareStatement(arg0,arg1,arg2,arg3);
	}

	@Override
	public void releaseSavepoint(Savepoint arg0) throws SQLException
	{
		assertNotClosed();
		origin.releaseSavepoint(arg0);
	}

	@Override
	public void rollback() throws SQLException
	{
		assertNotClosed();
		origin.rollback();	
	}

	@Override
	public void rollback(Savepoint arg0) throws SQLException
	{
		assertNotClosed();
		origin.rollback(arg0);		
	}

	@Override
	public void setAutoCommit(boolean arg0) throws SQLException
	{
		assertNotClosed();
		origin.setAutoCommit(arg0);
	}

	@Override
	public void setCatalog(String arg0) throws SQLException
	{
		assertNotClosed();
		origin.setCatalog(arg0);
	}

	@Override
	public void setClientInfo(Properties arg0) throws SQLClientInfoException
	{
		if(closed)
			throw new SQLClientInfoException();
		origin.setClientInfo(arg0);		
	}

	@Override
	public void setClientInfo(String arg0, String arg1) throws SQLClientInfoException
	{
		if(closed)
			throw new SQLClientInfoException();
		origin.setClientInfo(arg0, arg1);		
	}

	@Override
	public void setHoldability(int arg0) throws SQLException
	{
		assertNotClosed();
		origin.setHoldability(arg0);
	}

	@Override
	public void setNetworkTimeout(Executor arg0, int arg1) throws SQLException
	{
		assertNotClosed();
		origin.setNetworkTimeout(arg0, arg1);
	}

	@Override
	public void setReadOnly(boolean arg0) throws SQLException
	{
		assertNotClosed();
		origin.setReadOnly(arg0);
	}

	@Override
	public Savepoint setSavepoint() throws SQLException
	{
		assertNotClosed();
		return origin.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String arg0) throws SQLException
	{
		assertNotClosed();
		return origin.setSavepoint(arg0);
	}

	@Override
	public void setSchema(String arg0) throws SQLException
	{
		assertNotClosed();
		origin.setSchema(arg0);
	}

	@Override
	public void setTransactionIsolation(int arg0) throws SQLException
	{
		assertNotClosed();
		origin.setTransactionIsolation(arg0);		
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> arg0) throws SQLException
	{
		assertNotClosed();
		origin.setTypeMap(arg0);		
	}
}