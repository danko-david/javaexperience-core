package eu.javaexperience.database.impl;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.database.jdbc.Id;
import eu.javaexperience.database.pojodb.Model;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.Mirror.BelongTo;
import eu.javaexperience.reflect.Mirror.FieldSelector;
import eu.javaexperience.reflect.Mirror.Select;
import eu.javaexperience.reflect.Mirror.Visibility;

public class SqlMappingData
{
	public static class FieldData
	{
		protected Field field;
		protected boolean isId;
		
		public FieldData(Field f)
		{
			this.field = f;
			this.isId = null != f.getAnnotation(Id.class);
		}
	}
	
	//TODO model association
	//TODO field association
	
	protected static Map<Class, SqlMappingData> MAPPING = new ConcurrentHashMap<>();
	protected Class cls;
	protected Field[] fields;
	protected Map<String, FieldData> fieldNames = new SmallMap<>();
	protected FieldData id;
	
	protected static Field[] selectFields(Class cls)
	{
		Field[] fs = Mirror.getClassData(cls)
				.selectFields(new FieldSelector(true, Visibility.All, BelongTo.Instance, Select.All, Select.IsNot, Select.All));
		
		for(int i=0;i<fs.length;++i)
		{
			Field f = fs[i];
			if
			(
					null != f.getAnnotation(eu.javaexperience.database.annotations.Ignore.class)
				||
					null != f.getAnnotation(eu.javaexperience.generic.annotations.Ignore.class)
			)
			{
				fs[i] = null;
			}
		}
		
		return ArrayTools.withoutNulls(fs);
	}
	
	public static SqlMappingData getOrCreateMapping(Model m)
	{
		SqlMappingData map = MAPPING.get(m.getClass());
		if(null == map)
		{
			map = new SqlMappingData();
			map.cls = m.getClass();
			map.fields = selectFields(map.cls);
			for(Field f:map.fields)
			{
				FieldData fd = new FieldData(f);
				map.fieldNames.put(f.getName(), fd);
				if(null != f.getAnnotation(Id.class))
				{
					map.id = fd;
				}
			}
			
			MAPPING.put(map.cls, map);
		}
		return map;
	}
	
}