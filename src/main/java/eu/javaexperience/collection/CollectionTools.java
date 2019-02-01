package eu.javaexperience.collection;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.collection.map.KeyVal;
import eu.javaexperience.collection.map.PublisherMap;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;
import eu.javaexperience.semantic.references.MayNull;

public class CollectionTools
{
	public static <T> void mutualDeleteSection(Set<T> a, Set<T> b)
	{
		ArrayList<T> less = new ArrayList<>();

		Collection<T> more = a.size()>b.size()?a:b;
		less.addAll(a==more?b:a);
		
		for(T v:less)
			if(more.contains(v))
			{
				a.remove(v);
				b.remove(v);
			}
	}
	
	public static <T> boolean containsIdentical(Collection<T> coll, T elem)
	{
		for(T e:coll)
			if(e == elem)
				return true;
		
		return false;
	}
	

	
	private static enum asd
	{
		asd,
		fg,
		fdg,
		fgh,
	}
	
	/*public static void main(String[] args)
	{
		EnumSet<asd> en = EnumSet.noneOf(asd.class);
		en.add(asd.fgh);
		en.add(asd.fg);
		System.out.println(en);
		
		Number n = enumSetBits(asd.class, en);
		System.out.println(n);
		
		EnumSet<asd> re = enumSetBits(asd.class, n);
		
		System.out.println(re);
		
	}*/

	public static String toString(Collection<?> coll)
	{
		if(coll == null)
			return "null";
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Object o:coll)
		{
			if(sb.length() > 1)
				sb.append(",");
			sb.append(o);
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	public static String toStringMultiline(Collection<?> coll)
	{
		if(coll == null)
			return "null";
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Object o:coll)
		{
			sb.append(o);
			sb.append(",\n");
		}
		sb.append("]");
		return sb.toString();
	}
	
	public static <T,U extends Collection<T>,R extends Collection<U>> R shard(Collection<T> coll, int num_per_unit, SimpleGet<U> fact, SimpleGet<R> retType)
	{
		R ret = retType.get();
		int n = 0;
		U crnt = fact.get();
		ret.add(crnt);
		for(T e: coll)
			if(++n >  num_per_unit)
			{
				crnt = fact.get();
				ret.add(crnt);
				crnt.add(e);
				n = 0;
			}
			else
				crnt.add(e);

		return ret;
	}
	
	public static <T> T getMaxOccurrenceElementByEquals(Collection<T> coll)
	{
		Map<T,Integer> map = getElementOccurrenceByEquals(coll);
		T ret = null;
		int max = 0;
		
		for(Entry<T, Integer> kv:map.entrySet())
			if(kv.getValue() > max)
			{
				max = kv.getValue();
				ret = kv.getKey();
			}

		return ret;
	}
	
	public static <T> void removeNulls(Collection<T> coll)
	{
		Iterator<T> it = coll.iterator();
		while(it.hasNext())
			if(it.next() == null)
				it.remove();
	}
	
	public static boolean containsNull(Collection<?> coll)
	{
		for(Object e:coll)
			if(e == null)
				return true;
		
		return false;
	}
	
	public static <T> Map<T,Integer> getElementOccurrenceByEquals(Collection<T> coll)
	{
		Map<T,Integer> ret = new HashMap<>();
		for(T e:coll)
		{
			Integer i = ret.get(e);
			i = i==null?1:++i;
			ret.put(e, i);
		}
		return ret;
	}
	
	public static <T,D extends Collection<T>> D copyInto(Collection<T> src, D dst)
	{
		for(T a:src)
		{
			dst.add(a);
		}
		
		return dst;
	}
	
	public static <D extends Collection<T>, T> D copyInto(T[] src, D dst)
	{
		for(T a:src)
			dst.add(a);
		
		return dst;
	}
	
	public static <T> void copyReverseInto(T[] src, Collection<T> dst)
	{
		for(int i=src.length-1;i>=0;--i)
		{
			dst.add(src[i]);
		}
	}
	
	public static <T> boolean addLikeSet(Collection<T> coll, T element)
	{
		if(!coll.contains(element))
		{
			return coll.add(element);
		}
		return false;
	}
	
	public static <T> T tryGetFirst(Collection<T> coll)
	{
		if(null == coll)
		{
			return null;
		}
		
		for(T c:coll)
		{
			return c;
		}
		
		return null;
	}
	
	//Sub-case of tryGetFirst(Collection), but it doesn't need to create an iterator
	public static <T> T tryGetFirst(List<T> coll)
	{
		if(null == coll)
		{
			return null;
		}
		
		if(coll.isEmpty())
		{
			return null;
		}
		
		return coll.get(0);
	}
	
	public static <C, T> Collection<T> extractTo(Collection<T> dst, Collection<? extends C> src, GetBy1<T, C> extractor)
	{
		for(C c: src)
		{
			dst.add(extractor.getBy(c));
		}
		
		return dst;
	}
	
	/**
	 * if `maybe_collection` is a collection, then add the subject to it,
	 * else create a new one add add both to.
	 * if it's null, it will __not__ be added to the collection  
	 * 
	 * */
	public static <T> Collection<T> addOrWrap
	(
		Object maybe_collection,
		SimpleGet<Collection<T>> create,
		T subject
	)
	{
		if(maybe_collection instanceof Collection)
		{
			((Collection) maybe_collection).add(subject);
			return (Collection) maybe_collection;
		}
		else
		{
			Collection<T> c = create.get();
			if(null != maybe_collection)
			{
				c.add((T) maybe_collection);
			}
			if(null != subject)
			{
				c.add(subject);
			}
			return c;
		}
	}

