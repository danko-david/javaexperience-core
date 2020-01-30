package eu.javaexperience.database.impl;

import java.sql.Connection;
import java.util.ArrayList;

import eu.javaexperience.database.ConnectionBuilder;
import eu.javaexperience.database.ConnectionCreator;
import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.pojodb.SqlDatabase;
import eu.javaexperience.database.pojodb.dialect.MysqlDialect;

public class MysqlUsualDbModelTest extends UsualDbModelTest
{
	protected static final String user = "user";
	protected static final String password = "password";
	protected static final String db = "test";
	
	@Override
	public SqlDatabase createConnection(boolean resetDatabase) throws Exception
	{
		ConnectionCreator cc = ConnectionCreator.fromConnectionBuilder
		(
			ConnectionBuilder.mysql,
			"127.0.0.1",
			3306,
			user,
			password,
			db
		);

		SqlDatabase ret = new SqlDatabase(cc, new MysqlDialect());
		
		if(resetDatabase)
		{
			try(Connection conn = ret.getConnection())
			{
				ArrayList<String> tables = new ArrayList<>();
				JDBC.listTables(conn, tables);
				for(String s:tables)
				{
					JDBC.execute(conn, "DROP TABLE `"+s+"`");
				}
			}
		}
		
		return ret;
	}
}
