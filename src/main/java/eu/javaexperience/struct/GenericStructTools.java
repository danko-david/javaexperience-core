package eu.javaexperience.struct;

import java.util.ArrayList;
import java.util.Collection;

import eu.javaexperience.reflect.Mirror;

public class GenericStructTools
{
	public static <A,B,C> GenericStruct3<A, B, C> getWhereCIs(Collection<GenericStruct3<A, B, C>> rows, C searchFor)
	{
		for(GenericStruct3<A, B, C> row:rows)
		{
			if(Mirror.equals(row.c, searchFor))
			{
				return row;
			}
		}
		
		return null;
	}
	
	public static <A,B extends Comparable<B>,R extends GenericStruct2<A,B>> R getWhereBMax(Collection<R> rows)
	{
		B max = null;
		R r = null;
		for(R row:rows)
		{
			if(null == max)
			{
				max = row.b;
				r = row;
			}
			
			if(null != max && null != row.b)
			{
				if(max.compareTo(row.b) < 0)
				{
					max = row.b;
					r = row;
				}
			}
		}
		
		return r;
	}
	
	public static void main(String[] args)
	{
		ArrayList<GenericStruct2<Integer, Integer>> rows = new ArrayList<>();
		rows.add(new GenericStruct2<Integer,Integer>(0,0));
		rows.add(new GenericStruct2<Integer,Integer>(0,1));
		rows.add(new GenericStruct2<Integer,Integer>(0,5));
		rows.add(new GenericStruct2<Integer,Integer>(0,2));
		rows.add(new GenericStruct2<Integer,Integer>(0,1));
		
		System.out.println(getWhereBMax(rows).b);
		
	}
}
