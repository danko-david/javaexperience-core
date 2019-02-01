package eu.javaexperience.collection.enumerations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.list.NullList;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.CastTo;

public class EnumTools
{
	public static <E extends Enum<?>> boolean isSet(int num, E elem)
	{
		int bit = 0x1 << elem.ordinal();
		return (num & bit) == bit;
	}
	
	public static <E extends Enum<?>> boolean isSet(int num, int check)
	{
		return (num & check) == check;
	}

	public static <E extends Enum<?>> boolean isAllSet(int num, E... elems)
	{
		int bit = 0;
		for(E e:elems)
		{
			bit |= 0x1 << e.ordinal();
		}
		return (num & bit) == bit;
	}
	
	public static <E extends Enum<?>> int setBit(int num, E elem)
	{
		return num | 0x1 << elem.ordinal();
	}
	
	public static <E extends Enum<?>> int resetBit(int num, E elem)
	{
		return num & ~(0x1 << elem.ordinal());
	}
	
	public static <E extends Enum<?>> int setBitValue(int num, E elem, boolean set)
	{
		if(set)
		{
			return setBit(num, elem);
		}
		else
		{
			return resetBit(num, elem);
		}
	}
	
	public static <E extends Enum<?>> E getRandomEnum(Class<E> cls)
	{
		E[] es = cls.getEnumConstants();
		return es[(int)(Math.random()*es.length-1)];
	}
	
	
	public static <E extends Enum<?>> int assembleLeastOne(E elem, E... values)
	{
		int ret = 0x1 << elem.ordinal();
		
		for(E i:values)
		{
			ret |= 1 << i.ordinal();
		}
		return ret;
	}
	
	public static <E extends Enum<?>> int assemble(E... values)
	{
		int ret = 0x0;
		
		for(E i:values)
		{
			ret |= 1 << i.ordinal();
		}
		return ret;
	}
	
	public static <E extends Enum<E>> String[] getNames(Class<E> cls)
	{
		Enum[] e = cls.getEnumConstants();
		String[] ret = new String[e.length];
		for(int i=0;i<e.length;++i)
		{
			ret[i] = e[i].name();
		}
		
		return ret;
	}
	
	public static <E extends Enum<E>> void getNames(Collection<String> dst, Class<E> cls)
	{
		for(Enum e:cls.getEnumConstants())
		{
			dst.add(e.name());
		}
	}

	public static <E extends Enum<E>> Enum<E> getByName(Class<E> cls, String str)
	{
		E[] ret = cls.getEnumConstants();
		if(null != ret)
		{
			for(E e:ret)
			{
				if(e.name().equals(str))
				{
					return e;
				}
			}
		}
		return null;
	}
	
	public static final SimpleFixedEnumManager EMPTY_ENUM_MANAGER = new SimpleFixedEnumManager<>(NullList.instance);
	
	public static <E extends Enum<E>, L extends EnumLike<L>> EnumManager<L> createFromEnumClass(final Class<E> cls)
	{
		List<L> values = new ArrayList<>();
		final HashMap<String, L> byName = new HashMap<>();
		
		final EnumManager<L>[] BIND_LATER = new EnumManager[1];
		
		for(E val:cls.getEnumConstants())
		{
			final E c = val;
			EnumLike<L> instance = new EnumLike<L>()
			{
				@Override
				public boolean isRegistered()
				{
					return true;
				}

				@Override
				public String getName()
				{
					return c.name();
				}

				@Override
				public int getOrdinal()
				{
					return c.ordinal();
				}

				@Override
				public EnumManager<L> getEnumManager()
				{
					return BIND_LATER[0];
				}

				@Override
				public void setEnumManager(EnumManager<L> mngr)
				{
					throw new UnsupportedOperationException();
				}

				@Override
				public void setOrdinal(int oridinal)
				{
					throw new UnsupportedOperationException();
				}
			}; 
			
			byName.put(val.name(), (L) instance);
			values.add((L) instance);
		}
		
		final List<L> cnst = Collections.unmodifiableList(values);
		
		return new EnumManager<L>()
		{
			{
				BIND_LATER[0] = this;
			}
			
			@Override
			public Iterator<L> iterator()
			{
				return cnst.iterator();
			}

			@Override
			public void registerElement(L elem)
			{
				throw new UnsupportedOperationException();
			}

			@Override
			public L getByOrdinal(int ord)
			{
				return cnst.get(ord);
			}

			@Override
			public L getByName(String name)
			{
				return byName.get(name);
			}

			@Override
			public Object[] getValues()
			{
				return cnst.toArray();
			}

			@Override
			public L[] getValues(L[] arr)
			{
				return cnst.toArray(arr);
			}

			@Override
			public List<L> getValueList()
			{
				return cnst;
			}
			
			@Override
			public String toString()
			{
				return "EnumManager for enum class: "+cls;
			}
		};
	}
	
