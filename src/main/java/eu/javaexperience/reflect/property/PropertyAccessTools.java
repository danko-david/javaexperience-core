package eu.javaexperience.reflect.property;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.IndexedStorage;
import eu.javaexperience.interfaces.ObjectWithProperty;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.PrimitiveTools;
import eu.javaexperience.regex.RegexTools;

public class PropertyAccessTools
{
	public static Map<String, Object> dotAccessWrap(final Map<String, Object> ret)
	{
		return new Map<String, Object>()
		{
			protected String[] lk = new String[1];
			protected Map[] lm = new Map[1];
					
			@Override
			public int size()
			{
				return ret.size();
			}

			@Override
			public boolean isEmpty()
			{
				return ret.isEmpty();
			}

			@Override
			public boolean containsKey(Object key)
			{
				if(null == key)
				{
					return false;
				}
				
				if(!accessLeaf(lm, lk, ret, String.valueOf(key), false))
				{
					return false;
				}
				
				if(lm[0] instanceof Map)
				{
					return ((Map)lm[0]).containsKey(lk[0]);
				}
				
				return false;
			}

			@Override
			public boolean containsValue(Object value)
			{
				return ret.containsValue(value);
			}

			@Override
			public Object get(Object key)
			{
				if(null == key || "".equals(key) || ".".equals(key))
				{
					return ret;
				}
				
				if(!accessLeaf(lm, lk, ret, String.valueOf(key), false))
				{
					return null;
				}
				
				if(lm[0] instanceof Map)
				{
					return ((Map)lm[0]).get(lk[0]);
				}
				
				return null;
			}

			@Override
			public Object put(String key, Object value)
			{
				if(null == key)
				{
					return false;
				}
				
				if(!accessLeaf(lm, lk, ret, String.valueOf(key), false))
				{
					return false;
				}
				
				if(lm[0] instanceof Map)
				{
					return ((Map)lm[0]).put(lk[0], value);
				}
				
				return false;
			}

			@Override
			public Object remove(Object key)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void putAll(Map<? extends String, ? extends Object> m)
			{
				for(Entry<? extends String, ? extends Object> kv:m.entrySet())
				{
					put(kv.getKey(), kv.getValue());
				}
			}

			@Override
			public void clear()
			{
				ret.clear();
			}

			@Override
			public Set<String> keySet()
			{
				return ret.keySet();
			}

			@Override
			public Collection<Object> values()
			{
				return ret.values();
			}

			@Override
			public Set<Entry<String, Object>> entrySet()
			{
				return ret.entrySet();
			}
		};
	}
	
	public static boolean accessLeaf
	(
		Object[/*1*/] ret_leaf,
		String[/*1*/] ret_leaf_key,
		Map<String, Object> map,
		String path,
		boolean mkpath
	)
	{
		if(null == path || null == map)
		{
			return false;
		}
		
		if(!path.contains("."))
		{
			ret_leaf[0] = map;
			ret_leaf_key[0] = path;
			return true;
		}
		
		String[] p = RegexTools.DOT.split(path);
		Object val = null;
		for(int i=0;i<p.length-1;++i)
		{
			val = map.get(p[i]);
			if(null == val)
			{
				if(!mkpath)
				{
					return false;
				}
				else
				{
					map = new SmallMap<>();
				}
			}
			else
			{
				if(val instanceof Map)
				{
					map = (Map<String, Object>) val;
				}
				else
				{
					//we may access the last
					//if(i != p.length-2)
					{
						return false;
					}
				}
			}
		}
		
		ret_leaf[0] = val;
		ret_leaf_key[0] = p[p.length-1];
		return true;
	}
	
	public static Map<String, Object> wrap(Object o, DataAccessor... accessors)
	{
		return dotAccessWrap
		(
			DataAccessorTools.mixedAccessWrap
			(
				o,
				accessors
			)
		);
	}
	
