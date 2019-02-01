package eu.javaexperience.database.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import eu.javaexperience.database.JDBC;

public class TableIndex
{
	public String tableName;
	public String indexName;
	public ArrayList<String> fields = new ArrayList<>();
	
	public TableIndex(String table, String indexName, String... fields)
	{
		this.tableName = table;
		this.indexName = indexName;
		for(String f:fields)
		{
			this.fields.add(f);
		}
	}
	
	public void createIndex(Connection conn) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("ALTER TABLE `");
		sb.append(tableName);
		sb.append("` ADD INDEX `");
		sb.append(indexName);
		sb.append("` (");
		int n = 0;
		for(String field:fields)
		{
			if(0 != n)
			{
				sb.append(",");
			}
			sb.append("`");
			sb.append(field);
			sb.append("`");
			++n;
		}
		sb.append(")");
		
		System.out.println(sb.toString());
		JDBC.execute(conn, sb.toString());
	}
	
	public void ensureIndexExists(Connection conn) throws SQLException
	{
		if(!isIndexExists(conn))
		{
			createIndex(conn);
		}
	}
	
	public boolean isIndexExists(Connection conn) throws SQLException
	{
		try(Statement st = conn.createStatement())
		{
			ResultSet rs = st.executeQuery("SHOW INDEXES FROM `"+tableName+"` WHERE Key_name=\""+indexName+"\";");
			return rs.next();
		}
	}

	public void dropIndex(Connection conn) throws SQLException
	{
		JDBC.execute(conn, "ALTER TABLE `"+tableName+"` DROP INDEX `"+indexName+"`;");
	}
}