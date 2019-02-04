package eu.javaexperience.datastorage;

import java.sql.Connection;

import eu.javaexperience.database.ConnectionBuilder;
import eu.javaexperience.database.ConnectionCreator;
import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.JdbcConnectionPool;
import eu.javaexperience.database.collection.JdbcMapImplProviders;
import eu.javaexperience.database.collection.JdbcMap.JdbcMapImplProvider;
import eu.javaexperience.datastorage.sql.SqlDataStorage;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class MysqlDataStorageTest extends DataStorageTest
{
	protected static DataStorage getMysqlStorage()
	{
		String table = "test_table";
		JdbcConnectionPool cp = new JdbcConnectionPool(ConnectionCreator.fromConnectionBuilder(ConnectionBuilder.mysql,  "127.0.0.1", 3306, "user", "password", "test"));
		try(Connection c = cp.getConnection())
		{
			if(!JDBC.isTableExists(c, table))
			{
				JdbcMapImplProviders.mysqlCreateKeyValTable(c, table);
			}
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
		}
							
		return new SqlDataStorage
		(
			cp,
			new GetBy1<JdbcMapImplProvider<String,Object>, Connection>()
			{
				@Override
				public JdbcMapImplProvider<String, Object> getBy(Connection conn)
				{
					return (JdbcMapImplProvider) JdbcMapImplProviders.mysql(conn, table);
				}
			}
		);
	}
	
	public MysqlDataStorageTest()
	{
		super(getMysqlStorage());
	}
	
}
