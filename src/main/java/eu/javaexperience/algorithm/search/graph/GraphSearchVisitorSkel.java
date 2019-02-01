package eu.javaexperience.algorithm.search.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public class GraphSearchVisitorSkel<V, E> implements GraphSearchVisitor<V, E>
{
	@Override public void solutionFound(GraphSearchTraveledPath<V, E> sol){};

	@Override public void before(GraphSearcher<V, E> search, GraphSearchPhase initalize){};

	@Override public void after(GraphSearcher<V, E> search, GraphSearchPhase initalize){};
	
	@Override public void publishMergeSelection(GraphSearcher<V, E> search, List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> lst){};
	
	@Override public void publishExtendSelection
	(
		GraphSearcher<V, E> search,
		List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
	){};
}