	public static DataAccessor createExtractorExceptClasses(Class<?>... clss)
	{
		final HashSet<Class> exCls = new HashSet<>();
		CollectionTools.copyInto(clss, exCls);
		
		return createExtractorExceptClasses(new GetBy1<Boolean, Class<?>>()
		{
			@Override
			public Boolean getBy(Class a)
			{
				return !exCls.contains(a);
			}
		});
	}
	
	public static DataAccessor createExtractorExceptClasses(final GetBy1<Boolean, Class<?>> mayExtract)
	{
		return new DataAccessor()
		{
			@Override
			public boolean canHandle(Object subject)
			{
				return !isPrimitiveOrManagedClass(subject.getClass()) && Boolean.TRUE == mayExtract.getBy(subject.getClass());
			}

			@Override
			public Object get(Object subject, String key)
			{
				return EXTRACT_NONPRIMITIVE_OBJECTS.get(subject, key);
			}

			@Override
			public String[] keys(Object subject)
			{
				return EXTRACT_NONPRIMITIVE_OBJECTS.keys(subject);
			}
		};
	}
	
	public static boolean isPrimitiveOrManagedClass(Class cls)
	{
		if(PrimitiveTools.isPrimitiveTypeObject(PrimitiveTools.translatePrimitiveToObjectType(cls)))
		{
			return true;
		}
		
		if
		(
			cls == String.class ||
			cls.isArray() ||
			Map.class.isAssignableFrom(cls) ||
			List.class.isAssignableFrom(cls) ||
			IndexedStorage.class.isAssignableFrom(cls) ||
			ObjectWithProperty.class.isAssignableFrom(cls)
		)
		{
			return true;
		}
		
		return false;
	}
	
	public static DataAccessor EXTRACT_NONPRIMITIVE_OBJECTS = new DataAccessor()
	{
		@Override
		public boolean canHandle(Object subject)
		{
			//always can access even if class has no public field
			return !isPrimitiveOrManagedClass(subject.getClass());
		}

		@Override
		public Object get(Object subject, String key)
		{
			Map<String, Field> acc = WellKnownDataAccessors.getAccessorOfClass(subject.getClass());
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

		/*@Override
		public boolean remove(Object subject, String key)
		{
			return false;
		}*/

		@Override
		public String[] keys(Object subject)
		{
			Map<String, Field> acc = WellKnownDataAccessors.getAccessorOfClass(subject.getClass());
			return acc.keySet().toArray(Mirror.emptyStringArray);
		}
	};
	
	public static Map<String, Object> wrap(Object o)
	{
		return wrap
		(
			o,
			WellKnownDataAccessors.OBJECT_LIKE,
			WellKnownDataAccessors.ARRAY_LIKE,
			WellKnownDataAccessors.ARRAY,
			WellKnownDataAccessors.LIST,
			WellKnownDataAccessors.MAP,
			EXTRACT_NONPRIMITIVE_OBJECTS
		);
	}
	
	public static Map<String, Object> wrapSimplest(Object o)
	{
		return wrap
		(
			o,
			WellKnownDataAccessors.ARRAY_SIMPLEST,
			WellKnownDataAccessors.LIST_SIMPLEST,
			WellKnownDataAccessors.MAP,
			EXTRACT_NONPRIMITIVE_OBJECTS
		);
	}
	
	public static void linearize
	(
		Map<String, Object> dst,
		String prefix,
		Map<String, Object> src
	)
	{
		for(Entry<String, Object> kv:src.entrySet())
		{
			Object o = kv.getValue();
			if(o instanceof Map)
			{
				linearize(dst, prefix+"."+kv.getKey(), (Map)o);
			}
			else
			{
				dst.put(prefix+"."+kv.getKey(), o);
			}
		}
	}
	
	public static void linearize
	(
		Map<String, Object> dst,
		Map<String, Object> src
	)
	{
		for(Entry<String, Object> kv:src.entrySet())
		{
			Object o = kv.getValue();
			if(o instanceof Map)
			{
				linearize(dst, kv.getKey(), (Map)o);
			}
			else
			{
				dst.put(kv.getKey(), o);
			}
		}
	}
	
	public static void linearizeObject(Map<String,Object> dst, Object o)
	{
		linearize(dst, wrap(o));
	}
}
