package eu.javaexperience.algorithm.search.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.BulkTransitMap;
import eu.javaexperience.collection.map.KeyVal;
import eu.javaexperience.collection.map.MultiMap;
import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;

public class GraphSearchAlgorithms
{
	public static <V, E> GraphSearchAlgorithm<V, E> depthFirstHeuristic(final GetBy2<Long, V, V> toDestinationCostToMinimize)
	{
		return new GraphSearchAlgorithm<V, E>()
		{
			@Override
			public void extendSearchFront
			(
				GraphSearcher<V, E> search,
				List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
			)
			{
				//get always the longest paths
				BulkTransitMap<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>, Long> select = new BulkTransitMap<Entry<GraphSearchTraveledPath<V,E>, Entry<E, V>>, Long>();
				long lowest = Long.MAX_VALUE; 
				{
					V dst = search.getFirstDestination();
					
					Collections.sort((List)possibleExtends, (Comparator)getComparatorOrdersSearchPathLength());
					int max = possibleExtends.get(0).getKey().path.getTransitionsCount();
					
					for(int i=0;i<possibleExtends.size();++i)
					{
						Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>> e = possibleExtends.get(i);
						if(max != e.getKey().path.getTransitionsCount())
						{
							break;
						}
						
						long l = toDestinationCostToMinimize.getBy(e.getValue().getValue(), dst);
						
						if(l < lowest)
						{
							lowest = l;
						}
						
						select.put(e, l);
					}
				}
				
				//and select from these the the best one
				{
					for(Entry<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>, Long> kv:select.entrySet())
					{
						if(kv.getValue().equals(lowest))
						{
							selectedExtends.add(kv.getKey());
							return;
						}
					}
				}
			}

			@Override
			public boolean mergeBranches
			(
				GraphSearcher<V, E> search,
				List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs
			)
			{
				return false;
			}
			
			@Override
			public void initSearch(GraphSearcher<V, E> search)
			{
				search.initForwardSearch();
			}
		};
	}
	
	public static final <V, E> Comparator<Entry<GraphSearchTraveledPath<V, E>, V>> getComparatorOrdersSearchPathLength()
	{
		return (Comparator)ORDER_BY_GRAPH_PATH_LENGTH;
	}
	
	protected static final Comparator<Entry<GraphSearchTraveledPath<?, ?>, ?>> ORDER_BY_GRAPH_PATH_LENGTH = new Comparator<Entry<GraphSearchTraveledPath<?,?>,?>>()
	{
		@Override
		public int compare
		(
			Entry<GraphSearchTraveledPath<?, ?>, ?> o1,
			Entry<GraphSearchTraveledPath<?, ?>, ?> o2
		)
		{
			return -1*Integer.compare
			(
				o1.getKey().path.getTransitionsCount(),
				o2.getKey().path.getTransitionsCount()
			);
		}
	};
	
	public static <V, E> boolean mergeBranches
	(
		GraphSearcher<V, E> search,
		List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs
	)
	{
		MultiMap<V, GraphSearchTraveledPath<V, E>> forward = new MultiMap<>();
		MultiMap<V, GraphSearchTraveledPath<V, E>> backward = new MultiMap<>();
		
		for(GraphSearchTraveledPath<V, E> s:search.getOpenBranches())
		{
			switch(s.getSearchDirection())
			{
				case BACKWARD: backward.put(s.getDirectionVertex(), s); break;
				case BIDIRECTIONAL:
				case FORWARD:	forward.put(s.getDirectionVertex(), s); break;
				default: throw new UnimplementedCaseException(s.getSearchDirection());
			}
		}
		
		boolean mergeHappened = false;
		
		for(Entry<V, List<GraphSearchTraveledPath<V, E>>> f:forward.multiEntrySet())
		{
			List<GraphSearchTraveledPath<V, E>> lst = backward.getList(f.getKey());
			
			if(null != lst)
			{
				for(GraphSearchTraveledPath<V, E> m1:f.getValue())
				{
					for(GraphSearchTraveledPath<V, E> m2:lst)
					{
						mergePairs.add(new KeyVal(m1, m2));
						mergeHappened = true;
					}
				}
			}
		}
		
		return mergeHappened;
	}
	
