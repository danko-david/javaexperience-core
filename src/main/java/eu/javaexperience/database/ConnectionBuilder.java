package eu.javaexperience.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public enum ConnectionBuilder
{
	mysql("mysql",3306, "?characterEncoding=UTF-8&autoReconnect=true", "com.mysql.jdbc.Driver"),
	postgresql("postgresql",5432, "", "org.postgresql.Driver"),
	sqlite("sqlite",-1, "", "org.sqlite.JDBC"),
	
	;
	private final String name;
	private final int defaultPort;
	private final String urlExtra;
	
	private ConnectionBuilder(String name,int defaultPort, String urlExtra, String... possibleClasses)
	{
		this.name = name;
		this.defaultPort = defaultPort;
		this.urlExtra = urlExtra;
		for(String p:possibleClasses)
			try
			{
				Class.forName(p);
			}
			catch(Exception e)
			{}
	}
	
	public Connection openConnection(String host,String user,String password,String database) throws SQLException
	{
		return openConnection(host, defaultPort, user, password, database);
	}
	
	public Connection openConnection(String user,String password,String database) throws SQLException
	{
		return openConnection("localhost", defaultPort, user, password, database);
	}
	
	public Connection openConnection(String host, int port, String user,String password,String database) throws SQLException
	{
		if(port < 1)
		{
			if(defaultPort < 0)
			{
				return DriverManager.getConnection("jdbc:"+name+":"+database+urlExtra, user, password);
			}
			else
			{
				return DriverManager.getConnection("jdbc:"+name+"://"+database+urlExtra, user, password);
			}
		}
		else
			return DriverManager.getConnection("jdbc:"+name+"://"+host+":"+String.valueOf(port)+"/"+database+urlExtra, user, password);
	}
	
	public Connection openConnection(String host, int port, String user,String password,String database, Properties props) throws SQLException
	{
		//TODO https://code.google.com/archive/p/junixsocket/wikis/ConnectingToMySQL.wiki
		if(port < 1)
			return DriverManager.getConnection("jdbc:"+name+"://"+database+urlExtra, user, password);
		else
			return DriverManager.getConnection("jdbc:"+name+"://"+host+":"+String.valueOf(port)+"/"+database+urlExtra, user, password);
	}
	
	
	public static Connection openConnection(Class<? extends java.sql.Driver> driver,String DBtype, String host,int port,String user,String password, String database) throws SQLException
	{
		driver.getName();
		return DriverManager.getConnection("jdbc:"+DBtype+"://"+host+":"+String.valueOf(port)+"/"+database, user, password);
	}
}
