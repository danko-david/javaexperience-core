package eu.javaexperience.algorithm.search.graph;

public interface GraphSearchController<V, E>
{
	public boolean needContinueSearch(GraphSearcher<V, E> search);
}
