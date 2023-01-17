package eu.javaexperience.database.pojodb.dialect;

import java.sql.Connection;

public enum WellKnownSqlDialects
{
	MySql(new MysqlDialect()),
	PostgreSql(null),
	SqLite(new SqliteDialect())
	
	;
	
	private final SqlDialect dialect;
	
	private WellKnownSqlDialects(SqlDialect dial)
	{
		this.dialect = dial;
	}
	
	public SqlDialect getDialectManager()
	{
		return dialect;
	}
	
	public static WellKnownSqlDialects recognise(Connection conn)
	{
		for(WellKnownSqlDialects v:values())
		{
			try
			{
				if(v.dialect.probeDialect(conn))
				{
					return v;
				}
			}
			catch(Exception e)
			{
				
			}
		}
		
		throw new RuntimeException("Can't recognise Sql dialect of connection: "+conn);
	}
}
