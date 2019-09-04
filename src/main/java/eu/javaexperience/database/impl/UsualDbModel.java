package eu.javaexperience.database.impl;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.jdbc.Id;
import eu.javaexperience.database.pojodb.Model;
import eu.javaexperience.reflect.Mirror;

public abstract class UsualDbModel implements Model
{
	protected static class SqlMappingData
	{
		protected static Map<Class, SqlMappingData> MAPPING = new ConcurrentHashMap<>();
		protected Class cls;
		protected Field[] fields;
		protected Field id;
		
		public static SqlMappingData getOrCreateMapping(Model m)
		{
			SqlMappingData map = MAPPING.get(m.getClass());
			if(null == map)
			{
				map = new SqlMappingData();
				map.cls = m.getClass();
				map.fields = JDBC.simpleSelectClassSqlFileds(map.cls);
				for(Field f:map.fields)
				{
					if(null != f.getAnnotation(Id.class))
					{
						map.id = f;
					}
				}
				
				if(null == map.id)
				{
					for(Field f:map.fields)
					{
						if("id".equals(f.getName()))
						{
							map.id = f;
						}
					}
				}
				
				if(null == map.id)
				{
					throw new RuntimeException("Can't identify the field used as ID. Use a field named as id or use the @Id annotation.");
				}
				
				MAPPING.put(map.cls, map);
			}
			return map;
		}
		
	}
	
	@Override
	public String toString()
	{
		return Mirror.usualToString(this);
	}
	
	@Override
	public abstract String getTable();
	
	
	@Override
	public Field[] getFields()
	{
		return SqlMappingData.getOrCreateMapping(this).fields;
	}

	@Override
	public Field getIdField()
	{
		return SqlMappingData.getOrCreateMapping(this).id;
	}

	public Object getId()
	{
		try
		{
			return getIdField().get(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}