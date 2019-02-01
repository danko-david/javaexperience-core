package eu.javaexperience.algorithm.search.graph;

import java.util.List;
import java.util.Map.Entry;

public interface GraphSearchAlgorithm<V, E> extends GraphSearchExtendAlgorithm<V, E>
{
	public void initSearch(GraphSearcher<V, E> search);
	
	/**
	 * ret: is least one branch merged, is the search modified?
	 * */
	public boolean mergeBranches(GraphSearcher<V, E> search, List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs);
	
}
