package eu.javaexperience.database;

import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.PrimitiveTools;
import eu.javaexperience.reflect.Mirror.BelongTo;
import eu.javaexperience.reflect.Mirror.FieldSelector;
import eu.javaexperience.reflect.Mirror.Select;
import eu.javaexperience.reflect.Mirror.Visibility;
import eu.javaexperience.struct.GenericStruct1;
import eu.javaexperience.struct.GenericStruct2;
import eu.javaexperience.struct.GenericStruct3;
import eu.javaexperience.struct.GenericStruct4;
import eu.javaexperience.struct.GenericStruct5;
import eu.javaexperience.struct.GenericStruct6;
import eu.javaexperience.struct.GenericStruct7;
import eu.javaexperience.struct.GenericStruct8;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;
import eu.javaexperience.collection.PublisherCollection;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.database.annotations.Length;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//TODO logging facility in debug mode show all the queries!

public class JDBC
{

/************************** Primitive single getters **************************/
	
/******* Int ******/
	public static Integer getInt(Connection conn,String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return rs.getInt(1);
		}
		
		return null;
	}
	
	public static Integer getIntPrepared(Connection conn,String sql, Object... params) throws SQLException
	{
		try
		(
			PreparedStatement st = conn.prepareStatement(sql)
		)
		{
			for(int i=0;i<params.length;++i)
				st.setObject(i+1, params[i]);
			
			try(ResultSet rs = st.executeQuery())
			{
				if(rs.next())
					return rs.getInt(1);
			}
		}
		
		return null;
	}

/******* Long ******/
	public static Long getLong(Connection conn,String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return rs.getLong(1);
		}
		
		return null;
	}
	
	public static Long getLongPrepared(Connection conn,String sql, Object... params) throws SQLException
	{
		try
		(
			PreparedStatement st = conn.prepareStatement(sql)
		)
		{
			for(int i=0;i<params.length;++i)
				st.setObject(i+1, params[i]);
			
			try(ResultSet rs = st.executeQuery())
			{
				if(rs.next())
					return rs.getLong(1);
			}
		}
		
		return null;
	}
	