	public static <E extends EnumLike<E>>/*<E extends Enum<E> & EnumLike<E>>*/ EnumManager<E> createFromCompatibleEnumClass(final Class<E> cls)
	{
		return new SimpleFixedEnumManager<E>(CollectionTools.inlineAdd(new ArrayList<E>(), cls.getEnumConstants()));
	}
	
	public static <E extends EnumLike<E>> EnumManager<E> createFromEnumLikes(E... elems)
	{
		SimpleEnumManager<E> ret = new SimpleEnumManager<E>();
		for(E e:elems)
		{
			ret.registerElement(e);
		}
		
		return ret;
	}
	
	public static <C> C recogniseSymbol(Class<C> enumClass, Object ident)
	{
		if(null == ident)
		{
			return null;
		}
		
		Integer NUM = (Integer) CastTo.Int.cast(ident);
		int id = -1;
		if(null != NUM)
		{
			id = NUM;
		}

		String str = (String) CastTo.String.cast(ident);
		
		for(Object o:enumClass.getEnumConstants())
		{
			Enum ee = (Enum) o;
			if(id == ee.ordinal() || str.equals(ee.name()))
			{
				return (C) ee;
			}
		}
		
		return null;
	}
	
	public static <E extends Enum> void recogniseSymbols
	(
		Class<E> enumClass,
		Collection<String> keys,
		Collection<E> dst
	)
	{
		for(String s:keys)
		{
			E val = recogniseSymbol(enumClass, s);
			if(null != val)
			{
				dst.add(val);
			}
		}
	}
	
	public static <T extends Enum<T>> GetBy1<T, Object> createParserFunction(final Class<T> enumClass)
	{
		return new GetBy1<T, Object>()
		{
			@Override
			public T getBy(Object a)
			{
				return recogniseSymbol(enumClass, a);
			}
		};
	}
	
	public static <E extends Enum, M extends Map<String, E>> M mapByName(M dst, Class<E> enum_)
	{
		for(E e:enum_.getEnumConstants())
		{
			dst.put(e.name(), e);
		}
		
		return dst;
	}
	
	/**
	 * ret:
	 * 	len = (enum).values().length 
	 * 
	 * 	len > 64
	 * 		{@link BigInteger}
	 * 	len > 32  
	 * 		{@link long} 
	 * 	else
	 * 		{@link int}
	 * */
	public static <T extends Enum<T>> Number enumSetBits(Class<T> cls, EnumSet<T> enumSet)
	{
		Enum[] vals = cls.getEnumConstants();
		if(vals.length > 64)
		{
			BigInteger in = BigInteger.ZERO;
			for(Enum e:enumSet)
				in = in.setBit(e.ordinal());
			return in;
		}
		
		if(vals.length > 32)
		{
			long ret = 0x0;
			for(Enum e:enumSet)
				ret |= 1 << e.ordinal();
			
			return ret;
		}
		
		int ret = 0x0;
		for(Enum e:enumSet)
			ret |= 1 << e.ordinal();
		
		return ret;
	
	}

	public static <T extends Enum<T>> EnumSet<T> enumSetBits(Class<T> cls,Number num)
	{
		EnumSet<T> ret = EnumSet.noneOf(cls);
		T[] vals = cls.getEnumConstants();

		if(num instanceof BigInteger)
		{
			BigInteger in = (BigInteger) num;
			for(T e:vals)
				if(in.testBit(e.ordinal()))
					ret.add(e);
			
			return ret;
		}
		
		long val = num.longValue();
	
		for(T e:vals)
			if((val & (1L << e.ordinal())) != 0)
				ret.add(e);
				
		return ret;		
	}
}
