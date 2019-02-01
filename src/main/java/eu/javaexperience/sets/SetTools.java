package eu.javaexperience.sets;

import java.util.Collection;
import java.util.HashSet;

public class SetTools
{
	public static <T> HashSet<T> diffInplace(Collection<T> set_a, Collection<T> set_b)
	{
		HashSet<T> common = new HashSet<>();
		for(T a:set_a)
		{
			if(set_b.contains(a))
			{
				common.add(a);
			}
		}
		
		for(T c:common)
		{
			set_a.remove(c);
			set_b.remove(c);
		}
		
		return common;
	}
}
