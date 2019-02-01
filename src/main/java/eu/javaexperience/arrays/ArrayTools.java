package eu.javaexperience.arrays;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.reflect.Mirror;

public class ArrayTools
{
	private ArrayTools(){}
	
	/**
	 * Expand the array size by 1 and adds the element at the end of the array.
	 * @param arr target array
	 * @param elem element to add
	 * @return new, expanded array whit the specified element at the end.
	 * */
	public static <T> T[] arrayAppend(T[] arr, T elem)
	{
		T ret[] = Arrays.copyOf(arr, arr.length+1);
		ret[arr.length] = elem;
		return ret;
	}
	
	/**
	 * Expand the array size by 1 and adds the element at the 0th position of the array.
	 * @param arr target array
	 * @param elem element to add
	 * @return new, expanded array whit the specified element at the 0th index.
	 * */
	public static <T> T[] arrayAppend(T elem,T[] arr)
	{
		T ret[] = Arrays.copyOf(arr, arr.length+1);
		ret[0] = elem;
		for(int i=1;i<ret.length;i++)
		{
			ret[i] = arr[i-1];
		}
		
		
		
		return ret;
	}
	
	public static <T> T[] arrayAppendOrCreate(T[] arr, T elem)
	{
		if(null == arr)
		{
			T[] ret = (T[]) Array.newInstance(elem.getClass(), 1);
			ret[0] = elem;
			return ret;
		}
		
		T ret[] = Arrays.copyOf(arr, arr.length+1);
		ret[arr.length] = elem;
		return ret;
	}
	
	/**
	 * Add the specified element to the array if it doesn't conatins identically
	 * @param arr target array
	 * @param elem element to add
	 * @return if contains, identically same as arr param. otherwise new, expanded array with
	 * the specified element at the end.
	 * */
	public static <T> T[] arrayAppendIfNotContainsOpEQ(T[] arr,T elem)
	{
		for(T e:arr)
			if(e == elem)
				return arr;
		
		return arrayAppend(arr, elem);
	}
	
	/**
	 * Add the specified element to the array if it doesn't conatins identically
	 * @param arr target array
	 * @param elem element to add
	 * @return if contains, identically same as arr param. otherwise new, expanded array with
	 * the specified element at the end.
	 * */
	public static <T> T[] arrayAppendIfNotContainsEquals(T[] arr,T elem)
	{
		for(T e:arr)
			if(e != null && e.equals(elem))
				return arr;
		
		return arrayAppend(arr, elem);
	}
	
	public static <T> T[] arrayConcat(T[] arr1, T[] arr2)
	{
		T[] ret = Arrays.copyOf(arr1, arr1.length+arr2.length);
		for(int i=0;i<arr2.length;i++)
		{
			ret[arr1.length+i] = arr2[i];
		}
		
		return ret;
	}
	
	public static <T> T[] modifyReverse(T[] arr)
	{
		T buff = null;
		int lm1 = arr.length-1;
		int al2 = arr.length/2;
		for(int i=0;i<al2;i++)
		{
			buff = arr[lm1-i];
			arr[lm1-i] = arr[i];
			arr[i] = buff;
		}
		return arr;
	}
	
	public static byte[] arrayConcat(byte[] d1,byte[] d2)
	{
		byte[] ret = Arrays.copyOf(d1, d1.length+d2.length);
		for(int i=0;i<d2.length;i++)
			ret[d1.length+i] = d2[i];
			
		return ret;
	}
	
	public static <T> T[] copy(T[] arr)
	{
		return Arrays.copyOf(arr, arr.length);
	}

	public static <T> T[] array(T... el)
	{
		return el;
	}
	
	public static Object[] arrayRaw(Object... el)
	{
		return el;
	}
	
	public static int nullCount(Object[] arr)
	{
		int num = 0;
		for(int i=0;i<arr.length;i++)
			if(arr[i] == null)
				++num;
		
		return num;
	}
	