/******** Double ****/
	public static Double getDouble(Connection conn,String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return rs.getDouble(1);
		}
		
		return null;
	}
	
	
	public static Double getDoublePrepared(Connection conn,String sql, Object... params) throws SQLException
	{
		try
		(
			PreparedStatement st = conn.prepareStatement(sql)
		)
		{
			for(int i=0;i<params.length;++i)
				st.setObject(i+1, params[i]);
			
			try(ResultSet rs = st.executeQuery())
			{
				if(rs.next())
					return rs.getDouble(1);
			}
		}
		
		return null;
	}
	
	
	/**
	 * felsorolás pl.: arr = [1,2,3,4]
	 * 
	 * 	"... WHERE ID IN "+listing(arr)
	 * 
	 * ez lesz belőle
	 * 	"... WHERE ID IN (1,2,3,4)";
	 * 
	 * ügylej hogy ne legyen a lista üres "()", azt nem veszi be az SQL
	 * 
	 * */
	public static <B extends Object,A extends Collection<B>> String listing(A lst)
	{
		StringBuilder sb = new StringBuilder();
		try
		{
			listing(sb, lst);
		}
		catch (IOException e)
		{
			//can't trow
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	public static <B extends Object,A extends Iterable<B>> void listing(Appendable out, A lst) throws IOException
	{
		listing(out, lst, (GetBy1<String, B>) toQueryString);
	}
	
	public static <B extends Object,A extends Iterable<B>> void listing(Appendable out, A lst, GetBy1<String, B> toString) throws IOException
	{
		int i=0;
		out.append("(");
		for(Object o:lst)
		{
			if(++i>1)
			{
				out.append(",");
			}
			
			out.append(toString.getBy((B) o));
		}
		out.append(")");
	}
	
	public static <B extends Object,A extends List<B>> int listingRange
	(
		Appendable out,
		int from_inclusive,
		int to_exclusive,
		A lst
	)
		throws IOException
	{
		out.append("(");
		int max = lst.size();
		int n = 0;
		for(int i=from_inclusive;i < to_exclusive && i < max;++i)
		{
			Object o = lst.get(i); 
			if(i > from_inclusive)
			{
				out.append(",");
			}
			
			out.append(toQueryString(o));
			++n;
		}
		out.append(")");
		return n;
	}
	
	public static final GetBy1<String, Object> toQueryString = new GetBy1<String, Object>()
	{
		@Override
		public String getBy(Object a)
		{
			return toQueryString(a);
		}
	};
	
	private static String toQueryString(Object o)
	{
		if(o instanceof Date)
		{
			return String.valueOf(((Date)o).getTime());
		}
		
		return quote(o);
	}
	
	@Deprecated
	public static String quote(Object str)
	{
		if (null == str)
		{
			return "NULL";
		}
		if(str instanceof String)
		{
			return "\""+str.toString().replaceAll("([\"'])", "\\\\$1")+"\"";
		}
		return StringTools.toString(str).replaceAll("([\"'])", "\\\\$1");
	}
	
	public static void setTransactionIsolationLevelReadUncommitted(Connection conn) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED");
		)
		{}
	}
	
	public static String getString(Connection conn,String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return rs.getString(1);
		}
		
		return null;
	}
	
	public static String getStringPerpared(Connection conn, String sql,Object... args) throws SQLException
	{
		try
		(
			PreparedStatement ps = conn.prepareStatement(sql);
		)
		{
			for(int i=0;i<args.length;++i)
			{
				ps.setObject(i+1, args[i]);
			}
			
			try(ResultSet rs = ps.executeQuery())
			{
				if(rs.next())
				{
					return rs.getString(1);
				}
			}
		}
		
		return null;
	}

	/**
	 * ResultSet legyen a kiolvasandó rekordra állítva
	 * @throws SQLException 
	 * */
	public static void fillIntoMap(ResultSet rs,Map<String,Object> map) throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		for(int i=1;i<=rsmd.getColumnCount();i++)
			map.put(rsmd.getColumnName(i), rs.getObject(i));
	}
	
	/**
	 * ResultSet legyen a kiolvasandó rekordra állítva
	 * @throws SQLException 
	 * */
	public static void fillIntoMap(Connection conn,String sql,Map<String,Object> map) throws SQLException
	{
		try
		(
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(sql);
		)
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i=1;i<=rsmd.getColumnCount();i++)
				map.put(rsmd.getColumnName(i), rs.getObject(i));
		}
	}
	
	public static void genericInsert(Connection conn, String table,Object o, FieldSelector sel, Map<String,Object> map) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		Mirror.fillObjectPublicFieldIntoMap(o, map);
		Object[] vals = new Object[map.size()];
		String q = conn.getMetaData().getIdentifierQuoteString().trim();
		String quote = "".equals(q)?"\"":q;
		
		int ep = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(table);
		sb.append(" (");
		
		for(Entry<String, Object> kv:map.entrySet())
		{
			if(ep > 0)
				sb.append(",");
			
			sb.append(quote);
			sb.append(kv.getKey());
			sb.append(quote);
			
			vals[ep++] = kv.getValue();
		}
		
		sb.append(") VALUES (");
		
		for(int i=0;i<vals.length;i++)
		{
			if(i > 0)
				sb.append(",");
			sb.append("?");
		}		
		sb.append(");");
		try(PreparedStatement pst = conn.prepareStatement(sb.toString()))
		{
			for(int i=0;i< vals.length;i++)
				pst.setObject(i+1, intoSqlType(vals[i]));
				
			pst.execute();
		}
	}
	
	public static void genericUpdate(Connection conn,String table,String keyFieldColumn,Object o,FieldSelector sel,Map<String,Object> map) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		Mirror.fillObjectPublicFieldIntoMap(o, map);
		Object key = map.get(keyFieldColumn);
		if(key == null)
			throw new NullPointerException("Object key value is null!");
		
		Object[] vals = new Object[map.size()];
		int ep = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(table);
		sb.append(" SET ");
		
		for(Entry<String, Object> kv:map.entrySet())
		{
			if(ep > 0)
				sb.append(",");
			sb.append(kv.getKey());
			sb.append("=?");
			
			vals[ep++] = kv.getValue();
		}
		
		sb.append("WHERE ");
		sb.append(keyFieldColumn);
		sb.append("=?);");
		
		try(PreparedStatement pst = conn.prepareStatement(sb.toString()))
		{
			for(int i=0;i< vals.length;i++)
				pst.setObject(i+1, vals[i]);
			
			pst.setObject(vals.length+2, key);
			pst.execute();
		}
	}
	
	public static Object intoSqlType(Object o)
	{
		if(o == null)
			return null;
		
		switch (CastTo.getCasterForTargetClass(o.getClass()))
		{
		case Boolean:
		case Byte:
		case Date:
		case Double:
		case Float:
		case Int:
		case Long:
		case Short:
		case String:
			return o;
			
		case Char:		return o.toString();
		}

		return null;
	}
	
	public static boolean isTableExistsByName(Connection conn, String name) throws SQLException
	{
		DatabaseMetaData dbm = conn.getMetaData();
		try(ResultSet tables = dbm.getTables(null, null, name, null);)
		{
			return tables.next();
		}
	}
	
	public static int listTables(Connection conn, Collection<String> strs) throws SQLException
	{
		DatabaseMetaData dbm = conn.getMetaData();

		int n = 0;
		try(ResultSet tables = dbm.getTables(null, null, "%", null);)
		{
			while(tables.next())
			{
				strs.add(tables.getString(3));
				++n;
			}
		}
		return n;
	}
	
	public static void createTableByClass(Connection conn,String table,Class<?> cls,FieldSelector sel, String keyField, int maxStrLen) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		String q = conn.getMetaData().getIdentifierQuoteString().trim();
		String quote = "".equals(q)?"\"":q;
		
		sb.append("CREATE TABLE ");
		sb.append(quote);
		sb.append(table);
		sb.append(quote);
		sb.append(" (");
		
		Field[] fs = Mirror.getClassData(cls).selectFields(sel);

		int ep = 0;
		
		for(Field f:fs)
		{
			if(ep++ > 0)
				sb.append(",");

			sb.append(quote);
			sb.append(f.getName());
			sb.append(quote);
			
			switch (CastTo.getCasterForTargetClass(f.getType()))
			{
/*				sb.append("VARCHAR(");
				sb.append(create.getStringMaxLength());
				sb.append(")");
	*/			
			/*	sb.append("VARBINARY(");
				sb.append(create.getBlobMaxLength());
				sb.append(")");
				break;
	*/			
			case Boolean:
				sb.append("BOOLEAN");
				break;
				
			case Byte:
				//sb.append("BINARY(1)");
				sb.append("TINYINT");
				break;
				
			case Char:
				sb.append("CHARACTER(1) CHARACTER SET utf8");
				break;
				
			case Long:
				sb.append("BIGINT");
				break;

			case Date:
				sb.append("TIMESTAMP");
				break;

			case Double:
				sb.append("DOUBLE PRECISION");
				break;
				
			case Int:
			//case Enum:
				sb.append("INTEGER");
				break;
				
			case Float:
				sb.append("FLOAT");
				break;
				
			case Short:
				sb.append("SMALLINT");
				break;
				
			case String:
				int len = tryDetermineLength(f, maxStrLen);
				if(len < 256)
				{
					sb.append("VARCHAR(");
					sb.append(len);
					sb.append(") CHARACTER SET utf8");
				}
				else
				{
					sb.append("TEXT CHARACTER SET utf8");
				}
				break;
			}

			if(!PrimitiveTools.isPrimitiveTypeObject(f.getType()))
				sb.append(" NOT NULL");
			
			if(f.getName().equals(keyField))
				sb.append(" AUTO_INCREMENT PRIMARY KEY");
		}
		
		sb.append(")");
		
		sb.append("\nDEFAULT CHARACTER SET = utf8\nCOLLATE = utf8_bin;");
		
		try(Statement st = conn.createStatement())
		{
			st.execute(sb.toString());
		}
	}
	
	public static int tryDetermineLength(Field f, int def)
	{
		Length l = f.getAnnotation(Length.class);
		if(null != l)
		{
			return l.length();
		}
		
		return def;
	}
	
	public static boolean genericSelect(Connection conn,String table,String keyFieldColumn,Object keyValue,Object dst, Map<String,Object> map) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		try(PreparedStatement st = conn.prepareStatement("SELECT * FROM "+table+" WHERE "+keyFieldColumn+"=?;"))
		{
			st.setObject(1, keyValue);
			ResultSet rs = st.executeQuery();
			if(!rs.next())
				return false;
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i=1;i<=rsmd.getColumnCount();i++)
				map.put(rsmd.getColumnName(i), rs.getObject(i));
			
			Mirror.fillMapIntoObject(map, dst);
		}
		return true;
	}
	
	public static boolean genericSelect(Connection conn,String table,String keyFieldColumn,Object keyValue,Object dst) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		return genericSelect(conn, table, keyFieldColumn, keyValue, dst, new HashMap<String,Object>());
	}
	
	public static void genericUpdate(Connection conn,String table,String keyFieldColumn,Object o, FieldSelector sel) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		genericUpdate(conn, table, keyFieldColumn, o, sel, new HashMap<String,Object>());
	}
	
	public static void genericInsert(Connection conn, String table,Object o, FieldSelector sel) throws IllegalArgumentException, IllegalAccessException, SQLException
	{
		genericInsert(conn, table, o, sel, new HashMap<String, Object>());
	}
	
	public static <T> boolean insertInto1(Connection conn, String table, String row1, Collection<T> args) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ? (?) VALUES ");
		for(int i=0;i<args.size();i++)
		{
			if(i != 0)
				sb.append(",");
			sb.append("(?)");
		}	
		
		try(PreparedStatement ps = conn.prepareStatement(sb.toString()))
		{
			int n = 1;
			ps.setObject(n++, table);
			ps.setObject(n++, row1);
			
			for(T e:args)
				ps.setObject(n++, e);
	
			return ps.execute();
		}
	}
	
	public static <A,B> boolean insertInto2(Connection conn, String table, String row1, String row2, Collection<GenericStruct2<A, B>> args) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ? (?,?) VALUES ");
		for(int i=0;i<args.size();i++)
		{
			if(i != 0)
				sb.append(",");
			sb.append("(?,?)");
		}	
		
		try(PreparedStatement ps = conn.prepareStatement(sb.toString()))
		{
			int n = 1;
			ps.setObject(n++, table);
			ps.setObject(n++, row1);
			ps.setObject(n++, row2);
			
			for(GenericStruct2<A, B> e:args)
			{
				ps.setObject(n++, e.a);
				ps.setObject(n++, e.b);
			}
			
			return ps.execute();
		}
	}
	
	public static <A,B,C> boolean insertInto3(Connection conn, String table, String row1, String row2, String row3, Collection<GenericStruct3<A, B, C>> args) throws SQLException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(table);
		sb.append(" (");
		sb.append(row1);
		sb.append(",");
		sb.append(row2);
		sb.append(",");
		sb.append(row3);
		sb.append(") VALUES ");
		for(int i=0;i<args.size();i++)
		{
			if(i != 0)
				sb.append(",");
			sb.append("(?,?,?)");
		}	
		
		try(PreparedStatement ps = conn.prepareStatement(sb.toString()))
		{
			int n = 1;
			/*ps.setObject(n++, table);
			ps.setObject(n++, row1);
			ps.setObject(n++, row2);
			ps.setObject(n++, row3);
			*/
			for(GenericStruct3<A, B, C> e:args)
			{
				ps.setObject(n++, e.a);
				ps.setObject(n++, e.b);
				ps.setObject(n++, e.c);
			}
			
			return ps.execute();
		}
	}	
	
	public static Date getDate(Connection conn,String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return rs.getTimestamp(1);
		}

		return null;
	}
	
	public static Boolean getBool(Connection conn,String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return rs.getBoolean(1);
		}
		
		return null;
	}
	
	public static <T> void getAsList(Connection conn,String sql,Collection<T> lst) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				lst.add((T) rs.getObject(1));
		}
	}
	
	public static <T> void getAsListPrepared(Connection conn, Collection<T> lst, String sql, Object... params) throws SQLException
	{
		try
		(
			PreparedStatement st = conn.prepareStatement(sql);
		)
		{
			for(int i=0;i<params.length;++i)
			{
				st.setObject(i+1, params[i]);
			}
			
			try(ResultSet rs = st.executeQuery())
			{
				while(rs.next())
				{
					lst.add((T) rs.getObject(1));
				}
			}
		}
	}
	
	public static <T> void getAsList(ResultSet rs,Collection<T> lst) throws SQLException
	{
		while(rs.next())
			lst.add((T) rs.getObject(1));
	}

	public static <K,V> void getAsMap(Connection conn,String sql,Map<K,V> map) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
			{
				K k = (K) rs.getObject(1);
				V v = (V) rs.getObject(2);
				map.put(k, v);
			}
		}
	}
	

	public static <K,V> void getAsMapPrepared(Connection conn, Map<K,V> map, String sql, Object... params) throws SQLException
	{
		try
		(
			PreparedStatement st = conn.prepareStatement(sql);
		)
		{
			for(int i=0;i<params.length;++i)
			{
				st.setObject(i+1, params[i]);
			}
		
			try(ResultSet rs = st.executeQuery())
			{
				while(rs.next())
				{
					K k = (K) rs.getObject(1);
					V v = (V) rs.getObject(2);
					map.put(k, v);
				}
			}
		}
	}

	public static void getObjects(Connection conn,String sql,Collection<Map<String,Object>> objs,SimpleGet<Map<String,Object>> mapFactory) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			String[] labs = new String[rsmd.getColumnCount()];
			for(int i=0;i < labs.length;++i)
				labs[i] = rsmd.getColumnName(i+1);
			
			while(rs.next())
			{
				Map<String,Object> obj = mapFactory.get();
				for(String l:labs)
					obj.put(l,rs.getObject(l));
				
				objs.add(obj);
			}
		}
	}

	public static <T> GenericStruct1<T> resolvRow1(ResultSet rs) throws SQLException
	{
		GenericStruct1<T> ret = new GenericStruct1<>();
		ret.a = (T) rs.getObject(1);
		return ret;
	}

	public static <A,B> GenericStruct2<A,B> resolvRow2(ResultSet rs) throws SQLException
	{
		GenericStruct2<A,B> ret = new GenericStruct2<>();
		ret.a = (A) rs.getObject(1);
		ret.b = (B) rs.getObject(2);
		return ret;
	}
	
	public static <A,B,C, R extends GenericStruct3<A,B,C>> R resolvRow3(ResultSet rs) throws SQLException
	{
		R ret = (R) new GenericStruct3<>();
		ret.a = (A) rs.getObject(1);
		ret.b = (B) rs.getObject(2);
		ret.c = (C) rs.getObject(3);
		return ret;
	}

	public static <A,B,C,D> GenericStruct4<A,B,C,D> resolvRow4(ResultSet rs) throws SQLException
	{
		GenericStruct4<A,B,C,D>  ret = new GenericStruct4<>();
		ret.a = (A) rs.getObject(1);
		ret.b = (B) rs.getObject(2);
		ret.c = (C) rs.getObject(3);
		ret.d = (D) rs.getObject(4);
		return ret;
	}

	public static <A,B,C,D,E> GenericStruct5<A,B,C,D,E> resolvRow5(ResultSet rs) throws SQLException
	{
		GenericStruct5<A,B,C,D,E>  ret = new GenericStruct5<>();
		ret.a = (A) rs.getObject(1);
		ret.b = (B) rs.getObject(2);
		ret.c = (C) rs.getObject(3);
		ret.d = (D) rs.getObject(4);
		ret.e = (E) rs.getObject(5);
		return ret;
	}

	public static <A,B,C,D,E,F> GenericStruct6<A,B,C,D,E,F> resolvRow6(ResultSet rs) throws SQLException
	{
		GenericStruct6<A,B,C,D,E,F>  ret = new GenericStruct6<>();
		ret.a = (A) rs.getObject(1);
		ret.b = (B) rs.getObject(2);
		ret.c = (C) rs.getObject(3);
		ret.d = (D) rs.getObject(4);
		ret.e = (E) rs.getObject(5);
		ret.f = (F) rs.getObject(6);
		return ret;
	}
	
	public static <A,B,C,D,E,F,G> GenericStruct7<A,B,C,D,E,F,G> resolvRow7(ResultSet rs) throws SQLException
	{
		GenericStruct7<A,B,C,D,E,F,G>  ret = new GenericStruct7<>();
		ret.a = (A) rs.getObject(1);
		ret.b = (B) rs.getObject(2);
		ret.c = (C) rs.getObject(3);
		ret.d = (D) rs.getObject(4);
		ret.e = (E) rs.getObject(5);
		ret.f = (F) rs.getObject(6);
		ret.g = (G) rs.getObject(7);
		return ret;
	}
	
	public static <A,B,C,D,E,F,G,H> GenericStruct8<A,B,C,D,E,F,G,H> resolvRow8(ResultSet rs) throws SQLException
	{
		GenericStruct8<A,B,C,D,E,F,G,H>  ret = new GenericStruct8<>();
		ret.a = (A) rs.getObject(1);
		ret.b = (B) rs.getObject(2);
		ret.c = (C) rs.getObject(3);
		ret.d = (D) rs.getObject(4);
		ret.e = (E) rs.getObject(5);
		ret.f = (F) rs.getObject(6);
		ret.g = (G) rs.getObject(7);
		ret.h = (H) rs.getObject(8);
		return ret;
	}
	
	public static GenericStruct1<?> getRow1(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return resolvRow1(rs);
			else
				return null;
		}
	}

	public static GenericStruct2<?,?> getRow2(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return resolvRow2(rs);
			else
				return null;
		}
	}

	public static GenericStruct3<?,?,?> getRow3(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return resolvRow3(rs);
			else
				return null;
		}
	}
	
	public static GenericStruct4<?,?,?,?> getRow4(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)		{
			if(rs.next())
				return resolvRow4(rs);
			else
				return null;
		}
	}

	public static GenericStruct5<?,?,?,?,?> getRow5(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)		{
			if(rs.next())
				return resolvRow5(rs);
			else
				return null;
		}
	}
	
	public static GenericStruct6<?,?,?,?,?,?> getRow6(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)		{
			if(rs.next())
				return resolvRow6(rs);
			else
				return null;
		}
	}
	
	public static GenericStruct7<?,?,?,?,?,?,?> getRow7(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return resolvRow7(rs);
			else
				return null;
		}
	}
	
	public static GenericStruct8<?,?,?,?,?,?,?,?> getRow8(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			if(rs.next())
				return resolvRow8(rs);
			else
				return null;
		}
	}
	
	public static <B extends GenericStruct2<?,?>,C extends Collection<B>> void getRows2(Connection conn, String sql,C coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				coll.add((B) resolvRow2(rs));
		}
	}
	
	public static <B extends GenericStruct3<?,?,?>,C extends Collection<B>> void getRows3(Connection conn, String sql,C coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				coll.add((B) resolvRow3(rs));
		}
	}
	
	public static <B extends GenericStruct3<?,?,?>,C extends Collection<B>> void getRows3Prepared(Connection conn, C coll, String sql, Object... params) throws SQLException
	{
		try
		(
			PreparedStatement ps = conn.prepareStatement(sql);
		)
		{
			for(int i=0;i<params.length;++i)
			{
				ps.setObject(i+1, params[i]);
			}
			
			try
			(
				ResultSet rs = ps.executeQuery(sql);
			)
			{
				while(rs.next())
				{
					coll.add((B) resolvRow3(rs));
				}
			}
		}
	}
	
	public static void getRows4(Connection conn, String sql, Collection<GenericStruct4<?,?,?,?>> coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				coll.add(resolvRow4(rs));
		}
	}

	public static void getRows5(Connection conn, String sql,Collection<GenericStruct5<?,?,?,?,?>> coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				coll.add(resolvRow5(rs));
		}
	}
	
	public static void getRows6(Connection conn, String sql,Collection<GenericStruct6<?,?,?,?,?,?>> coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				coll.add(resolvRow6(rs));
		}
	}
	
	public static void getRows7(Connection conn, String sql,Collection<GenericStruct7<?,?,?,?,?,?,?>> coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				coll.add(resolvRow7(rs));
		}
	}
	
	public static void getRows8(Connection conn, String sql,Collection<GenericStruct8<?,?,?,?,?,?,?,?>> coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				coll.add(resolvRow8(rs));
		}
	}
	
	public static void fillRows1(Connection conn, String sql, Collection<GenericStruct1<?>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add(resolvRow1(rs));
		}
	}
	
	public static <A,B> void fillRows2(Connection conn, String sql, Collection<GenericStruct2<A, B>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add((GenericStruct2<A, B>) resolvRow2(rs));
		}
	}

	public static <A,B,C> void fillRows3(Connection conn, String sql, Collection<GenericStruct3<A, B, C>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add((GenericStruct3<A, B, C>) resolvRow3(rs));
		}
	}

	public static <A,B,C,D> void fillRows4(Connection conn, String sql, Collection<GenericStruct4<A, B, C, D>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add((GenericStruct4<A, B, C, D>)resolvRow4(rs));
		}
	}
	
	public static <A,B,C,D,E> void fillRows5(Connection conn, String sql, Collection<GenericStruct5<A,B,C,D,E>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add((GenericStruct5<A,B,C,D,E>)resolvRow5(rs));
		}
	}
	
	public static void fillRows6(Connection conn, String sql, Collection<GenericStruct6<?, ?, ?, ?, ?, ?>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add(resolvRow6(rs));
		}
	}
	
	public static void fillRows7(Connection conn, String sql, Collection<GenericStruct7<?, ?, ?, ?, ?, ?, ?>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add(resolvRow7(rs));
		}
	}
	
	public static void fillRows8(Connection conn, String sql, Collection<GenericStruct8<?, ?, ?, ?, ?, ?, ?, ?>> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
				rows.add(resolvRow8(rs));
		}
	}
	
	
	public static <T> void fetch(Connection conn, String sql, GetBy1<T, ResultSet> fetcher, Collection<T> rows) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
			{
				T ret = fetcher.getBy(rs);
				if(null != ret)
				{
					rows.add(ret);
				}
			}
		}
	}
	
	public static <T> void fetchPrepared(Connection conn, GetBy1<T, ResultSet> fetcher, Collection<T> rows, String sql, Object... params) throws SQLException
	{
		try(PreparedStatement ps = conn.prepareStatement(sql);)
		{
			for(int i=0;i<params.length;++i)
			{
				ps.setObject(i+1, params[i]);
			}
			
			try(ResultSet rs = ps.executeQuery())
			{
				while(rs.next())
				{
					T ret = fetcher.getBy(rs);
					if(null != ret)
					{
						rows.add(ret);
					}
				}
			}
		}
	}
	
	public static boolean hasResult(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			return rs.next();
		}
	}
	

	public static boolean hasResultPrepared(Connection conn, String sql, Object... params) throws SQLException
	{
		try
		(
			PreparedStatement ps = conn.prepareStatement(sql);
		)
		{
			for(int i=0;i<params.length;++i)
				ps.setObject(i+1, params[i]);
			
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}
	
	public static boolean execute(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
		)
		{
			return st.execute(sql);
		}
	}
	
	public static int executeUpdate(Connection conn, String sql) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
		)
		{
			return st.executeUpdate(sql);
		}
	}
	
	public static boolean executePrepared(Connection conn, String sql,Object... params) throws SQLException
	{
		try
		(
			PreparedStatement st = conn.prepareStatement(sql);
		)
		{
			for(int i=0;i<params.length;++i)
			{
				st.setObject(i+1, params[i]);
			}
			
			return st.execute();
		}
	}
	
	/**
	 * http://www.w3schools.com/sql/sql_datatypes_general.asp //általános adattípusok
	 * 
	 * boolean			=>	BOOLEAN
	 * byte				=>	BINARY(1)
	 * char				=>	CHARACTER(1)
	 * short			=>	SMALLINT
	 * int				=>	INTEGER
	 * float			=>	FLOAT
	 * long				=>	BIGINT
	 * double			=>	DOUBLE PRECISION
	 * 
	 * Date				=> long	=>	BIGINT
	 * String			=>	VARCHAR(...?)
	 * SerialObject		=>	VARBINARY(...?)
	 * Storeid(long)	=> long	=> BIGINT	
	 * */
	public static GetBy1<String, Field> generalSqlTypeMapping = new GetBy1<String, Field>()
	{
		
		@Override
		public String getBy(Field f)
		{
			Class<?> a = f.getType();
			if(boolean.class.equals(a))
				return "BOOLEAN NOT NULL";
			if(Boolean.class.equals(a))
				return "BOOLEAN";
			
			if(byte.class.equals(a))
				return "BINARY(1) NOT NULL";
			if(Byte.class.equals(a))
				return "BINARY(1)";
			
			if(char.class.equals(a))
				return "CHARACTER(1) NOT NULL";
			if(Character.class.equals(a))
				return "CHARACTER(1) NOT NULL";
			
			if(short.class.equals(a))
				return "SMALLINT NOT NULL";
			if(Short.class.equals(a))
				return "SMALLINT";
			
			if(int.class.equals(a))
				return "INT NOT NULL";
			if(Integer.class.equals(a))
				return "INT";
			
			if(float.class.equals(a))
				return "FLOAT NOT NULL";
			if(Float.class.equals(a))
				return "FLOAT";
			
			if(long.class.equals(a))
				return "BIGINT NOT NULL";
			if(Long.class.equals(a))
				return "BIGINT";
			
			if(double.class.equals(a))
				return "DOUBLE PRECISION NOT NULL";
			if(Double.class.equals(a))
				return "DOUBLE PRECISION";
			
			if(Date.class.equals(a))
				return "TIMESTAMP";
			
			if(String.class.equals(a))
				return "TEXT";
			
			throw new RuntimeException("Not primitive class or Date or String: "+a);
		}
	};
	
	public static boolean createTableByJavaType(Connection conn, Field[] fields, String tableName,GetBy1<String, Field> typeMapping) throws SQLException
	{
		//final String quote = "\"";
		final String quote = "`";
		
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(quote);
		sb.append(tableName);
		sb.append(quote);
		sb.append(" (");
		
		int i = 0;
		for(Field fd:fields)
		{
			String type = typeMapping.getBy(fd);
			if(null != type)
			{
				if(i++ > 0)
				{
					sb.append(", ");
				}
				
				sb.append(quote);
				sb.append(fd.getName());
				sb.append(quote);
				sb.append(" ");
				sb.append(type);
			}
		}
		
		sb.append(")");
		
		//sb.append("\nDEFAULT CHARACTER SET = utf8\nCOLLATE = utf8_bin;");
		try
		(
				Statement st = conn.createStatement();
		)
		{
			System.out.println(sb.toString());
			return st.execute(sb.toString());
		}
	}
	
	public static boolean isTableExists(Connection conn,String table) throws SQLException
	{
		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		while(rs.next())
			if(table.equals(rs.getString(3)))
				return true;
		
		return false;
	}
	
	
	public static int nextId(Connection conn, String table, String field) throws SQLException
	{
		try(Statement st = conn.createStatement())
		{
			ResultSet rs = st.executeQuery("SELECT max("+field+") FROM "+table);
			if(rs.next())
				return rs.getInt(1)+1;
			
			return 0;
		}
	}
	
	//adatok egyszerűsített beolvasása egy java objektumba
	public static void simpleReadIntoJavaObject(ResultSet rs, Field[] fields,Object o) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		for(Field f: fields)
		{
			Object in = rs.getObject(f.getName());
			CastTo target = CastTo.getCasterForTargetClass(f.getType());
			f.set(o, target.cast(in));
		}
	}
	
	public static boolean simpleInsertIntoTableFromJavaObject(Connection conn, Field[] fields, String table, Object o, Field... except) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		if(null != except && except.length > 0)
		{
			Field[] f = new Field[fields.length];
			int n = 0;
			out:for(Field add: fields)
			{
				for(Field e:except)
				{
					if(add == e)
					{
						continue out;
					}
				}
				
				f[n++] = add;
			}
			
			if(n != fields.length)
			{
				fields = Arrays.copyOf(f, n);
			}
		}
		
		if(0 == fields.length)
		{
			return false;
		}
		
		return null != simpleInsertIntoTableFromJavaObjectResultInsertion(conn, fields, table, o);
	}
	
	public static Map<String, Object> simpleInsertIntoTableFromJavaObjectResultInsertion(Connection conn, Field[] fields, String table, Object o) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(table);
		sb.append(" (");
		
		int nums = 0;
		
		for(int i=0;i<fields.length;++i)
		{
			if(i > 0)
				sb.append(",");
			
			sb.append("`");
			sb.append(fields[i].getName());
			sb.append("`");
			++nums;
		}
		
		sb.append(")VALUES("); 
		
		for(int i=0;i<nums;++i)
		{
			if(i > 0)
				sb.append(",");
			
			sb.append("?");
		}
		
		sb.append(");");
		
		nums = 0;
		try(PreparedStatement ps = conn.prepareStatement(sb.toString()))
		{
			for(int i=0;i<fields.length;++i)
			{
				ps.setObject(++nums, fields[i].get(o));
			}
			
			Map<String, Object> ret = null;
			if(ps.executeUpdate() != 0)
			{
				ret = new SmallMap<>();
				
				try(ResultSet generatedKeys = ps.getGeneratedKeys())
				{
					ResultSetMetaData md = generatedKeys.getMetaData();
					int count = md.getColumnCount();
					for(int i = 1;i <= count;++i)
					{
						ret.put(md.getColumnLabel(i), generatedKeys.getObject(i));
					}
				}
			}
			
			return ret;
		}
	}
	
	/**
	 * where condition: without the "WHERE" keyword just like: id=10;
	 * if where condition is null WHERE keyword will not added, that means UPDATE will be applied on the entire table.
	 * */
	public static void simpleUpdateTableFromJavaObject(Connection conn, Field[] fields, String table, Object o,String whereCondition, Object... params) throws SQLException, IllegalArgumentException, IllegalAccessException
	{
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(table);
		sb.append(" SET ");
		
		for(int i=0;i<fields.length;++i)
		{
			if(i > 0)
				sb.append(", ");
			
			sb.append(fields[i].getName());
			sb.append("=?");
		}
		
		if(whereCondition != null)
		{
			sb.append(" WHERE ");
			sb.append(whereCondition);
		}
		
		try(PreparedStatement ps = conn.prepareStatement(sb.toString()))
		{
			int i=0;
			for(;i<fields.length;++i)
				ps.setObject(i+1, fields[i].get(o));
			
			if(null != params)
			{
				for(int p=0;p<params.length;++p,++i)
				{
					ps.setObject(i+1, params[p]);
				}
			}
			
			ps.execute();
		}
	}
	
	public static Field[] simpleSelectClassSqlFileds(Class<?> cls)
	{
		return Mirror.getClassData(cls)
		.selectFields(new FieldSelector(false, Visibility.All, BelongTo.Instance, Select.All, Select.IsNot, Select.All));
	}
	
	public static void main(String[] args)
	{
		//Field[] fs = simpleSelectClassSqlFileds(ImportArticleList.class);
		//System.out.println(fs);
	}
	
	/**
	 * ret: létre kellett hozni a táblát?
	 * */
	public static boolean createTableIfNonexists(Connection conn,String table, Field[] fields, GetBy1<String, Field> typeMapping) throws SQLException
	{
		if(isTableExists(conn, table))
			return false;
		
		return createTableByJavaType(conn, fields, table, typeMapping);
	}
	
	public static void processAll(Connection conn, String sql, SimplePublish1<ResultSet> pub_rs) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
		)
		{
			while(rs.next())
			{
				pub_rs.publish(rs);
			}
		}
	}
	
	public static void processAllPrepared(Connection conn, SimplePublish1<ResultSet> pub_rs, String sql, Object... args) throws SQLException
	{
		try(PreparedStatement st = conn.prepareStatement(sql))
		{
			for(int i=0;i<args.length;++i)
			{
				st.setObject(i+1, args[i]);
			}
			
			try(ResultSet rs = st.executeQuery())
			{
				while(rs.next())
				{
					pub_rs.publish(rs);
				}
			}
		}
	}

	public static void createIndex(Connection conn, String tableName, String field) throws SQLException
	{
		JDBC.execute(conn, "ALTER TABLE `"+tableName+"` ADD INDEX `"+field+"` (`"+field+"`)");
	}
	
	public static void createCompoundIndex(Connection conn, String tableName, String... fields) throws SQLException
	{
		JDBC.execute(conn, "ALTER TABLE `"+tableName+"` ADD INDEX `compound___"+StringTools.join("__", fields)+"` (`"+StringTools.join("`,`", fields)+"`)");
	}
	
	public static void createUnique(Connection conn, String tableName, String... fields) throws SQLException
	{
		JDBC.execute(conn, "ALTER TABLE `"+tableName+"` ADD CONSTRAINT `ix___"+StringTools.join("__", fields)+"` UNIQUE (`"+StringTools.join("`,`", fields)+"`)");
	}
	
	public static int getNumberOfResults(ResultSet rs) throws SQLException
	{
		int rows = 0;
		if(rs.last())
		{
			rows = rs.getRow();
			rs.beforeFirst();
		}
		return rows;
	}
	
	public static boolean setOffset(ResultSet rs, int offset) throws SQLException
	{
		return rs.absolute(offset);
	}

	public static void bulkInsertPreparedWithValuesTerminatedQuery
	(
		Connection conn,
		String query_with_value_terminated,
		int elements,
		Object... array
	)
		throws SQLException
	{
		if(array.length % elements != 0)
		{
			throw new RuntimeException("Insert parameter length is invalid, reminder: "+(array.length % elements));
		}
		
		int max = array.length /elements;
		StringBuilder sb = new StringBuilder();
		sb.append(query_with_value_terminated);
		for(int i = 0;i<max;++i)
		{
			if(i != 0)
			{
				sb.append(",");
			}
			
			sb.append("(");
			for(int e=0;e<elements;++e)
			{
				if(e != 0)
				{
					sb.append(",");
				}
				sb.append("?");
			}
			
			sb.append(")");
		}
		
		JDBC.executePrepared(conn, sb.toString(), array);
	}
	
	public static Object insertRetFirstGenerated
	(
		Connection conn,
		String query,
		Object... values
	)
		throws SQLException
	{
		try(PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
		{
			for(int i=0;i<values.length;++i)
			{
				ps.setObject(i+1, values[i]);
			}
			
			ps.execute();
			
			try (ResultSet generatedKeys = ps.getGeneratedKeys())
			{
				if (generatedKeys.next())
				{
					return generatedKeys.getObject(1);
				}
				else
				{
					return null;
				}
			}
		}
	}

	public static void commit(Connection conn) throws SQLException
	{
		execute(conn, "commit");
	}

	public static void getAsMapAssocPrepared
	(
		Connection conn,
		String sql,
		SimpleGet<Map<String,Object>> factory,
		Collection<Map<String, Object>> coll,
		Object... args
	)
		throws SQLException
	{
		try
		(
			PreparedStatement ps = conn.prepareStatement(sql);
		)
		{
			for(int i=0;i<args.length;++i)
			{
				ps.setObject(i+1, args[i]);
			}
			
			try(ResultSet rs = ps.executeQuery())
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				String[] names = new String[rsmd.getColumnCount()];

				for(int i=0;i<names.length;++i)
				{
					names[i] = rsmd.getColumnLabel(i+1);
				}
				
				while(rs.next())
				{
					Map<String, Object> add = factory.get();
					for(int i=0;i<names.length;++i)
					{
						add.put(names[i], rs.getObject(i+1));
					}
					coll.add(add);
				}
			}
		}
	}

	public static String[] getAsRows(Connection conn, String sql, Collection<Object[]> coll) throws SQLException
	{
		try
		(
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql)
		)
		{
			ResultSetMetaData rsmd = rs.getMetaData();
			String[] names = new String[rsmd.getColumnCount()];

			for(int i=0;i<names.length;++i)
			{
				names[i] = rsmd.getColumnLabel(i+1);
			}
			
			while(rs.next())
			{
				Object[] row = new Object[names.length];
				for(int i=0;i<names.length;++i)
				{
					row[i] = rs.getObject(i+1);
				}
				coll.add(row);
			}
			
			return names;
		}
	}
	
	public static String[] getAsRowsPrepared(Connection conn, String sql, Collection<Object[]> coll, Object... params) throws SQLException
	{
		try(PreparedStatement ps = conn.prepareStatement(sql))
		{
			for(int i=0;i<params.length;++i)
			{
				ps.setObject(i+1, params[i]);
			}
			
			try(ResultSet rs = ps.executeQuery())
			{
			
				ResultSetMetaData rsmd = rs.getMetaData();
				String[] names = new String[rsmd.getColumnCount()];
	
				for(int i=0;i<names.length;++i)
				{
					names[i] = rsmd.getColumnLabel(i+1);
				}
				
				while(rs.next())
				{
					Object[] row = new Object[names.length];
					for(int i=0;i<names.length;++i)
					{
						row[i] = rs.getObject(i+1);
					}
					coll.add(row);
				}
				
				return names;
			}
		}
	}
	
	public static DbQueryResultTable getAsDataTable(Connection conn, String sql) throws SQLException
	{
		DbQueryResultTable res = new DbQueryResultTable();
		res.columns = JDBC.getAsRows(conn, sql, res.rows);
		return res;
	}
	
	public static DbQueryResultTable getAsDataTablePrepared(Connection conn, String sql, Object... params) throws SQLException
	{
		DbQueryResultTable res = new DbQueryResultTable();
		res.columns = JDBC.getAsRowsPrepared(conn, sql, res.rows, params);
		return res;
	}

	public static boolean isCommitFailed(SQLException e)
	{
        return e.getErrorCode() == 1213;
	}
}