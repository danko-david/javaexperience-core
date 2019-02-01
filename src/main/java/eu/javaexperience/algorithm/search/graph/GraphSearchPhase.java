package eu.javaexperience.algorithm.search.graph;

public enum GraphSearchPhase
{
	//before, after
	INITALIZE,
	
	//before
	CONTINUE,
	
	//before, after
	MERGE_BRANCH,
	
	//before, after
	EXTEND_SEARCH_FRONT,
	
	//before
	CHECK_FOR_SOLUTION,
	
	//before
	EXIT_NO_MORE_SOLUTION,
	
	//before
	EXIT_BY_CONTROLLER,
	
}
