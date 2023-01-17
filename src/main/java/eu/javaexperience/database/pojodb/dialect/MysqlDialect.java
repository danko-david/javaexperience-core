package eu.javaexperience.database.pojodb.dialect;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;

import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.annotations.AutoIncrement;
import eu.javaexperience.database.annotations.Ignore;
import eu.javaexperience.database.annotations.Indexed;
import eu.javaexperience.database.annotations.Length;
import eu.javaexperience.database.annotations.NotNull;
import eu.javaexperience.database.annotations.Primary;
import eu.javaexperience.database.annotations.Type;
import eu.javaexperience.database.annotations.Unique;
import eu.javaexperience.database.annotations.Unsigned;
import eu.javaexperience.database.jdbc.Id;
import eu.javaexperience.text.Format;

public class MysqlDialect implements SqlDialect
{
	@Override
	public boolean probeDialect(Connection conn)
	{
		try
		{
			{
				String versionString = JDBC.getString(conn, "SELECT @@VERSION");
				versionString = versionString.toLowerCase();
				if(versionString.contains("mysql") || versionString.contains("mariadb"))
				{
					return true;
				}
			}
			
			{
				String versionCommentString = JDBC.getString(conn, "SELECT @@version_comment");
				versionCommentString = versionCommentString.toLowerCase();
				if(versionCommentString.contains("debian")  || versionCommentString.contains("mysql") || versionCommentString.contains("mariadb"))
				{
					return true;
				}
			}
		}
		catch(Exception e)
		{
			return false;
		}
		return false;
	}

	@Override
	public String getSqlType(Field f)
	{
		{
			Ignore i = f.getAnnotation(Ignore.class);
			if(null != i)
			{
				return null;
			}
		}
		
		Class<?> a = f.getType();
		
		boolean notNull = false;
		String type = null;
		
		if(boolean.class.equals(a))
		{
			notNull = true;
			type = "BOOLEAN";
		}
		else if(Boolean.class.equals(a))
		{
			type = "BOOLEAN";
		}
		else if(byte.class.equals(a))
		{
			notNull = true;
			type = "BINARY(1)";
		}
		else if(Byte.class.equals(a))
		{
			type = "BINARY(1)";
		}
		else if(char.class.equals(a))
		{
			notNull = true;
			type = "CHARACTER(1)";
		}
		else if(Character.class.equals(a))
		{
			type = "CHARACTER(1)";
		}
		else if(short.class.equals(a))
		{
			notNull = true;
			type = "SMALLINT";
		}
		else if(Short.class.equals(a))
		{
			type = "SMALLINT";
		}
		else if(int.class.equals(a))
		{
			notNull = true;
			type = "INT";
		}
		else if(Integer.class.equals(a))
		{
			type = "INT";
		}
		else if(float.class.equals(a))
		{
			notNull = true;
			type = "FLOAT";
		}
		else if(Float.class.equals(a))
		{
			type = "FLOAT";
		}
		else if(long.class.equals(a))
		{
			notNull = true;
			type = "BIGINT";
		}
		else if(Long.class.equals(a))
		{
			type = "BIGINT";
		}
		else if(double.class.equals(a))
		{
			notNull = true;
			type = "DOUBLE PRECISION";
		}
		else if(Double.class.equals(a))
		{
			type = "DOUBLE PRECISION";
		}
		else if(Date.class.isAssignableFrom(a))
		{
			type = "TIMESTAMP";
		}
		else if(String.class.equals(a))
		{
			if(null == f.getAnnotation(Length.class))
			{
				type = "TEXT";
			}
			else
			{
				type = "VARCHAR";
			}
		}
		
		{
			Type t = f.getAnnotation(Type.class);
			if(null != t)
			{
				type = t.type();
			}
		}
		
		if(null == type)
		{
			throw new RuntimeException("Not primitive class or Date or String: "+f);
		}
		
		{
			Length l = f.getAnnotation(Length.class);
			if(null != l)
			{
				type += "("+l.length()+")";
			}
		}
		
		
		{
			NotNull nn = f.getAnnotation(NotNull.class);
			if(null != nn)
			{
				notNull = true;
			}
		}
		
		if(notNull  || null != f.getAnnotation(Id.class))
		{
			type += " NOT NULL";
		}
		else
		{
			type += " NULL";
		}
		
		if(null != f.getAnnotation(Primary.class) || null != f.getAnnotation(Id.class))
		{
			type += " PRIMARY KEY";
		}

		if(null != f.getAnnotation(Unique.class) || null != f.getAnnotation(Id.class))
		{
			type += " UNIQUE";
		}

		if(null != a.getAnnotation(AutoIncrement.class) || null != f.getAnnotation(Id.class))
		{
			type += " AUTO_INCREMENT";
		}
		
		if(null != a.getAnnotation(Unsigned.class))
		{
			type += " UNSIGNED";
		}
		
		{
			Indexed i = a.getAnnotation(Indexed.class);
			if(null != i)
			{
				//TODO extra options like lindexing mehtod (bthree/hash)
				type += " INDEX(`"+f.getName()+"`)";
			}
		}
		
		return type;
	}

	@Override
	public String getFieldQuoteString()
	{
		return "`";
	}

	@Override
	public String getStringQuote()
	{
		return "\"";
	}

	@Override
	public String getOtherTableCreateOptions()
	{
		return "\nDEFAULT CHARACTER SET = utf8\nCOLLATE = utf8_bin;";
	}

	@Override
	public void getTableFields(Connection connection, Collection<String> dbf, String table) throws SQLException
	{
		try(Statement st = connection.createStatement())
		{
			try(ResultSet rs = st.executeQuery("SHOW COLUMNS FROM "+getFieldQuoteString()+table+getFieldQuoteString()))
			{
				while(rs.next())
				{
					dbf.add(rs.getString(1));
				}
			}
		}		
	}

	@Override
	public String escapeString(String value)
	{
		if(null == value)
		{
			return "null";
		}
		return value.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	@Override
	public String toQueryString(Object o)
	{
		if(o instanceof Date)
		{
			return Format.sqlTimestamp(((Date)o));
		}
		if(null != o && o.getClass().isEnum())
		{
			return String.valueOf(((Enum)o).ordinal());
		}
		
		return getStringQuote()+escapeString(o.toString())+getStringQuote();
	}
}
