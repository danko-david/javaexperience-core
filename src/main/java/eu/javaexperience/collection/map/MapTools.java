package eu.javaexperience.collection.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.interfaces.simple.SimpleGetFactory;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class MapTools
{
	public static String toString(Map<?,?> map)
	{
		if(map.size() == 0)
			return "{}";
		
		StringBuilder sb = new StringBuilder();

		sb.append("{");
		
		for(Entry<?,?> kv:map.entrySet())
		{
			if(sb.length()> 1)
				sb.append(", ");
			
			sb.append("\"");
			sb.append(kv.getKey());
			sb.append("\":\"");
			sb.append(kv.getValue());
			sb.append("\"");
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
	public static <K,V> String toStringMultiline(Map<K, V> map)
	{
		if(map.size() == 0)
			return "{}";
		
		return toStringMultiline(map.entrySet());
	}
	
	public static <K,V> String toStringMultiline(Set<Entry<K, V>> entrySet)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		
		for(Entry<K, V> kv:entrySet)
		{
			sb.append("\t\"");
			sb.append(kv.getKey());
			sb.append("\":\"");
			
			Object val = kv.getValue();
			if(val == null)
			{
				sb.append("null");
			}
			else if(val.getClass().isArray() && Object.class.isAssignableFrom(val.getClass().getComponentType()))
			{
				sb.append(Arrays.toString((Object[])kv.getValue()));
			}
			else
			{
				sb.append(kv.getValue());
			}
			
			sb.append("\"\n");
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
	
	public static <K,T> T getIfType(Map<K,?> map,K key,Class<T> type)
	{
		Object o = map.get(key);
		return type.isAssignableFrom(type)?(T)o:null;
	}
	
	public static <K extends Comparable<K>> K highestKey(Map<K,?> map)
	{
		K ret = null;
		for(Entry<K, ?> kv:map.entrySet())
		{
			K k = kv.getKey();
			if(k == null)
				continue;
			
			if(ret == null)
				ret = k;
			else if(ret.compareTo(k) < 0)
				ret = k;
		}
		
		return ret;
	}

	public static <K extends Comparable<K>> K lowsetKey(Map<K,?> map)
	{
		K ret = null;
		for(Entry<K, ?> kv:map.entrySet())
		{
			K k = kv.getKey();
			if(k == null)
				continue;
			
			if(ret == null)
				ret = k;
			else if(ret.compareTo(k) > 0)
				ret = k;
		}
		
		return ret;
	}

	private static Comparator<Entry<? extends Comparable, ?>> comparatorByKey = new Comparator<Entry<? extends Comparable,?>>()
	{
		@Override
		public int compare(Entry<? extends Comparable, ?> arg0, Entry<? extends Comparable, ?> arg1)
		{
			return arg0.getKey().compareTo(arg1.getKey());
		}		
	};
	
	private static Comparator<Entry<?, ? extends Comparable>> comparatorByVal = new Comparator<Entry<?,?  extends Comparable>>()
	{
		@Override
		public int compare(Entry<?,? extends Comparable> arg0, Entry<?,? extends Comparable> arg1)
		{
			return arg0.getValue().compareTo(arg1.getValue());
		}		
	};
	
	public static <K extends Comparable<K>,V> List<Entry<K,V>> sortByKeys(Map<K,V> map)
	{
		List<Entry<K, V>> ret = new ArrayList<>();
		for(Entry<K,V> kv : map.entrySet())
			ret.add(kv);

		//nyers típus rulz!
		Collections.sort(ret,((Comparator) comparatorByKey));
			
		return ret;
	}
	
	public static <I,V> void index(Map<I,? extends Collection<V>> index, Collection<V> source, SimpleGet store, GetBy1<I, V> get)
	{
		for(V k:source)
		{
			I i = get.getBy(k);
			Collection dst = index.get(i);
			if(dst == null)
			{
				dst = (Collection) store.get();
				((Map)index).put(i, (Collection) dst);
			}
			dst.add(k);
		}
	}
	
	public static <K,V> void indirectIndex(Map<K,V> index,Collection<K> coll,GetBy1<V, K> getby)
	{
		for(K k:coll)
		{
			V v = getby.getBy(k);
			index.put(k, v);
		}
	}
	
	public static <K,V> void extractedIndex(Map<K,V> index,Collection<V> coll,GetBy1<K, V> getby)
	{
		for(V v:coll)
		{
			K k = getby.getBy(v);
			index.put(k, v);
		}
	}
	

	public static <K,V> void extractedIndexFirstOnly(Map<K,V> index,Collection<V> coll,GetBy1<K, V> getby)
	{
		for(V v:coll)
		{
			K k = getby.getBy(v);
			if(!index.containsKey(k))
			{
				index.put(k, v);
			}
		}
	}
	
	public static <S,K,V> void indirectIndex(Map<K,V> index,Collection<S> coll,GetBy1<K, S> getKeyBy, GetBy1<V, S> getValueBy)
	{
		for(S s:coll)
		{
			K k = getKeyBy.getBy(s);
			V v = getValueBy.getBy(s);
			index.put(k, v);
		}
	}
	
	public static <K, V extends Comparable<V>> List<Entry<K,V>> sortByValues(Map<K,V> map, final Comparator<V> cmp)
	{
		return  sortByValues(map, cmp, false);
	}
	
	public static <K, V extends Comparable<V>> List<Entry<K,V>> sortByValues(Map<K,V> map, final Comparator<V> cmp, final boolean rev)
	{
		List<Entry<K, V>> ret = new ArrayList<>();
		for(Entry<K,V> kv : map.entrySet())
		{
			ret.add(kv);
		}
		
		//nyers típus rulz!
		Collections.sort(ret, (Comparator) new Comparator<V>()
		{
			@Override
			public int compare(V o1, V o2)
			{
				return cmp.compare(o1, o2)*(rev?-1:1);
			}
		});
			
		return ret;
	}
	
	public static <K extends Comparable<K>,V> List<Entry<K,V>> sortByKeys(Map<K,V> map, final Comparator<K> cmp)
	{
		List<Entry<K, V>> ret = new ArrayList<>();
		for(Entry<K,V> kv : map.entrySet())
			ret.add(kv);

		//nyers típus rulz!
		Collections.sort(ret,((Comparator) new Comparator<Entry<K,?>>()
		{
			@Override
			public int compare(Entry<K, ?> o1, Entry<K, ?> o2)
			{
				return cmp.compare(o1.getKey(), o2.getKey());
			}
		}));
			
		return ret;
	}
	
	public static <K, V extends Comparable<V>> List<Entry<K,V>> sortByValues(Map<K,V> map, boolean rev)
	{
		return sortByValues(map,  (Comparator) comparatorByVal, rev);
	}
	
	public static <K, V extends Comparable<V>> List<Entry<K,V>> sortByValues(Map<K,V> map)
	{
		return sortByValues(map, false);
	}
	
	public static <K,V,T extends Map<K,V>> T inlinePut(T a, K k, V v)
	{
		a.put(k, v);
		return a;
	}

	public static <K,V,T extends Map<K,V>> T inlinePut(T a, Object... args)
	{
		for(int i=0;i<args.length;i+=2)
		{
			a.put((K)args[i], (V) args[i+1]);
		}
		return a;
	}
	
	public static <K,V,T extends Map<K,V>> T inlineSmallMap(Object... args)
	{
		T a = (T) new SmallMap<>();
		for(int i=0;i<args.length;i+=2)
		{
			a.put((K)args[i], (V) args[i+1]);
		}
		return a;
	}
	
	public static <K,V,T extends Map<K,V>> T inlineHashMap(Object... args)
	{
		T a = (T) new HashMap<>();
		for(int i=0;i<args.length;i+=2)
		{
			a.put((K)args[i], (V) args[i+1]);
		}
		return a;
	}
	
	public static <K,V> void putAll(Map<? extends K, ? extends V> source, Map<K,V> destination)
	{
		for(Entry<? extends K, ? extends V> kv:source.entrySet())
		{
			destination.put(kv.getKey(), kv.getValue());
		}
	}
	
	public static <K,V> Map<K, V> entriesAsMap(Map<K,V> map, Collection<Entry<K, V>> dst_kv_url)
	{
		for(Entry<K, V> kv:dst_kv_url)
		{
			map.put(kv.getKey(), kv.getValue());
		}
		
		return map;
	}
	
	public static <R,P> R getOrCreate
	(
		Map<P,R> map,
		P key,
		GetBy1<R,P> factory
	)
	{
		R ret = map.get(key);
		if(null == ret)
		{
			R cre = factory.getBy(key);
			if(null == cre)
			{
				return null;
			}
			
			map.put(key, cre);
			return cre;
		}
		
		return ret;
	}

	public static <K> Map ensureMapInMap
	(
		Map<K, ?> map,
		K key,
		SimpleGet factory
	)
	{
		Map ret =  (Map) map.get(key);
		if(null == ret)
		{
			ret = (Map) factory.get();
			((Map)map).put(key, ret);
		}
		return ret;
	}
	
	public static <K, V> Map<K, V> ensureSmallMapKey
	(
		Map<K, V> map,
		K key
	)
	{
		return (Map<K, V>) MapTools.ensureMapInMap(map, key, SimpleGetFactory.getSmallMapFactory());
	}
	
	public static <T> Map<String, T> firstValues(Map<String, T[]> map)
	{
		Map<String, T> ret = new SmallMap<>();
		for(Entry<String, T[]> kv:map.entrySet())
		{
			T[] in = kv.getValue();
			if(null != in && 0 != in.length)
			{
				ret.put(kv.getKey(), in[0]);
			}
		}
		return ret;
	}

	public static <K,V> Map<K, V> inlineFill(Map<K, V> map, Object... key_val)
	{
		for(int i=0;i< key_val.length;i+=2)
		{
			map.put((K)key_val[i], (V)key_val[i+1]);
		}
		return map;
	}

	public static <T> Map<T, Integer> population(Collection<T> c)
	{
		HashMap<T, Integer> ret = new HashMap<>();
		for(T t:c)
		{
			Integer nums = ret.get(t);
			if(null == nums)
			{
				nums = 1;
			}
			else
			{
				++nums;
			}
			ret.put(t, nums);
		}
		return ret;
	}

	public static <K,V> V ensureKey
	(
		Map<K, V> map,
		K key,
		SimpleGet<V> init
	)
	{
		V ret = map.get(key);
		if(ret == null)
			map.put(key,ret = init.get());

		return ret;
	}

	public static <K, V> Map<V, K> reverse(Map<K, V> from, Map<V, K> dst)
	{
		for(Entry<K, V> kv:from.entrySet())
		{
			dst.put(kv.getValue(), kv.getKey());
		}
		
		return dst;
	}

	public static <T> void incrementCount(Map<T, Integer> counts, T item)
	{
		Integer in = counts.get(item);
		if(null == in)
		{
			in = 1;
		}
		else
		{
			in = in+1;
		}
		counts.put(item, in);
	}

	public static <K, V> void extractListOfMapsTo
	(
		Map<V, V> dst,
		Collection<Map<K, V>> from,
		K asKey,
		K asValue
	)
	{
		for(Map<K, V> p:from)
		{
			dst.put(p.get(asKey), p.get(asValue));
		}
	}
}