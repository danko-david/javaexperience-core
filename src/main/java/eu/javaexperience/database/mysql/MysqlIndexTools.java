package eu.javaexperience.database.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class MysqlIndexTools
{
	public static Map<String, MysqlIndexMap> bulkEnsureIndexes(Connection conn, Collection<TableIndex> indexes, boolean supressErrors) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		Map<String, MysqlIndexMap> all = MysqlIndexMap.loadAllTableIndex(conn);
		for(TableIndex ti:indexes)
		{
			MysqlIndexMap tab = all.get(ti.tableName);
			if(null != tab)
			{
				if(tab.hasIndexNamed(ti.indexName))
				{
					continue;
				}
				try
				{
					ti.createIndex(conn);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					if(!supressErrors)
					{
						throw e;
					}
				}
			}
		}
		return all;
	}
}
