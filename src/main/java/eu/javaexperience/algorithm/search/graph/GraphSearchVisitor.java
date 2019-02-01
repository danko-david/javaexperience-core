package eu.javaexperience.algorithm.search.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public interface GraphSearchVisitor<V, E>
{
	public void solutionFound(GraphSearchTraveledPath<V, E> sol);

	public void before(GraphSearcher<V, E> search, GraphSearchPhase initalize);

	public void after(GraphSearcher<V, E> search, GraphSearchPhase initalize);
	
	public void publishMergeSelection(GraphSearcher<V, E> search, List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> lst);
	
	public void publishExtendSelection
	(
		GraphSearcher<V, E> search,
		List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
	);
}
