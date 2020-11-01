package eu.javaexperience.reflect.property;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.interfaces.IndexedStorage;
import eu.javaexperience.interfaces.ObjectWithProperty;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.reflect.Mirror;

public enum WellKnownDataAccessors implements DataAccessor
{
	MAP()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			return subject instanceof Map;
		}
		
		@Override
		public Object get(Object subject, String key)
		{
			return ((Map) subject).get(key);
		}
		
		@Override
		public String[] keys(Object subject)
		{
			return ((Map<String,?> ) subject).keySet()
				.toArray(Mirror.emptyStringArray);
		}
	},
	
	
	ARRAY()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			return subject.getClass().isArray();
		}

		@Override
		public Object get(Object subject, String key)
		{
			if(null == key)
			{
				return null;
			}
			
			int len = Array.getLength(subject);
			
			if("length".equals(key))
			{
				return len;
			}
			
			if(0 == len)
			{
				return null;
			}
			
			if("first".equals(key))
			{
				return Array.get(subject, 0);
			}
			
			if("last".equals(key))
			{
				return Array.get(subject, len-1);
			}
			
			int index = ParsePrimitive.tryParseInt(key, -1);
			if(-1 < index && index < len)
			{
				return Array.get(subject, index);
			}
			return null;
		}
		
		@Override
		public String[] keys(Object subject)
		{
			return generateArrayKeys(Array.getLength(subject), true);
		}
		
	},
	
	LIST()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			return subject instanceof List;
		}

		@Override
		public Object get(Object subject, String key)
		{
			List s = (List) subject;
			
			int len = s.size();
			
			if("length".equals(key))
			{
				return len;
			}
			
			if(0 == len)
			{
				return null;
			}
			
			if("first".equals(key))
			{
				return s.get(0);
			}
			
			if("last".equals(key))
			{
				return s.get(len-1);
			}
			
			int index = ParsePrimitive.tryParseInt(key, -1);
			if(-1 < index && index < len)
			{
				return s.get(index);
			}
			
			return null;
		}
		
		@Override
		public String[] keys(Object subject)
		{
			return generateArrayKeys(((List) subject).size(), true);
		}
		
	},
	
	OBJECT_PUBLIC_FIELDS()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			//always can access even if class has no public field
			return true;
		}

		@Override
		public Object get(Object subject, String key)
		{
			Map<String, Field> acc = getAccessorOfClass(subject.getClass());
			Field f = acc.get(key);
			if(null != f)
			{
				try
				{
					return f.get(subject);
				}
				catch (Exception e)
				{
					Mirror.propagateAnyway(e);
				}
			}
			
			return null;
		}
		
		@Override
		public String[] keys(Object subject)
		{
			Map<String, Field> acc = getAccessorOfClass(subject.getClass());
			return acc.keySet().toArray(Mirror.emptyStringArray);
		}
	},
	
	ARRAY_SIMPLEST()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			return subject.getClass().isArray();
		}

		@Override
		public Object get(Object subject, String key)
		{
			if(null == key)
			{
				return null;
			}
			
			int len = Array.getLength(subject);
			
			int index = ParsePrimitive.tryParseInt(key, -1);
			if(-1 < index && index < len)
			{
				return Array.get(subject, index);
			}
			return null;
		}
		
		@Override
		public String[] keys(Object subject)
		{
			return generateArrayKeys(Array.getLength(subject), false);
		}
		
	},
	
	LIST_SIMPLEST()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			return subject instanceof List;
		}

		@Override
		public Object get(Object subject, String key)
		{
			List s = (List) subject;
			
			int len = s.size();
			
			int index = ParsePrimitive.tryParseInt(key, -1);
			if(-1 < index && index < len)
			{
				return s.get(index);
			}
			
			return null;
		}
		
		@Override
		public String[] keys(Object subject)
		{
			return generateArrayKeys(((List) subject).size(), false);
		}
		
	},
	
	OBJECT_LIKE()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			return subject instanceof ObjectWithProperty;
		}

		@Override
		public Object get(Object subject, String key)
		{
			return ((ObjectWithProperty) subject).get(key);
		}
		
		@Override
		public String[] keys(Object subject)
		{
			return ((ObjectWithProperty) subject).keys();
		}
	},
	
	
	ARRAY_LIKE()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			return subject instanceof IndexedStorage;
		}

		@Override
		public Object get(Object subject, String key)
		{
			return ((IndexedStorage) subject).get(Integer.parseInt(key));
		}
		
		@Override
		public String[] keys(Object subject)
		{
			return generateArrayKeys(((IndexedStorage) subject).size(), false);
		}
	},
	
	;
	
	protected static ConcurrentMap<Class, Map<String,Field>>
	classAccessors = new ConcurrentHashMap<Class, Map<String,Field>>();
	
	public static Map<String, Field> getAccessorOfClass(Class cls)
	{
		Map<String, Field> ret = classAccessors.get(cls);
		
		if(null == ret)
		{
			Field[] fs = Mirror.collectClassFields(cls, true);
			ret = new HashMap<>();
			for (Field f : fs)
			{
				f.setAccessible(true);
				ret.put(f.getName(), f);
			}
			
			ret = Collections.unmodifiableMap(ret);
			
			Map<String, Field> in = classAccessors.put(cls, ret);
			if(null != in)
			{
				ret = in;
			}
		}
		
		return ret;
	}
	
	protected static String[] generateArrayKeys(int len, boolean withExtra)
	{
		if(0 == len)
		{
			return Mirror.emptyStringArray;
		}
		
		String[] ret = new String[len+(withExtra?3:0)];
		
		for(int i=0;i<len;++i)
		{
			ret[i] = String.valueOf(i); 
		}
		
		if(withExtra)
		{
			ret[len] = "first";
			ret[len+1] = "last";
			ret[len+2] = "length";
		}
		return ret;
	}
	
}