	public static <E, C extends Collection<E>> C inlineAdd
	(
		C coll,
		E... elem
	)
	{
		for(E e:elem)
		{
			coll.add(e);
		}
		return coll;
	}
	
	public static <E, C extends Collection<E>> C inlineAdd
	(
		C coll,
		Collection<E> elem
	)
	{
		for(E e:elem)
		{
			coll.add(e);
		}
		return coll;
	}
	
	public static <K,V> Map<K, V> mapLikeEntryIntoCollection(final Collection<Entry<K, V>> dst)
	{
		return new PublisherMap<K, V>()
		{
			@Override
			public V put(K key, V value)
			{
				dst.add(new KeyVal<K, V>(key, value));
				return null;
			}

			@Override
			public void putAll(Map<? extends K, ? extends V> m)
			{
				for(java.util.Map.Entry<? extends K, ? extends V> kv:m.entrySet())
				{
					dst.add((java.util.Map.Entry<K, V>) kv);
				}
			}
		};
	}
	
	public static <SRC,C extends Collection<T>, T> C executeWith
	(
		SimpleGet<? extends C> carrier_creator,
		SimplePublish2<? extends C, ? extends SRC> transformer,
		SRC src
	)
	{
		C ret = carrier_creator.get();
		((SimplePublish2)transformer).publish(ret, src);
		return ret;
	}

	public static <T> T randomItem(List<T> coll)
	{
		return coll.get((int) (Math.random()*coll.size()));
	}

	public static <T> boolean containsExternal
	(
		Collection<T> coll,
		T key,
		GetBy2<Boolean, T, T> eq
	)
	{
		for(T t:coll)
		{
			if(Boolean.TRUE == eq.getBy(key, t))
			{
				return true;
			}
		}
		return false;
	}

	public static <T> List<T> tryWrapToList(Object object, String name)
	{
		if(object instanceof List)
		{
			return (List<T>) object;
		}
		else if(object instanceof Collection)
		{
			ArrayList<T> ret = new ArrayList<>();
			copyInto((Collection)object, ret);
			return ret;
		}
		else if(object.getClass().isArray())
		{
			ArrayList<T> ret = new ArrayList<>();
			int len = Array.getLength(object);
			for(int i=0;i<len;++i)
			{
				ret.add((T)Array.get(object, i));
			}
			
			return ret;
		}
		
		throw new RuntimeException(name+" is not a collection or array");
	}

	public static <S, D, R extends Collection<D>, G extends Collection<S>> R transaformInto
	(
		R dst,
		G from,
		GetBy1<D, S> converter
	)
	{
		for(S f:from)
		{
			dst.add(converter.getBy(f));
		}
		
		return dst;
	}

	public static <T> String toString
	(
		Collection<T> elems,
		@MayNull String glue,
		GetBy1<String, T> elemAppend
	)
	{
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(T e:elems)
		{
			if(null != glue && ++i > 1)
			{
				sb.append(glue);
			}
			sb.append(elemAppend.getBy(e));
		}
		return sb.toString();
	}
	
	public static <S, D> D[] convert(Class<D> dst, Collection<S> src, GetBy1<D, S> converter)
	{
		D[] ret = (D[]) Array.newInstance(dst, src.size());
		int i = 0;
		for(S s:src)
		{
			ret[i++] = converter.getBy(s);
		}
		
		return ret;
	}
	
	public static <C extends Collection<D>, S, D> C convert(C dst, Collection<S> src, GetBy1<D, S> converter)
	{
		for(S s:src)
		{
			dst.add(converter.getBy(s));
		}
		return dst;
	}
	
	public static <T> void reverse(List<T> list, int fromInclusive, int toInclusive)
	{
		while(fromInclusive < toInclusive)
		{
			Collections.swap(list, fromInclusive++, toInclusive--);
		}
	}
	
	/**
	 * https://codeforces.com/blog/entry/3980
	 **/
	public static <C extends Comparable<C>> boolean nextPermutation(List<C> c)
	{
		// 1. finds the largest k, that c[k] < c[k+1]
		int first = -1;
		
		for(int i=c.size()-2; i>= 0;--i)
		{
			if(c.get(i).compareTo(c.get(i + 1)) < 0 )
			{
				first = i;
				break;
			}
		}
		
		if(first == -1)
		{
			return false; // no greater permutation
		}
		
		// 2. find last index toSwap, that c[k] < c[toSwap]
		int toSwap = c.size() - 1;
		while(c.get(first).compareTo(c.get(toSwap)) >= 0)
		{
			--toSwap;
		}
		
		// 3. swap elements with indexes first and last
		Collections.swap( c, first++, toSwap );
		
		// 4. reverse sequence from k+1 to n (inclusive) 
		toSwap = c.size() - 1;
		while ( first < toSwap )
		{
			Collections.swap(c, first++, toSwap--);
		}
		return true;
	}

	public static void main(String[] args)
	{
		List<Integer> arr = Arrays.asList(0,1,2, 4);
		System.out.println(arr);
		while(nextPermutation(arr))
		{
			System.out.println(arr);
		}
	}

	public static <T> ArrayList<T> inlineArrayList(T... elems)
	{
		return inlineAdd(new ArrayList<>(), elems);
	}

	/*public static <T> void indexOfExternalEquals
	(
		List<T> list,
		T key,
		GetBy2<Boolean, T,T> eq
	)
	{
		for(int i=0;i<list.size();++i)
		{
			
		}
	}
	
	public static <T> void replaceExternalEquals
	(
		List<T> list,
		T key,
		GetBy2<Boolean, T,T> eq
	)
	{
		for(T t:coll)
		{
			if(Boolean.TRUE == eq.getBy(key, t))
			{
				return true;
			}
		}
		return false;
	}*/
}