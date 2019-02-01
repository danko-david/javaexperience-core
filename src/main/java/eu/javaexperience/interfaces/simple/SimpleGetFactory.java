package eu.javaexperience.interfaces.simple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.collection.map.SmallMap;

public class SimpleGetFactory
{
	private static final SimpleGet<ArrayList> newArrayList = new SimpleGet<ArrayList>()
	{
		@Override
		public ArrayList get()
		{
			return new ArrayList();
		}
	};
	
	public static SimpleGet<ArrayList> getArrayListFactory()
	{
		return newArrayList;
	}
	
	private static final SimpleGet<Vector<?>> newVector = new SimpleGet<Vector<?>>()
	{
		@Override
		public Vector<?> get()
		{
			return new Vector<>();
		}
	};
	
	public static <T> SimpleGet<Vector<T>> getVectorFactory()
	{
		return (SimpleGet<Vector<T>>)(Object)newVector;
	}
	
	private static final SimpleGet<HashMap<?,?>> newHashMap = new SimpleGet<HashMap<?,?>>()
	{
		@Override
		public HashMap<?,?> get()
		{
			return new HashMap<>();
		}
	};
	
	private static final SimpleGet<ConcurrentHashMap<?,?>> newConcurrentHashMap = new SimpleGet<ConcurrentHashMap<?,?>>()
	{
		@Override
		public ConcurrentHashMap<?,?> get()
		{
			return new ConcurrentHashMap<>();
		}
	};
	
	public static <K,V> SimpleGet<HashMap<K,V>> getHashMapFactory()
	{
		return (SimpleGet<HashMap<K,V>>)(Object) newHashMap;
	}
	
	public static <K,V> SimpleGet<HashMap<K,V>> getConcurrentHashMapFactory()
	{
		return (SimpleGet<HashMap<K,V>>)(Object) newConcurrentHashMap;
	}
	
	public static <T> SimpleGet<T> alwaysReturnWith(final T thisElement)
	{
		return new SimpleGet<T>()
		{
			@Override
			public T get()
			{
				return thisElement;
			}
		};
	}

	public static SimpleGet<HashSet> getHashSetFactory()
	{
		return new SimpleGet<HashSet>()
		{
			@Override
			public HashSet get()
			{
				return new HashSet<>();
			}
		};
	}

	public static SimpleGet<SmallMap> getSmallMapFactory()
	{
		return new SimpleGet<SmallMap>()
		{
			@Override
			public SmallMap get()
			{
				return new SmallMap<>();
			}
		};
	}
	
}