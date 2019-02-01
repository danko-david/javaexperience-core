package eu.javaexperience.database.mysql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.database.JDBC;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.Mirror.BelongTo;
import eu.javaexperience.reflect.Mirror.FieldSelector;
import eu.javaexperience.reflect.Mirror.Select;
import eu.javaexperience.reflect.Mirror.Visibility;

public class MysqlIndexMap
{
	protected static class MysqlIndexRow
	{
		public String Table;
		public Integer Non_unique;
		public String Key_name;
		public Integer Seq_in_index;
		public String Column_name;
		public String Collation;
		public Long Cardinality;
		public String Sub_part;
		public String Packed;
		public Boolean Null;
		public String Index_type;
		public String Comment;
		public String Index_comment;
		
		private static final Field[] SQL_FIELDS = Mirror.getClassData(MysqlIndexRow.class).selectFields(new FieldSelector(false, Visibility.Public, BelongTo.Instance, Select.All, Select.All, Select.All));
	}
	
	
	protected String table;
	
	protected ArrayList<MysqlIndexRow> indexes = null;
	
	public static Map<String, MysqlIndexMap> loadAllTableIndex(Connection conn) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		ArrayList<String> tables = new ArrayList<>();
		JDBC.getAsList(conn, "SHOW TABLES;", tables);
		HashMap<String, MysqlIndexMap> ret = new HashMap<>();
		for(String t:tables)
		{
			ret.put(t, load(conn, t));
		}
		
		return ret;
	}
	
	public static MysqlIndexMap load(Connection conn, String table) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		MysqlIndexMap ret = new MysqlIndexMap();
		
		ret.table = table;
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SHOW INDEXES FROM `"+table+"`;");
		)
		{
			ArrayList<MysqlIndexRow> rows = new ArrayList<>();
			while(rs.next())
			{
				MysqlIndexRow n = new MysqlIndexRow();
				JDBC.simpleReadIntoJavaObject(rs, MysqlIndexRow.SQL_FIELDS, n);
				rows.add(n);
			}
			ret.indexes = rows;
		}
		return ret;
	}

	public boolean hasIndexNamed(String indexName)
	{
		for(MysqlIndexRow row:indexes)
		{
			if(indexName.equals(row.Key_name))
			{
				return true;
			}
		}
		return false;
	}
	
	
	
	
}
