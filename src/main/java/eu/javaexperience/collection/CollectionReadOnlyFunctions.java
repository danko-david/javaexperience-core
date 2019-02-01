package eu.javaexperience.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class CollectionReadOnlyFunctions
{
	private CollectionReadOnlyFunctions() {}
	
	public static final GetBy1<List, List> MAKE_LIST_READ_ONLY = new GetBy1<List, List>()
	{
		@Override
		public List getBy(List a)
		{
			return Collections.unmodifiableList(a);
		}
	};
	
	public static final GetBy1<Collection, Collection> MAKE_COLLECTION_READ_ONLY = new GetBy1<Collection, Collection>()
	{
		@Override
		public Collection getBy(Collection a)
		{
			return Collections.unmodifiableCollection(a);
		}
	};
	
	public static final GetBy1<Set, Set> MAKE_SET_READ_ONLY = new GetBy1<Set, Set>()
	{
		@Override
		public Set getBy(Set a)
		{
			return Collections.unmodifiableSet(a);
		}
	};
	
	public static final GetBy1<Map, Map> MAKE_MAP_READ_ONLY = new GetBy1<Map, Map>()
	{
		@Override
		public Map getBy(Map a)
		{
			return Collections.unmodifiableMap(a);
		}
	};
}
