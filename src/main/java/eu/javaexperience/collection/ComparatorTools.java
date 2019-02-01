package eu.javaexperience.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class ComparatorTools
{
	public static <T, C extends Comparable<C>> Comparator<T> createFieldComparatorNonNulls(final GetBy1<C, T> getField)
	{
		return new Comparator<T>()
		{
			@Override
			public int compare(T o1, T o2)
			{
				C a1 = getField.getBy(o1);
				C a2 = getField.getBy(o2);
				return a1.compareTo(a2);
			}
		};
	}
	
	public static <T, C extends Comparable<C>> Comparator<T> createFieldComparatorWithNulls
	(
		final GetBy1<C, T> getField,
		final boolean onTrueNullsLowest_onFalseNullHighest
	)
	{
		return new Comparator<T>()
		{
			@Override
			public int compare(T o1, T o2)
			{
				C a1 = getField.getBy(o1);
				C a2 = getField.getBy(o2);
				
				if(null == a1 && null == a2)
				{
					return 0;
				}
				
				if(null == a1)
				{
					return onTrueNullsLowest_onFalseNullHighest?-1:1;
				}
				
				if(null == a2)
				{
					return onTrueNullsLowest_onFalseNullHighest?1:-1;
				}
				
				return a1.compareTo(a2);
			}
		};
	}
	
	public static <T, C> Comparator<T> createFieldComparatorWithNulls
	(
		final GetBy1<C, T> getField,
		final Comparator<C> cmp,
		final boolean onTrueNullsLowest_onFalseNullHighest
	)
	{
		return new Comparator<T>()
		{
			@Override
			public int compare(T o1, T o2)
			{
				C a1 = getField.getBy(o1);
				C a2 = getField.getBy(o2);
				
				if(null == a1 && null == a2)
				{
					return 0;
				}
				
				if(null == a1)
				{
					return onTrueNullsLowest_onFalseNullHighest?-1:1;
				}
				
				if(null == a2)
				{
					return onTrueNullsLowest_onFalseNullHighest?1:-1;
				}
				
				return cmp.compare(a1, a2);
			}
		};
	}
	
	public static <T> Comparator<T> reverseOrder(final Comparator<T> cmp)
	{
		return new Comparator<T>()
		{
			@Override
			public int compare(T o1, T o2)
			{
				return -1*cmp.compare(o1, o2);
			}
		};
	}
	
	public static <T> Comparator<T> comparatorByGoldenList(final T... list)
	{
		return new Comparator<T>()
		{
			protected int getOrdinal(T elem)
			{
				return ArrayTools.indexOf(elem, list);
			}
			
			@Override
			public int compare(T o1, T o2)
			{
				return Integer.compare(getOrdinal(o1), getOrdinal(o2));
			}
		};
	}
	
	public static final Comparator<Boolean> COMPARATOR_FOR_BOOLEAN = (a,b) -> a.compareTo(b);
	public static final Comparator<Byte> COMPARATOR_FOR_BYTE = (a,b) -> a.compareTo(b);
	public static final Comparator<Character> COMPARATOR_FOR_CHARACTER = (a,b) -> a.compareTo(b);
	public static final Comparator<Integer> COMPARATOR_FOR_INTEGER = (a,b) -> a.compareTo(b);
	public static final Comparator<Long> COMPARATOR_FOR_LONG = (a,b) -> a.compareTo(b);
	public static final Comparator<Float> COMPARATOR_FOR_FLOAT = (a,b) -> a.compareTo(b);
	public static final Comparator<Double> COMPARATOR_FOR_DOUBLE = (a,b) -> a.compareTo(b);
	public static final Comparator<String> COMPARATOR_FOR_STRING = (a,b) -> a.compareTo(b);
	public static final Comparator<Date> COMPARATOR_FOR_DATE = (a,b) -> a.compareTo(b);
	
	public static final Comparator<Number> COMPARATOR_FOR_NUMBER = (a,b) -> COMPARATOR_FOR_DOUBLE.compare(a.doubleValue(), b.doubleValue());
	
	public static final Comparator<Enum> COMPARATOR_FOR_ENUM = (a,b) -> a.compareTo(b);
	
	public static final Comparator<Object> COMPARATOR_FOR_OBJECT_HASH = (a,b) -> COMPARATOR_FOR_INTEGER.compare(a.hashCode(), b.hashCode());
	//public static final Comparator<> COMPARATOR_FOR_ = (a,b) -> a.compareTo(b);
	//public static final Comparator<> COMPARATOR_FOR_ = (a,b) -> a.compareTo(b);
	
	protected static final Map<Class, Comparator> BY_CLASS = new HashMap<>();
	static
	{
		BY_CLASS.put(Boolean.class, COMPARATOR_FOR_BOOLEAN);
		BY_CLASS.put(Byte.class, COMPARATOR_FOR_BYTE);
		BY_CLASS.put(Character.class, COMPARATOR_FOR_CHARACTER);
		BY_CLASS.put(Integer.class, COMPARATOR_FOR_INTEGER);
		BY_CLASS.put(Long.class, COMPARATOR_FOR_LONG);
		BY_CLASS.put(Float.class, COMPARATOR_FOR_FLOAT);
		BY_CLASS.put(Double.class, COMPARATOR_FOR_DOUBLE);
		BY_CLASS.put(String.class, COMPARATOR_FOR_STRING);
		BY_CLASS.put(Date.class, COMPARATOR_FOR_DATE);
		BY_CLASS.put(Number.class, COMPARATOR_FOR_NUMBER);
		BY_CLASS.put(Enum.class, COMPARATOR_FOR_ENUM);
	}
	
	public static <T> Comparator<T> getComparatorByClass(Class c)
	{
		return BY_CLASS.get(c);
	}
	
	
	public static <T> Comparator<T> getComparatorByClass(Class<T> type, Comparator<T> _default)
	{
		Comparator<T> ret = getComparatorByClass(type);
		if(null == ret)
		{
			return _default; 
		}
		
		return ret;
	}
	
	public static void main(String[] args)
	{
		ArrayList<String> lst = new ArrayList<>();
		
		Comparator<String> gc = comparatorByGoldenList("^", "/", "*", "-", "+");
		
		lst.add("*");
		lst.add("+");
		lst.add("^");
		lst.add("/");
		lst.add("^");
		lst.add("-");
		
		Collections.sort(lst, gc);
		
		System.out.println(CollectionTools.toStringMultiline(lst));
		
		/*ArrayList<String> strs = new ArrayList<String>();
		
		strs.add(null);
		strs.add("a");
		strs.add("c");
		strs.add("b");
		
		Collections.sort(strs, createFieldComparatorWithNulls(new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return a;
			}
		},
		false
		));
		
		System.out.println(strs);*/
	}
}
