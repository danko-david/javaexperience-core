package eu.javaexperience.algorithm.search.graph.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.algorithm.search.graph.GraphSearchAlgorithm;
import eu.javaexperience.algorithm.search.graph.GraphSearchTraveledPath;
import eu.javaexperience.algorithm.search.graph.GraphSearcher;
import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.sets.SetTools;

public class InteractiveGraphSearchTools
{
	/*public static <V,E> GraphSearchAlgorithm<V, E> forwardSearchFrom(V from)
	{
		return new GraphSearchAlgorithm<V, E>()
		{
			@Override
			public void initSearch(GraphSearcher<V, E> search)
			{
				search.setStartVertexes(from);
			}

			@Override
			public boolean mergeBranches(GraphSearcher<V, E> search, List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs)
			{
				return false;
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
				
			}
		};
	}*/
	
	public static <V,E> GraphSearchAlgorithm<V, E> forwardSearchUsingCliFrom
	(
		GetBy1<String, Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> renderOption,
		BufferedReader in,
		Appendable out
	)
	{
		return new GraphSearchAlgorithm<V, E>()
		{
			@Override
			public void initSearch(GraphSearcher<V, E> search)
			{
			}

			@Override
			public boolean mergeBranches(GraphSearcher<V, E> search, List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs)
			{
				return false;
			}
			
			protected Set<Integer> promptIndexes(String text, int length) throws IOException
			{
				out.append(text);
				String line = in.readLine();
				Set<Integer> ret = new HashSet<>();
				
				try
				{
					String[] nums = line.split(",");
					for(String num:nums)
					{
						num = num.trim();
						if(num.length() > 0)
						{
							ret.add(Integer.parseInt(num.trim()));	
						}
					}
				}
				catch(Exception e)
				{
					out.append(e.getMessage());
					return null;
				}
				return ret;
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
				try
				{
					while(true)
					{
						{
							int i=0;
							for(Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>> ex:possibleExtends)
							{
								out.append((i++)+") "+renderOption.getBy(ex));
								out.append("\n");
							}
						}
						
						Set<Integer> open = promptIndexes("States to open: ", possibleExtends.size());
						if(null == open)
						{
							continue;
						}
						
						Set<Integer> close = promptIndexes("States to close: ", possibleExtends.size());
						if(null == close)
						{
							continue;
						}
						
						HashSet<Integer> match = SetTools.diffInplace(open, close);
						if(!match.isEmpty())
						{
							out.append("Options spcified both open and close: "+CollectionTools.toString(match)+" (Try again)");
							continue;
						}
						
						for(int i=0;i<possibleExtends.size();++i)
						{
							if(open.contains(i))
							{
								selectedExtends.add(possibleExtends.get(i));
							}
							
							if(close.contains(i))
							{
								closedExtends.add(possibleExtends.get(i));
							}
						}
						
						break;
					}
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
				}
			}
		};
	}
	
	public static <V, E> GraphSearchAlgorithm<V, E> hookExtendsSearchFront(GraphSearchAlgorithm<V, E> original, SimplePublish2<Boolean, GraphSearcher<V, E>> hook_trueBefore_falseAfter)
	{
		return new GraphSearchAlgorithm<V, E>()
		{
			@Override
			public void initSearch(GraphSearcher<V, E> search)
			{
				original.initSearch(search);
			}

			@Override
			public boolean mergeBranches
			(
				GraphSearcher<V, E> search,
				List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs
			)
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
				hook_trueBefore_falseAfter.publish(Boolean.TRUE, search);
				original.extendSearchFront(search, possibleExtends, selectedExtends, closedExtends);
				hook_trueBefore_falseAfter.publish(Boolean.FALSE, search);
			}
			
		};
	}
	
	public static <V,E> GraphSearchAlgorithm<V, E> forwardSearchUsingCliStdio
	(
		GetBy1<String, Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> renderOption
	)
	{
		return forwardSearchUsingCliFrom
		(
			renderOption,
			new BufferedReader(new InputStreamReader(System.in)),
			System.out
		);
	}
	
}
