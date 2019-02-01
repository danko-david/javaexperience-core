package eu.javaexperience.collection.list;

import java.util.ArrayList;
import java.util.Collection;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class PrettyPrintArrayList<E> extends ArrayList<E>
{
	protected GetBy1<String, Collection<E>> print;
	
	public PrettyPrintArrayList(GetBy1<String, Collection<E>> print)
	{
		this.print = print;
	}
	
	@Override
	public String toString()
	{
		return print.getBy(this);
	}

	public static final GetBy1<String, Collection> PRINT_CONTENT_MULTILINE = new GetBy1<String, Collection>()
	{
		@Override
		public String getBy(Collection a)
		{
			
			return CollectionTools.toStringMultiline(a);
		}
	};

	
	public static final GetBy1<String, Collection> PRINT_CONTENT_ONLY_UNSEPARATED = new GetBy1<String, Collection>()
	{
		@Override
		public String getBy(Collection a)
		{
			StringBuilder sb = new StringBuilder();
			for(Object c:a)
			{
				sb.append(c);
			}
			return sb.toString();
		}
	};
}
