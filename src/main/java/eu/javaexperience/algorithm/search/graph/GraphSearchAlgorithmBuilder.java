package eu.javaexperience.algorithm.search.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;

public class GraphSearchAlgorithmBuilder<V, E>
{
	public GraphSearchDirection searchDirection = GraphSearchDirection.FORWARD;
	public ArrayList<GetBy1<Boolean, Entry<GraphSearchTraveledPath<V, E>, V>>> extendFilters = new ArrayList<>();
	public Comparator<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> orderExtends = null;
	public SimplePublish2<List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>>, Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>>> extendSelector;
	
	
	public GraphSearchAlgorithm<V, E> build()
	{
		final SimplePublish2<List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>>, Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>>> extendSelector = this.extendSelector;
		AssertArgument.assertNotNull(extendSelector, "ExtendSelector must be given, so ");
		
		final GraphSearchDirection searchDirection = this.searchDirection;
		final ArrayList<GetBy1<Boolean, Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>>> extendFilters = (ArrayList<GetBy1<Boolean, Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>>>) this.extendFilters.clone();
		
		final Comparator<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> orderExtends = this.orderExtends;
		
		
		return new GraphSearchAlgorithm<V, E>()
		{
			@Override
			public void initSearch(GraphSearcher<V, E> search)
			{
				switch(searchDirection)
				{
					case BACKWARD: 		search.initBackwardSearch();	break;
					case BIDIRECTIONAL:	search.initTwoSideSearch();		break;
					case FORWARD:		search.initForwardSearch();		break;
					default:			throw new UnimplementedCaseException(searchDirection);
				}
			}

			@Override
			public boolean mergeBranches
			(
				GraphSearcher<V, E> search,
				List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs
			)
			{
				if(searchDirection == GraphSearchDirection.BIDIRECTIONAL)
				{
					return GraphSearchAlgorithms.mergeBranches(search, mergePairs);
				}
				
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
				ArrayList<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> select = new	ArrayList<>();
				
				kint:for(Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>> p:possibleExtends)
				{
					for(GetBy1<Boolean, Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> f:extendFilters)
					{
						Boolean ret = f.getBy(p);
						if(Boolean.FALSE == ret)
						{
							closedExtends.add(p);
							continue kint;
						}
						else if(Boolean.TRUE == ret)
						{
							select.add(p);
							continue kint;
						}
						//if null we skip it (in next iteration it might be selected)
					}
				}
				
				if(null != orderExtends)
				{
					Collections.sort(select, orderExtends);
				}
				
				extendSelector.publish(select, selectedExtends);
			}
		};
	}

	public GraphSearchDirection getSearchDirection()
	{
		return searchDirection;
	}
	
	public void setSearchDirection(GraphSearchDirection searchDirection)
	{
		this.searchDirection = searchDirection;
	}
}
