package eu.javaexperience.functional;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class ComparableFunctions
{
	public static <C extends Comparable<C>> GetBy1<Boolean, C> isBetween(C min, C max)
	{
		return new GetBy1<Boolean, C>()
		{
			@Override
			public Boolean getBy(C a)
			{
				return
					min.compareTo(a) <= 0
				&&
					a.compareTo(max) <= 0;
			}
		};
	}
	
	public static <C extends Comparable<C>> GetBy1<Boolean, C> isLessThan(C val)
	{
		return new GetBy1<Boolean, C>()
		{
			@Override
			public Boolean getBy(C a)
			{
				return a.compareTo(val) < 0;
			}
		};
	}
	
	public static <C extends Comparable<C>> GetBy1<Boolean, C> isLessThanEquals(C val)
	{
		return new GetBy1<Boolean, C>()
		{
			@Override
			public Boolean getBy(C a)
			{
				return a.compareTo(val) <= 0;
			}
		};
	}
	
	public static <C extends Comparable<C>> GetBy1<Boolean, C> isGreaterThan(C val)
	{
		return new GetBy1<Boolean, C>()
		{
			@Override
			public Boolean getBy(C a)
			{
				return a.compareTo(val) > 0;
			}
		};
	}
	
	public static <C extends Comparable<C>> GetBy1<Boolean, C> isGreaterThanEquals(C val)
	{
		return new GetBy1<Boolean, C>()
		{
			@Override
			public Boolean getBy(C a)
			{
				return a.compareTo(val) >= 0;
			}
		};
	}
	
	public static <C extends Comparable<C>> GetBy1<Boolean, C> isEquals(C val)
	{
		return new GetBy1<Boolean, C>()
		{
			@Override
			public Boolean getBy(C a)
			{
				return a.compareTo(val) == 0;
			}
		};
	}
}
