package eu.javaexperience.algorithm.search.graph;

import java.util.Collection;
import java.util.Map.Entry;

public interface GraphSearchSubject<V, E>
{
	public boolean vertextEquals(V a, V b);
	public void fillNeighborVertexes(V elem, Collection<Entry<E,V>> dstNeighbors);
}
