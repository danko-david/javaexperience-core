package eu.javaexperience.database.failsafe;

import static eu.javaexperience.log.LogLevel.DEBUG;
import static eu.javaexperience.log.LoggingTools.tryLogFormat;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class FailSafePreparedStatement implements PreparedStatement
{
	protected JdbcFailSafeConnection conn;
	protected PreparedStatement _;
	
	protected FailSafePreparedStatement(JdbcFailSafeConnection jdbcFailSafeConnection)
	{
		this.conn = jdbcFailSafeConnection;
	}
	
	protected int mode = -1;
	
	public static FailSafePreparedStatement create(JdbcFailSafeConnection conn, String query) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s prepareStatement(%s)", conn.conn.hashCode(), query);
		FailSafePreparedStatement ret = new FailSafePreparedStatement(conn);
		ret.mode = 0;
		ret.a = query;
		ret.recreate();
		return ret;
	}
	
	public static FailSafePreparedStatement create(JdbcFailSafeConnection conn, String query, int b) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s prepareStatement(%s, %d)", conn.conn.hashCode(), query, b);
		FailSafePreparedStatement ret = new FailSafePreparedStatement(conn);
		ret.mode = 1;
		ret.a = query;
		ret.b = b;
		ret.recreate();
		return ret;
	}
	
	public static FailSafePreparedStatement create(JdbcFailSafeConnection conn, String query, int[] b) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s prepareStatement(%s, int[])", conn.conn.hashCode(), query);
		FailSafePreparedStatement ret = new FailSafePreparedStatement(conn);
		ret.mode = 2;
		ret.a = query;
		ret._b = b;
		ret.recreate();
		return ret;
	}
	
	public static FailSafePreparedStatement create(JdbcFailSafeConnection conn, String query, String[] b) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s prepareStatement(%s, String[])", conn.conn.hashCode(), query);
		FailSafePreparedStatement ret = new FailSafePreparedStatement(conn);
		ret.mode = 3;
		ret.a = query;
		ret.s_b = b;
		ret.recreate();
		return ret;
	}
	
	public static FailSafePreparedStatement create(JdbcFailSafeConnection conn, String query, int b, int c) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s prepareStatement(%s, %d, %d)", conn.conn.hashCode(), query, b, c);
		FailSafePreparedStatement ret = new FailSafePreparedStatement(conn);
		ret.mode = 4;
		ret.a = query;
		ret.b = b;
		ret.c = c;
		ret.recreate();
		return ret;
	}
	
	public static FailSafePreparedStatement create(JdbcFailSafeConnection conn, String query, int b, int c, int d) throws SQLException
	{
		tryLogFormat(JdbcFailSafeConnection.LOG, DEBUG, "%s prepareStatement(%s, %d, %d, %d)", conn.conn.hashCode(), query, b, c, d);
		FailSafePreparedStatement ret = new FailSafePreparedStatement(conn);
		ret.mode = 5;
		ret.a = query;
		ret.b = b;
		ret.c = c;
		ret.d = d;
		ret.recreate();
		return ret;
	}
	
	protected String a;
	protected int b;
	protected int[] _b;
	protected String[] s_b;
	protected int c;
	protected int d;
	
	protected PreparedStatement create() throws SQLException
	{
		
		switch (mode)
		{
			case 0:
				return conn.conn.prepareStatement(a);
			case 1:
				return conn.conn.prepareStatement(a, b);
			case 2:
				return conn.conn.prepareStatement(a, _b);
			case 3:
				return conn.conn.prepareStatement(a, s_b);
			case 4:
				return conn.conn.prepareStatement(a, b, c);
			case 5:
				return conn.conn.prepareStatement(a, b, c, d);
			default:
				 throw new RuntimeException("No mode set");
		}
	}
	
	
	protected void recreate() throws SQLException
	{
		try
		{
			_ = create();
		}
		catch(SQLException e)
		{
			conn.reconnect(e);
			for(int i=1;i<conn.att;i++)
			{
				try
				{
					_ = create();
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
	public ResultSet executeQuery() throws SQLException
	{
		try
		{
			return _.executeQuery();

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeQuery();
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
	public int executeUpdate() throws SQLException
	{
		try
		{
			return _.executeUpdate();

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeUpdate();
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
	public void setNull(int a, int b, String c) throws SQLException
	{
		try
		{
			_.setNull(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNull(a, b, c);			return;
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
	public void setNull(int a, int b) throws SQLException
	{
		try
		{
			_.setNull(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNull(a, b);			return;
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
	public void setBigDecimal(int a, BigDecimal b) throws SQLException
	{
		try
		{
			_.setBigDecimal(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBigDecimal(a, b);			return;
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
	public void setString(int a, String b) throws SQLException
	{
		try
		{
			_.setString(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setString(a, b);			return;
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
	public void setBytes(int a, byte[] b) throws SQLException
	{
		try
		{
			_.setBytes(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBytes(a, b);			return;
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
	public void setDate(int a, Date b, Calendar c) throws SQLException
	{
		try
		{
			_.setDate(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setDate(a, b, c);			return;
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
	public void setDate(int a, Date b) throws SQLException
	{
		try
		{
			_.setDate(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setDate(a, b);			return;
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
	public void setAsciiStream(int a, InputStream b, long c) throws SQLException
	{
		try
		{
			_.setAsciiStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setAsciiStream(a, b, c);			return;
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
	public void setAsciiStream(int a, InputStream b) throws SQLException
	{
		try
		{
			_.setAsciiStream(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setAsciiStream(a, b);			return;
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
	public void setAsciiStream(int a, InputStream b, int c) throws SQLException
	{
		try
		{
			_.setAsciiStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setAsciiStream(a, b, c);			return;
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
	@Deprecated
	public void setUnicodeStream(int a, InputStream b, int c) throws SQLException
	{
		try
		{
			_.setUnicodeStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setUnicodeStream(a, b, c);			return;
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
	public void setBinaryStream(int a, InputStream b, long c) throws SQLException
	{
		try
		{
			_.setBinaryStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBinaryStream(a, b, c);			return;
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
	public void setBinaryStream(int a, InputStream b, int c) throws SQLException
	{
		try
		{
			_.setBinaryStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBinaryStream(a, b, c);			return;
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
	public void setBinaryStream(int a, InputStream b) throws SQLException
	{
		try
		{
			_.setBinaryStream(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBinaryStream(a, b);			return;
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
	public void clearParameters() throws SQLException
	{
		try
		{
			_.clearParameters();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.clearParameters();			return;
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
	public void setObject(int a, Object b, int c) throws SQLException
	{
		try
		{
			_.setObject(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setObject(a, b, c);			return;
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
	public void setObject(int a, Object b) throws SQLException
	{
		try
		{
			_.setObject(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setObject(a, b);			return;
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
	public void setObject(int a, Object b, int c, int d) throws SQLException
	{
		try
		{
			_.setObject(a, b, c, d);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setObject(a, b, c, d);			return;
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
	public void addBatch() throws SQLException
	{
		try
		{
			_.addBatch();
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.addBatch();			return;
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
	public void setCharacterStream(int a, Reader b, int c) throws SQLException
	{
		try
		{
			_.setCharacterStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setCharacterStream(a, b, c);			return;
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
	public void setCharacterStream(int a, Reader b) throws SQLException
	{
		try
		{
			_.setCharacterStream(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setCharacterStream(a, b);			return;
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
	public void setCharacterStream(int a, Reader b, long c) throws SQLException
	{
		try
		{
			_.setCharacterStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setCharacterStream(a, b, c);			return;
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
	public void setRef(int a, Ref b) throws SQLException
	{
		try
		{
			_.setRef(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setRef(a, b);			return;
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
	public void setBlob(int a, InputStream b) throws SQLException
	{
		try
		{
			_.setBlob(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBlob(a, b);			return;
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
	public void setBlob(int a, Blob b) throws SQLException
	{
		try
		{
			_.setBlob(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBlob(a, b);			return;
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
	public void setBlob(int a, InputStream b, long c) throws SQLException
	{
		try
		{
			_.setBlob(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBlob(a, b, c);			return;
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
	public void setClob(int a, Reader b) throws SQLException
	{
		try
		{
			_.setClob(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setClob(a, b);			return;
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
	public void setClob(int a, Reader b, long c) throws SQLException
	{
		try
		{
			_.setClob(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setClob(a, b, c);			return;
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
	public void setClob(int a, Clob b) throws SQLException
	{
		try
		{
			_.setClob(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setClob(a, b);			return;
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
	public void setArray(int a, Array b) throws SQLException
	{
		try
		{
			_.setArray(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setArray(a, b);			return;
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
	public ResultSetMetaData getMetaData() throws SQLException
	{
		try
		{
			return _.getMetaData();

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMetaData();
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
	public ParameterMetaData getParameterMetaData() throws SQLException
	{
		try
		{
			return _.getParameterMetaData();

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getParameterMetaData();
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
	public void setRowId(int a, RowId b) throws SQLException
	{
		try
		{
			_.setRowId(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setRowId(a, b);			return;
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
	public void setNString(int a, String b) throws SQLException
	{
		try
		{
			_.setNString(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNString(a, b);			return;
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
	public void setNCharacterStream(int a, Reader b, long c) throws SQLException
	{
		try
		{
			_.setNCharacterStream(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNCharacterStream(a, b, c);			return;
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
	public void setNCharacterStream(int a, Reader b) throws SQLException
	{
		try
		{
			_.setNCharacterStream(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNCharacterStream(a, b);			return;
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
	public void setNClob(int a, Reader b, long c) throws SQLException
	{
		try
		{
			_.setNClob(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNClob(a, b, c);			return;
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
	public void setNClob(int a, Reader b) throws SQLException
	{
		try
		{
			_.setNClob(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNClob(a, b);			return;
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
	public void setNClob(int a, NClob b) throws SQLException
	{
		try
		{
			_.setNClob(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setNClob(a, b);			return;
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
	public void setSQLXML(int a, SQLXML b) throws SQLException
	{
		try
		{
			_.setSQLXML(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setSQLXML(a, b);			return;
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
	public void setBoolean(int a, boolean b) throws SQLException
	{
		try
		{
			_.setBoolean(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setBoolean(a, b);			return;
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
	public void setByte(int a, byte b) throws SQLException
	{
		try
		{
			_.setByte(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setByte(a, b);			return;
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
	public void setShort(int a, short b) throws SQLException
	{
		try
		{
			_.setShort(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setShort(a, b);			return;
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
	public void setInt(int a, int b) throws SQLException
	{
		try
		{
			_.setInt(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setInt(a, b);			return;
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
	public void setLong(int a, long b) throws SQLException
	{
		try
		{
			_.setLong(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setLong(a, b);			return;
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
	public void setFloat(int a, float b) throws SQLException
	{
		try
		{
			_.setFloat(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setFloat(a, b);			return;
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
	public void setDouble(int a, double b) throws SQLException
	{
		try
		{
			_.setDouble(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setDouble(a, b);			return;
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
	public void setTimestamp(int a, Timestamp b, Calendar c) throws SQLException
	{
		try
		{
			_.setTimestamp(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setTimestamp(a, b, c);			return;
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
	public void setTimestamp(int a, Timestamp b) throws SQLException
	{
		try
		{
			_.setTimestamp(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setTimestamp(a, b);			return;
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
	public void setURL(int a, URL b) throws SQLException
	{
		try
		{
			_.setURL(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setURL(a, b);			return;
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
	public void setTime(int a, Time b, Calendar c) throws SQLException
	{
		try
		{
			_.setTime(a, b, c);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setTime(a, b, c);			return;
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
	public void setTime(int a, Time b) throws SQLException
	{
		try
		{
			_.setTime(a, b);
			return;
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					_.setTime(a, b);			return;
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
	public boolean execute() throws SQLException
	{
		try
		{
			return _.execute();

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.execute();
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
	public ResultSet executeQuery(String a) throws SQLException
	{
		try
		{
			return _.executeQuery(a);

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.executeQuery(a);
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
	public int executeUpdate(String a) throws SQLException
	{
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
					_.addBatch(a);			return;
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
					_.setMaxFieldSize(a);			return;
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
					_.setMaxRows(a);			return;
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
					_.setEscapeProcessing(a);			return;
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
					_.setQueryTimeout(a);			return;
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
					_.cancel();			return;
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
					_.clearWarnings();			return;
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
					_.setCursorName(a);			return;
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
			return _.getResultSet();

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getResultSet();
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
					_.setFetchDirection(a);			return;
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
					_.setFetchSize(a);			return;
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
					_.clearBatch();			return;
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
			return _.getGeneratedKeys();

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getGeneratedKeys();
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
					_.setPoolable(a);			return;
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
					_.closeOnCompletion();			return;
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
					_.close();			return;
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
	public boolean execute(String a) throws SQLException
	{
		try
		{
			return _.execute(a);

		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.execute(a);
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
}