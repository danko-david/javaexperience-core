package eu.javaexperience.database;

import java.lang.reflect.Field;

import eu.javaexperience.database.annotations.AutoIncrement;
import eu.javaexperience.database.annotations.Ignore;
import eu.javaexperience.database.annotations.Indexed;
import eu.javaexperience.database.annotations.Length;
import eu.javaexperience.database.annotations.NotNull;
import eu.javaexperience.database.annotations.Primary;
import eu.javaexperience.database.annotations.Type;
import eu.javaexperience.database.annotations.Unique;
import eu.javaexperience.database.annotations.Unsigned;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class MysqlFieldMapper implements GetBy1<String, Field>
{
	protected GetBy1<String, Field> base = JDBC.generalSqlTypeMapping;
	
	@Override
	public String getBy(Field a)
	{
		String ret = base.getBy(a);
		if(null == ret)
		{
			return null;
		}

		{
			Ignore i = a.getAnnotation(Ignore.class);
			if(null != i)
			{
				return null;
			}
		}
		
		{
			Type t = a.getAnnotation(Type.class);
			if(null != t)
			{
				return t.type();
			}
		}
		
		{
			Length l = a.getAnnotation(Length.class);
			if(null != l)
			{
				ret += "("+l.length()+")";
			}
		}
		
		
		{
			NotNull nn = a.getAnnotation(NotNull.class);
			if(null != nn)
			{
				ret += " NOT NULL";
			}
		}
		
		{
			Primary p = a.getAnnotation(Primary.class);
			if(null != p)
			{
				ret += " PRIMARY KEY";
			}
		}

		{
			Unique p = a.getAnnotation(Unique.class);
			if(null != p)
			{
				ret += " UNIQUE";
			}
		}

		{
			AutoIncrement ai = a.getAnnotation(AutoIncrement.class);
			if(null != ai)
			{
				ret += " AUTO INCREMENT";
			}
		}
		
		{
			Unsigned u = a.getAnnotation(Unsigned.class);
			if(null != u)
			{
				ret += " UNSIGNED";
			}
		}
		
		{
			Indexed i = a.getAnnotation(Indexed.class);
			if(null != i)
			{
				//TODO extra options like lindexing mehtod (bthree/hash)
				ret += " INDEX(`"+a.getName()+"`)";
			}
		}
		
		return ret;
	}
}
