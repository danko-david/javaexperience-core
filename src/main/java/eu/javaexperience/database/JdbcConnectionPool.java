package eu.javaexperience.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.database.failsafe.JdbcFailSafeConnection;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;

public class JdbcConnectionPool implements ConnectionPool
{
	public static final Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("JdbcConnectionPool"));
	
	protected ConnectionCreator cc;
	
	//protected long timeoutMs = 5*60*1000;
	//protected String statemenetForTimeoutCheck = "SELECT DATABASE();";
	public JdbcConnectionPool(ConnectionCreator cc)
	{
		this.cc = cc;
	}	
	
	public JdbcConnectionPool(ConnectionBuilder type,String host,int port,String user, String passwd, String db)
	{
		cc = ConnectionCreator.fromConnectionBuilder(type, host, port, user, passwd, db);
	}	

	public JdbcConnectionPool(ConnectionBuilder type,String user, String passwd, String db)
	{
		this(type,null,-1,user,passwd,db);
	}
	
	public JdbcConnectionPool(ConnectionBuilder type,String host,String user, String passwd, String db)
	{
		this(type,host,-1,user,passwd,db);
		AssertArgument.assertNotNull(host, "host");
	}

	private final ArrayList<ConnectionData> pool = new ArrayList<>();
	
	public synchronized JdbcIssuedConnection getConnection() throws SQLException
	{
		for(ConnectionData d:pool)
		{
			if(d.check())
			{
				LoggingTools.tryLogFormat(LOG, LogLevel.DEBUG, "Issue existing connection: %s", d.conn);
				return new JdbcIssuedConnection(d);
			}
		}
		///vagy új kapcsolat
		ConnectionData ret = new ConnectionData(openNewConnection());
		pool.add(ret);
		LoggingTools.tryLogFormat(LOG, LogLevel.DEBUG, "Issue new connection: %s", ret.conn);
		return new JdbcIssuedConnection(ret);
	}
	
	public class ConnectionData
	{
		public ConnectionData(Connection conn)
		{
			this.conn = conn;
		}
		
		boolean check() throws SQLException
		{
			return free;
		}
		
		boolean free = true;
		Connection conn;
		long lastGet = System.currentTimeMillis();
		
		public boolean isFree()
		{
			return free;
		}
		
		public long getLastGet()
		{
			return lastGet;
		}
	}
	
	protected Connection openNewConnection() throws SQLException
	{
		return new JdbcFailSafeConnection(cc, 3);
	}
}