	/**
	 * @throws IndexOutOfBoundsException if array modified concurrently
	 * and a null value swapped to non null value.
	 * */
	public static <T> T[] withoutNulls(T... arr)
	{
		int ep = 0;
		T[] ret = Arrays.copyOf(arr, arr.length-nullCount(arr));
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i] != null)
			{
				ret[ep++] = arr[i];
			}
		}
		return ret;
	}

	public static <T> int populationIdentically(T[] arr, T elem)
	{
		int occ = 0;
		for(T a:arr)
			if(elem == a)
				++occ;
		
		return occ;
	}
	
	public static <T> T[] withoutElementIdentically(T[] array, T element)
	{
		int occ = populationIdentically(array, element);
		if(0 == occ)
			return array;

		T[] ret = Arrays.copyOf(array, array.length-occ);
		int ep = 0;
		for(int i=0;i<array.length;++i)
			if(array[i] != element)
				ret[ep++] = array[i];
		
		return ret;
	}
	
	public static <T> int population(T[] arr, T elem)
	{
		int occ = 0;
		for(T a:arr)
		{
			if(Mirror.equals(a, elem))
			{
				++occ;
			}
		}
		return occ;
	}
	
	public static <T> T[] whitoutElement(T[] vals, T value)
	{
		T[] ret = Arrays.copyOf(vals, vals.length);
		int len = 0;
		
		for(T e:vals)
		{
			if(!Mirror.equals(e, value))
			{
				ret[len++] = e;
			}
		}
		
		if(len == vals.length)
		{
			return ret;
		}
		
		return Arrays.copyOf(ret, len);
	}
	
	/**
	 * It @param obj is an array and has least one element this function returns
	 * the first element of array.
	 * */
	public static Object tryAccessFirstElement(Object obj)
	{
		if(null == obj)//note obj instanceof Object[] is false e.g. for int[]
			return null;
		
		if(obj.getClass().isArray())
			if(Array.getLength(obj) > 0)
				return Array.get(obj, 0);
		
		return null;
	}

	public static <T> boolean contains(T[] arr, T elem)
	{
		for(T t:arr)
		{
			if(Mirror.equals(t, elem))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isEndsWith(String[] search, String[] pattern)
	{
		if(pattern.length > search.length)
		{
			return false;
		}
		
		for(int i=0;i<pattern.length;++i)
		{
			if(!pattern[pattern.length-1-i].equals(search[search.length-1-i]))
			{
				return false;
			}
		}
		
		return true;
	}

	public static <T> boolean contains
	(
		GetBy2<Boolean, T, T> eq,
		T[] arr, T a
	)
	{
		for(T in: arr)
		{
			if(Boolean.TRUE == eq.getBy(in, a))
			{
				return true;
			}
		}
		return false;
	}

	public static <T> T extractFirst(T[] arr)
	{
		if(null != arr)
		{
			if(arr.length > 0)
			{
				return arr[0];
			}
		}
		return null;
	}

	public static <T> T[][] sliceHalf(T[] subject)
	{
		T[][] ret = (T[][]) Array.newInstance(subject.getClass(), 2);
		if(subject.length == 0)
		{
			ret[0] = ret[1] = (T[]) Array.newInstance(subject.getClass().getComponentType(), 0);
		}
		
		else
		//else if(subject.length % 2 == 0)
		{
			int f = subject.length/2;
			ret[0] = Arrays.copyOfRange(subject, 0, f);
			ret[1] = Arrays.copyOfRange(subject, f, subject.length);
		}
		
		return ret;
	}
	
	public static <T> boolean arrayEquals(T[] a, T[] b)
	{
		if(a.length != b.length)
		{
			return false;
		}
		
		for(int i=0;i<a.length;++i)
		{
			if(!Mirror.equals(a[i], b[i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	protected static <T> void sliceMerge(T[] arr)
	{
		T[][] split = sliceHalf(arr);
		T[] re = arrayConcat(split[0], split[1]);
		if(!arrayEquals(arr, re))
		{
			System.out.println(Arrays.toString(arr)+" | "+Arrays.toString(re));
			throw new RuntimeException("Slice merge failed");
		}
	}
	
	/*public static void main(String[] args)
	{
		sliceMerge(new String[]{});
		sliceMerge(new String[]{"asd"});
		sliceMerge(new String[]{"asd", "asdf"});
		sliceMerge(new String[]{"asd", "asdf", "asdfg"});
		sliceMerge(new String[]{"asd", "asdf", "asdfg"});
		sliceMerge(new String[]{"asd", "asdf", "asdfg", "asdfgh"});
		System.out.println("success");
	}*/

	public static <T> T accessIndexSafe(T[] arr, int index)
	{
		if(index >= 0 && null != arr && arr.length > index)
		{
			return arr[index];
		}
		
		return null;
	}
	
	public static <T> T[] withoutIndex(T[] arr, int index)
	{
		if(index >= arr.length && index < 0)
			return arr;
		
		T[] ret = Arrays.copyOf(arr, arr.length-1);
		int b = 0;
		for(int i=0;i<arr.length;++i)
		{
			if(i == index)
				continue;
			ret[b++] = arr[i];
		}
		
		return ret;
	}
	
	public static <T> int indexOf(T elem, T... array)
	{
		for(int i=0;i<array.length;++i)
		{
			if(elem.equals(array[i]))
			{
				return i;
			}
		}
		
		return -1;
	}

	public static String toString(Object[] obj)
	{
		if(obj == null)
			return "null";
		
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(Object o:obj)
		{
			if(sb.length() > 1)
				sb.append(",");
			sb.append(o);
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	public static <F, T> T[] convert(Class<T> toClass, F[] from, GetBy1<T, F> convert)
	{
		T[] ret = (T[]) Array.newInstance(toClass, from.length);
		for(int i=0;i<ret.length;++i)
		{
			ret[i] = convert.getBy(from[i]);
		}
		return ret;
	}

	public static Object[] extractPrimitiveArray(Object content)
	{
		int len = Array.getLength(content);
		Object[] ret = new Object[len];
		for(int i=0;i<ret.length;++i)
		{
			ret[i] = Array.get(content, i);
		}
		
		return ret;
	}
}