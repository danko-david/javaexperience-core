package eu.javaexperience.collection;

import java.util.Collection;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class CollectionFunctions
{
	private CollectionFunctions(){}
	
	public static <T> GetBy1<Boolean, T> isIn(final Collection<T> coll)
	{
		return new GetBy1<Boolean, T>()
		{
			@Override
			public Boolean getBy(T a)
			{
				return coll.contains(a);
			}
		};
	}
}
