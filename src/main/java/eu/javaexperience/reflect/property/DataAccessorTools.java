package eu.javaexperience.reflect.property;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.collection.map.NullMap;
import eu.javaexperience.collection.set.ArrayListSeemsSet;
import eu.javaexperience.exceptions.UnsupportedMethodException;

public class DataAccessorTools
{
	public static DataAccessor selectAccessor
	(
		Object subject,
		DataAccessor... accessors
	)
	{
		for(DataAccessor a:accessors)
		{
			if(a.canHandle(subject))
			{
				return a;
			}
		}
		
		return null;
	}
	
	public static interface AccessMap extends Map<String, Object>
	{
		public Object getOrigin();
	}

	/**
	 * Object => public fields
	 * Map<String,?> => keys
	 * Array & List => "first", "0", "1", "last", "length"
	 * */
	public static Map<String, Object> mixedAccessWrap
	(
		final Object subject,
		final DataAccessor... accessors
	)
	{
		final DataAccessor acc = selectAccessor(subject, accessors);
		if(null == acc)
		{
			return NullMap.instance;
		}
		
		return new AccessMap()
		{
			protected Object wrapRet(Object ret)
			{
				if(null != ret)
				{
					//return acc.get(subject, k);
					DataAccessor acc = selectAccessor(ret, accessors);
					if(null == acc)
					{
						return ret;
					}
					return mixedAccessWrap(ret, accessors);
				}
				return null;
			}
			
			@Override
			public int size()
			{
				return acc.keys(subject).length;
			}

			@Override
			public boolean isEmpty()
			{
				return 0 != size();
			}

			@Override
			public boolean containsKey(Object key)
			{
				return ArrayTools.contains(acc.keys(subject), key);
			}

			@Override
			public boolean containsValue(Object value)
			{
				return false;
			}

			@Override
			public Object get(Object key)
			{
				return wrapRet(acc.get(subject, String.valueOf(key)));
			}

			@Override
			public Object put(String key, Object value)
			{
				throw new UnsupportedMethodException("put");
			}

			@Override
			public Object remove(Object key)
			{
				throw new UnsupportedMethodException("remove");
			}

			@Override
			public void putAll(Map<? extends String, ? extends Object> m)
			{
				if(true)
				{
					throw new UnsupportedMethodException("putAll");
				}
				for (Entry<? extends String, ? extends Object> kv : m.entrySet())
				{
					put(kv.getKey(), kv.getKey());
				}
			}

			@Override
			public void clear()
			{
				if(true)
				{
					throw new UnsupportedMethodException("clear");
				}
			}

			@Override
			public Set<String> keySet()
			{
				return new ArrayListSeemsSet<>(acc.keys(subject));
			}

			@Override
			public Collection<Object> values()
			{
				throw new UnsupportedMethodException("values");
			}

			@Override
			public Set<Entry<String, Object>> entrySet()
			{
				String[] keys = acc.keys(subject);
				ArrayListSeemsSet<Entry<String, Object>> ret =
					new ArrayListSeemsSet<>();
				
				for (final String k: keys)
				{
					ret.add(new Entry<String, Object>()
					{
						@Override
						public String getKey()
						{
							return k;
						}

						@Override
						public Object getValue()
						{
							return wrapRet(acc.get(subject, k));
						}

						@Override
						public Object setValue(Object value)
						{
							throw new UnsupportedMethodException("setValue");
						}
					});
				}
				
				return ret;
			}
			
			@Override
			public String toString()
			{
				return "DataAccessorTools: "+MapTools.toStringMultiline(entrySet());
			}

			@Override
			public Object getOrigin()
			{
				return subject;
			}
		};
	}
}
