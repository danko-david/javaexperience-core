package eu.javaexperience.database.failsafe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import static eu.javaexperience.log.LoggingTools.*;
import static eu.javaexperience.log.LogLevel.*;


public class FailSafeStatement implements Statement
{
	public FailSafeStatement(JdbcFailSafeConnection conn) throws SQLException
	{
		this.conn = conn;
		recreate();
	}
	
	protected JdbcFailSafeConnection conn;
	
	private Statement _; 
	
	protected void recreate() throws SQLException
	{
		try
		{
			_ = conn.conn.createStatement();
		}
		catch(SQLException e)
		{
			conn.reconnect(e);
			for(int i=1;i<conn.att;i++)
			{
				try
				{
					_ = conn.conn.createStatement();
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
	public boolean execute(String arg0) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, " %s boolean execute(%s)", _.hashCode(), arg0);
		try
		{
			return _.execute(arg0);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				conn.reconnect(e);
				recreate();
				try
				{
					return _.execute(arg0);
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
	public ResultSet executeQuery(String arg0) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, " %s ResultSet execute(%s)", _.hashCode(), arg0);
		try
		{
			return conn.publishClosable(_.executeQuery(arg0));
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				conn.reconnect(e);
				recreate();
				try
				{
					return conn.publishClosable(_.executeQuery(arg0));
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}
	
	/*************************************************************************/
	
	@Override
	public boolean execute(String a, String[] b) throws SQLException
	{
		try
		{
			return _.execute(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.execute(a, b);
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
	public boolean execute(String a, int b) throws SQLException
	{
		try
		{
			return _.execute(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.execute(a, b);
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
	public boolean execute(String a, int[] b) throws SQLException
	{
		try
		{
			return _.execute(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.execute(a, b);
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
	public int executeUpdate(String a, String[] b) throws SQLException
	{
		try
		{
			return _.executeUpdate(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeUpdate(a, b);
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
	public int executeUpdate(String a, int[] b) throws SQLException
	{
		try
		{
			return _.executeUpdate(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeUpdate(a, b);
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
	public int executeUpdate(String a, int b) throws SQLException
	{
		try
		{
			return _.executeUpdate(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeUpdate(a, b);
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
	public int executeUpdate(String a) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, " %s int executeUpdate(%s)", _.hashCode(), a);
		try
		{
			return _.executeUpdate(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeUpdate(a);
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
	public int getMaxFieldSize() throws SQLException
	{
		try
		{
			return _.getMaxFieldSize();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxFieldSize();
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
	public void setMaxFieldSize(int a) throws SQLException
	{
		try
		{
			_.setMaxFieldSize(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setMaxFieldSize(a);
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
	public int getMaxRows() throws SQLException
	{
		try
		{
			return _.getMaxRows();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxRows();
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
	public void setMaxRows(int a) throws SQLException
	{
		try
		{
			_.setMaxRows(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setMaxRows(a);
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
	public void setEscapeProcessing(boolean a) throws SQLException
	{
		try
		{
			_.setEscapeProcessing(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setEscapeProcessing(a);
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
	public int getQueryTimeout() throws SQLException
	{
		try
		{
			return _.getQueryTimeout();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getQueryTimeout();
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
	public void setQueryTimeout(int a) throws SQLException
	{
		try
		{
			_.setQueryTimeout(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setQueryTimeout(a);
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
	public void cancel() throws SQLException
	{
		try
		{
			_.cancel();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.cancel();
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
	public SQLWarning getWarnings() throws SQLException
	{
		try
		{
			return _.getWarnings();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getWarnings();
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
			_.clearWarnings();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.clearWarnings();
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
	public void setCursorName(String a) throws SQLException
	{
		try
		{
			_.setCursorName(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setCursorName(a);
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
	public ResultSet getResultSet() throws SQLException
	{
		try
		{
			return conn.publishClosable(_.getResultSet());
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return conn.publishClosable(_.getResultSet());
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
	public int getUpdateCount() throws SQLException
	{
		try
		{
			return _.getUpdateCount();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getUpdateCount();
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
	public boolean getMoreResults(int a) throws SQLException
	{
		try
		{
			return _.getMoreResults(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMoreResults(a);
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
	public boolean getMoreResults() throws SQLException
	{
		try
		{
			return _.getMoreResults();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMoreResults();
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
	public void setFetchDirection(int a) throws SQLException
	{
		try
		{
			_.setFetchDirection(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setFetchDirection(a);
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
	public int getFetchDirection() throws SQLException
	{
		try
		{
			return _.getFetchDirection();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getFetchDirection();
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
	public void setFetchSize(int a) throws SQLException
	{
		try
		{
			_.setFetchSize(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setFetchSize(a);
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
	public int getFetchSize() throws SQLException
	{
		try
		{
			return _.getFetchSize();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getFetchSize();
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
	public int getResultSetConcurrency() throws SQLException
	{
		try
		{
			return _.getResultSetConcurrency();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getResultSetConcurrency();
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
	public int getResultSetType() throws SQLException
	{
		try
		{
			return _.getResultSetType();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getResultSetType();
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
	public void addBatch(String a) throws SQLException
	{
		try
		{
			_.addBatch(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.addBatch(a);
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
	public void clearBatch() throws SQLException
	{
		try
		{
			_.clearBatch();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.clearBatch();
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
	public int[] executeBatch() throws SQLException
	{
		try
		{
			return _.executeBatch();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeBatch();
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
	public ResultSet getGeneratedKeys() throws SQLException
	{
		try
		{
			return conn.publishClosable(_.getGeneratedKeys());
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return conn.publishClosable(_.getGeneratedKeys());
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
	public int getResultSetHoldability() throws SQLException
	{
		try
		{
			return _.getResultSetHoldability();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getResultSetHoldability();
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
			return _.isClosed();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.isClosed();
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
	public void setPoolable(boolean a) throws SQLException
	{
		try
		{
			_.setPoolable(a);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setPoolable(a);
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
	public boolean isPoolable() throws SQLException
	{
		try
		{
			return _.isPoolable();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.isPoolable();
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
	public void closeOnCompletion() throws SQLException
	{
		try
		{
			_.closeOnCompletion();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.closeOnCompletion();
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
	public boolean isCloseOnCompletion() throws SQLException
	{
		try
		{
			return _.isCloseOnCompletion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.isCloseOnCompletion();
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
	public Connection getConnection() throws SQLException
	{
		try
		{
			return _.getConnection();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getConnection();
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
			return _.isWrapperFor(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.isWrapperFor(a);
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
			return _.unwrap(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.unwrap(a);
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
			_.close();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.close();
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
}