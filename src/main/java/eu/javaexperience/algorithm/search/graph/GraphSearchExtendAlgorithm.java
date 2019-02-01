package eu.javaexperience.algorithm.search.graph;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public interface GraphSearchExtendAlgorithm<V, E>
{
	public void extendSearchFront
	(
		GraphSearcher<V, E> search,
		List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
	);
}
