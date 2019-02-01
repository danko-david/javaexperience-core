package eu.javaexperience.arrays;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.exceptions.UnimplementedMethodException;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.reflect.BidirectionalCaster;

public class ListMapAdapter<K,V> implements List<V>
{
	protected Map<K, V> map;
	protected MapKeyMapper<K> caster;
	
	public static interface MapKeyMapper<T> extends BidirectionalCaster<Integer,T>
	{
		public T getNextKeyValue(Map<T,?> map);
	}
	
	public ListMapAdapter(Map<K, V> map, MapKeyMapper<K> caster)
	{
		AssertArgument.assertNotNull(this.map = map, "map");
		AssertArgument.assertNotNull(this.caster = caster, "caster");
	}
	
	@Override
	public int size()
	{
		return map.size();
	}

	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return map.containsValue(o);
	}

	@Override
	public Iterator<V> iterator()
	{
		return map.values().iterator();
	}

	@Override
	public Object[] toArray()
	{
		return map.values().toArray();
	}

	@Override
	public <T> T[] toArray(T[] a)
	{
		return map.values().toArray(a);
	}

	@Override
	public boolean add(V e)
	{
		K k = caster.getNextKeyValue(map);
		map.put(k, e);
		return true;
	}

	@Override
	public boolean remove(Object o)
	{
		return null != map.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for(Object o:c)
		{
			if(!map.containsValue(o))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends V> c)
	{
		for(V v:c)
		{
			add(v);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends V> c)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		for(Object o:c)
		{
			remove(o);
		}
		
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public void clear()
	{
		map.clear();
	}

	@Override
	public V get(int index)
	{
		K k = caster.cast(index);
		if(null == k)
		{
			return null;
		}
		return map.get(k);
	}

	@Override
	public V set(int index, V element)
	{
		K k = caster.cast(index);
		if(null == k)
		{
			return null;
		}
		return map.put(k, element);
	}

	@Override
	public void add(int index, V element)
	{
		K k = caster.cast(index);
		map.put(k, element);
	}

	@Override
	public V remove(int index)
	{
		K k = caster.cast(index);
		if(null == k)
		{
			return null;
		}
		
		return map.remove(k);
	}

	@Override
	public int indexOf(Object o)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public int lastIndexOf(Object o)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public ListIterator<V> listIterator()
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public ListIterator<V> listIterator(int index)
	{
		throw new UnimplementedMethodException();
	}

	@Override
	public List<V> subList(int fromIndex, int toIndex)
	{
		throw new UnimplementedMethodException();
	}
	
	public static final MapKeyMapper<String> INDEXED_KEYS = new MapKeyMapper<String>()
	{
		@Override
		public String cast(Integer from)
		{
			return from.toString();
		}
		
		@Override
		public Integer castReverse(String from)
		{
			return ParsePrimitive.tryParseInt(from);
		}
		
		@Override
		public String getNextKeyValue(Map<String, ?> map)
		{
			int i = -1;
			for(String s:map.keySet())
			{
				Integer k = ParsePrimitive.tryParseInt(s);
				if(null != k)
				{
					int val = k;
					if(val > i)
					{
						i = val;
					}
				}
			}
			
			return String.valueOf(i+1);
		}
	};
}