	public static <V, E> GraphSearchAlgorithm<V, E> beforeOriginalExtend
	(
		final GraphSearchAlgorithm<V, E> original,
		final GraphSearchExtendAlgorithm<V, E>... selects
	)
	{
		return new GraphSearchAlgorithm<V, E>()
		{
			@Override
			public void initSearch(GraphSearcher<V, E> search)
			{
				original.initSearch(search);
			}

			@Override
			public boolean mergeBranches(GraphSearcher<V, E> search, List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs)
			{
				return original.mergeBranches(search, mergePairs);
			}

			@Override
			public void extendSearchFront
			(
				GraphSearcher<V, E> search,
				List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
			)
			{
				List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> pe = new ArrayList<>();
				Set<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> decided = Collections.newSetFromMap(new IdentityHashMap<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>, Boolean>());
				
				pe.addAll(possibleExtends);
				for(GraphSearchExtendAlgorithm<V, E> select:selects)
				{
					pe.removeAll(decided);
					select.extendSearchFront(search, pe, selectedExtends, closedExtends);
					decided.addAll(selectedExtends);
					decided.addAll(closedExtends);
				}
				pe.removeAll(decided);
				original.extendSearchFront(search, pe, selectedExtends, closedExtends);
			}
		};
	}
	
	public static GraphSearchExtendAlgorithm EXTEND_WHEN_ONLY_ONE_OPTION_AVAILALBE = new GraphSearchExtendAlgorithm()
	{
		@Override
		public void extendSearchFront
		(
			GraphSearcher search,
			List possibleExtends,
			Collection selectedExtends,
			Collection closedExtends
		)
		{
			if(possibleExtends.size() == 1)
			{
				selectedExtends.add(possibleExtends.get(0));
			}
		}
	};

	public static <V, E> SimplePublish2<List<Entry<GraphSearchTraveledPath<V, E>, V>>, Collection<Entry<GraphSearchTraveledPath<V, E>, V>>> getExtendAllSelector()
	{
		return (SimplePublish2) EXTEND_ALL_SELECTOR;
	}
	
	protected static final SimplePublish2<List<Entry>, Collection<Entry>> EXTEND_ALL_SELECTOR = new SimplePublish2<List<Entry>, Collection<Entry>>()
	{
		@Override
		public void publish(List<Entry> a, Collection<Entry> b)
		{
			CollectionTools.copyInto(a, b);
		}
	};
	
	public static final GraphSearchExtendAlgorithm EXTEND_ALL = new GraphSearchExtendAlgorithm()
	{
		@Override
		public void extendSearchFront(GraphSearcher search, List possibleExtends, Collection selectedExtends, Collection closedExtends)
		{
			selectedExtends.addAll(possibleExtends);
		}
	};
	
	public static <V, E> GraphSearchAlgorithm<V, E> fromExtend(final GraphSearchExtendAlgorithm<V, E> ext)
	{
		return new GraphSearchAlgorithm<V, E>()
		{
			@Override
			public void extendSearchFront
			(
				GraphSearcher<V, E> search,
				List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
			)
			{
				ext.extendSearchFront(search, possibleExtends, selectedExtends, closedExtends);
			}

			@Override public void initSearch(GraphSearcher<V, E> search){}

			@Override
			public boolean mergeBranches(GraphSearcher<V, E> search, List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs)
			{
				return false;
			}
		};
	}

	public static <V, E> GraphSearchExtendAlgorithm<V, E> closeVisitedState(final GetBy2<Boolean, GraphSearcher<V, E>, Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> select)
	{
		return new GraphSearchExtendAlgorithm<V, E>()
		{
			@Override
			public void extendSearchFront
			(
				GraphSearcher<V, E> search,
				List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
				Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
			)
			{
				for(Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>> ext:possibleExtends)
				{
					if(Boolean.TRUE == select.getBy(search, ext))
					{
						closedExtends.add(ext);
					}
				}
			}
		};
	}
}
