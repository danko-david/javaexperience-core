package eu.javaexperience.algorithm.search.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.collection.CollectionReadOnlyFunctions;
import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.ReadOnlyAndRwCollection;
import eu.javaexperience.collection.map.KeyVal;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.ExternalDataAttached;
import eu.javaexperience.semantic.references.MayNotNull;

public class GraphSearcher<V, E> implements ExternalDataAttached
{
	protected GraphSearchAlgorithm<V, E> algorithm;
	protected GraphSearchSubject<V, E> subject;
	protected GraphSearchVisitor<V, E> visitor;
	protected GraphSearchController<V, E> controller;
	
	protected ReadOnlyAndRwCollection<List<V>> startVertexes = new ReadOnlyAndRwCollection(new ArrayList<>(), CollectionReadOnlyFunctions.MAKE_LIST_READ_ONLY);
	protected ReadOnlyAndRwCollection<List<V>> destinationVertexes = new ReadOnlyAndRwCollection(new ArrayList<>(), CollectionReadOnlyFunctions.MAKE_LIST_READ_ONLY);
	
	protected static Set createSet()
	{
		return Collections.newSetFromMap(new IdentityHashMap<>());
	}
	
	protected ReadOnlyAndRwCollection<Collection<GraphSearchTraveledPath<V, E>>> closedBranches = new ReadOnlyAndRwCollection(createSet(), CollectionReadOnlyFunctions.MAKE_SET_READ_ONLY);
	protected ReadOnlyAndRwCollection<Collection<GraphSearchTraveledPath<V, E>>> openBranches = new ReadOnlyAndRwCollection(createSet(), CollectionReadOnlyFunctions.MAKE_SET_READ_ONLY);
	protected ReadOnlyAndRwCollection<Collection<GraphSearchTraveledPath<V, E>>> solutions = new ReadOnlyAndRwCollection(createSet(), CollectionReadOnlyFunctions.MAKE_SET_READ_ONLY);
	
	protected boolean searchStarted = false;
	protected boolean noMoreSolution = false;
	
	protected int iteration;
	
	public GraphSearcher
	(
		GraphSearchAlgorithm<V, E> algorithm,
		GraphSearchSubject<V, E> subject,
		GraphSearchController<V, E> controller
	)
	{
		this.algorithm = algorithm;
		this.subject = subject;
		this.controller = controller;
	}
	
	public void setVisitor(GraphSearchVisitor<V, E> visitor)
	{
		this.visitor = visitor;
	}
	
	protected void addBranchStart(V point, GraphSearchDirection dir )
	{
		GraphSearchTraveledPath<V, E> add = new GraphSearchTraveledPath<>(this, dir);
		add.path.addInitialVertext(point);
		openBranches.getWriteable().add(add);
	}
	
	public void initForwardSearch()
	{
		assertSearchNotStarted();
		for(V v:startVertexes.getReadOnly())
		{
			addBranchStart(v, GraphSearchDirection.FORWARD);
		}
	}
	
	public void initTwoSideSearch()
	{
		assertSearchNotStarted();
		for(V v:startVertexes.getReadOnly())
		{
			addBranchStart(v, GraphSearchDirection.FORWARD);
		}
		
		for(V v:destinationVertexes.getReadOnly())
		{
			addBranchStart(v, GraphSearchDirection.BACKWARD);
		}
	}
	
	public void initBackwardSearch()
	{
		assertSearchNotStarted();
		for(V v:destinationVertexes.getReadOnly())
		{
			addBranchStart(v, GraphSearchDirection.BACKWARD);
		}
	}
	
	public boolean isMoreSolutionPossible()
	{
		return !noMoreSolution;
	}
	
	public void assertSearchNotStarted()
	{
		if(searchStarted)
		{
			throw new RuntimeException("Search already started");
		}
	}
	
	public void setStartVertexes(V... from)
	{
		assertSearchNotStarted();
		CollectionTools.copyInto(from, startVertexes.getWriteable());
	}
	
	public void setDestinationVertexes(V... to)
	{
		assertSearchNotStarted();
		CollectionTools.copyInto(to, destinationVertexes.getWriteable());
	}
	
	public Collection<GraphSearchTraveledPath<V, E>> getSolutions()
	{
		return solutions.getReadOnly();
	}
	
	public Collection<GraphSearchTraveledPath<V, E>> getOpenBranches()
	{
		return openBranches.getReadOnly();
	}
	
	public Collection<GraphSearchTraveledPath<V, E>> getClosedBranches()
	{
		return closedBranches.getReadOnly();
	}
	
	public List<GraphSearchTraveledPath<V, E>> getAllBranches()
	{
		List<GraphSearchTraveledPath<V, E>> ret = new ArrayList<>();
		ret.addAll(solutions.getReadOnly());
		ret.addAll(openBranches.getReadOnly());
		ret.addAll(closedBranches.getReadOnly());
		return ret;
	} 
	
	public void continueSearch()
	{
		continueSearch(false);
	}
	
	public void continueSearch(boolean stepping)
	{
		if(!searchStarted)
		{
			visitAtPoint(true, GraphSearchPhase.INITALIZE);
			algorithm.initSearch(this);
			visitAtPoint(false, GraphSearchPhase.INITALIZE);
		}
		
		searchStarted = true;
		
		ArrayList<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends = new ArrayList<>();
		Set<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends = createSet();//new HashSet<>();
		
		List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs = new ArrayList<>();
		
		visitAtPoint(true, GraphSearchPhase.CONTINUE);
		
		boolean step = true;
		
		while(step && isMoreSolutionPossible() && controller.needContinueSearch(this))
		{
			if(stepping)
			{
				step = false;
			}
			
			possibleExtends.clear();
			mergePairs.clear();
			
			visitAtPoint(true, GraphSearchPhase.CHECK_FOR_SOLUTION);
			
			//check solution for fist, we might be in the destination already
			if(examineSolutions())
			{
				visitAtPoint(true, GraphSearchPhase.EXIT_BY_CONTROLLER);
				return;
			}
			
			visitAtPoint(true, GraphSearchPhase.MERGE_BRANCH);
			
			if(algorithm.mergeBranches(this, mergePairs))
			{
				executeMerges(mergePairs);
				
				if(examineSolutions())
				{
					visitAtPoint(true, GraphSearchPhase.EXIT_BY_CONTROLLER);
					return;
				}
			}
			
			visitAtPoint(false, GraphSearchPhase.MERGE_BRANCH);
			
			visitAtPoint(true, GraphSearchPhase.EXTEND_SEARCH_FRONT);
			
			iteration++;
			fillPossibleExtends(possibleExtends);
			
			if(possibleExtends.isEmpty())
			{
				noMoreSolution = true;
				visitAtPoint(true, GraphSearchPhase.EXIT_NO_MORE_SOLUTION);
				return;
			}
			else
			{
				Set<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends = createSet();
				
				algorithm.extendSearchFront(this, possibleExtends, selectedExtends, closedExtends);
				
				//fool proofing
				closedExtends.removeAll(selectedExtends);
				
				publishToExtend(possibleExtends, selectedExtends, closedExtends);
				
				for(Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>> c:closedExtends)
				{
					c.getKey().processedNeighborVertexes.add(c.getValue());
					tryRemoveClosedBranch(c.getKey());
				}
				
				if(selectedExtends.isEmpty())
				{
					noMoreSolution = true;
					visitAtPoint(true, GraphSearchPhase.EXIT_NO_MORE_SOLUTION);
					return;
				}
				else
				{
					Collection<GraphSearchTraveledPath<V, E>> ob = openBranches.getWriteable();
					
					//process new extends
					for(Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>> e:selectedExtends)
					{
						GraphSearchTraveledPath<V, E> ex = createExtend(e);
						//if there's no other edge to discover, we remove this branch
						tryRemoveClosedBranch(e.getKey());
						
						ob.add(ex);
					}
				}
			}
			
			visitAtPoint(false, GraphSearchPhase.EXTEND_SEARCH_FRONT);
			
			visitAtPoint(true, GraphSearchPhase.CHECK_FOR_SOLUTION);
			
			if(examineSolutions())
			{
				visitAtPoint(true, GraphSearchPhase.EXIT_BY_CONTROLLER);
				return;
			}
		}
	}
	
	/*protected void checkForMultiPath()
	{
		//Set<GraphSearchTraveledPath<V, E>>
		Set<String>
		paths = new HashSet<>();
		for(GraphSearchTraveledPath<V, E> ro:openBranches.getReadOnly())
		{
			if(!paths.add(ro.toString()))
			{
				System.out.println("Already added path: "+ro);
				new Exception().printStackTrace();
			}
		}
	}
	*/
	protected void publishToExtend
	(
		List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> selectedExtends,
		Collection<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> closedExtends
	)
    {
		if(null != visitor)
		{
			visitor.publishExtendSelection(this, possibleExtends, Collections.unmodifiableCollection(selectedExtends), Collections.unmodifiableCollection(closedExtends));
		}
    }

	protected void publishToMerge(List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> lst)
	{
		if(null != visitor)
		{
			visitor.publishMergeSelection(this, Collections.unmodifiableList(lst));
		}
	}
	
	protected void visitAtPoint(boolean before, GraphSearchPhase phase)
	{
		if(null != visitor)
		{
			if(before)
			{
				visitor.before(this, phase);
			}
			else
			{
				visitor.after(this, phase);
			}
		}
	}
	
	
	protected @MayNotNull GraphSearchTraveledPath<V, E> createExtend(Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>> ex)
	{
		return ex.getKey().fork(subject, ex.getValue());
	}
	
	protected void executeMerges(List<Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>>> mergePairs)
	{
		Collection<GraphSearchTraveledPath<V, E>> ob = openBranches.getWriteable();
		
		for(Entry<GraphSearchTraveledPath<V, E>, GraphSearchTraveledPath<V, E>> p:mergePairs)
		{
			GraphSearchTraveledPath<V, E> m1 = p.getKey();
			GraphSearchTraveledPath<V, E> m2 = p.getValue();
			
			//TODO merge the unbounded path (which not connedted to the start or destination)
			
			//TODO merge nodes with same start/end
			
			GraphSearchTraveledPath<V, E> add = m1.merge(m2);
			
			ob.add(add);
			
			tryRemoveClosedBranch(m1);
			tryRemoveClosedBranch(m2);
		}
	}
	
	protected void tryRemoveClosedBranch(GraphSearchTraveledPath<V, E> b)
	{
		if(!b.hasUndiscoveredBranch(subject))
		{
			openBranches.getWriteable().remove(b);
			closedBranches.getWriteable().add(b);
		}
	}
	
	protected void tryRemoveClosedBranchBulk(Collection<GraphSearchTraveledPath<V, E>> bs)
	{
		Collection<GraphSearchTraveledPath<V, E>> set = new HashSet<>();
		
		for(GraphSearchTraveledPath<V, E>b:bs)
		{
			if(!b.hasUndiscoveredBranch(subject))
			{
				set.add(b);
			}
		}
		
		if(!set.isEmpty())
		{
			openBranches.getWriteable().removeAll(set);
			closedBranches.getWriteable().addAll(set);
		}
	}
	
	protected void fillPossibleExtends(List<Entry<GraphSearchTraveledPath<V, E>, Entry<E, V>>> possibleExtends)
	{
		ArrayList<Entry<E, V>> v = new ArrayList<>();
		for(GraphSearchTraveledPath<V, E> ob:openBranches.getReadOnly())
		{
			v.clear();
			
			ob.fillOpenVertexes(this, v);
			
			for(Entry<E, V> e:v)
			{
				KeyVal<GraphSearchTraveledPath<V, E>, Entry<E, V>> add = new KeyVal<>(ob, e);
				possibleExtends.add(add);
			}
		}
	}
	
	
	protected boolean examineSolutions()
	{
		List<V> s = startVertexes.getReadOnly();
		List<V> d = destinationVertexes.getReadOnly();
		
		//only if we not in surf search mode
		if(s.size() > 0 && d.size() > 0)
		{
			List<GraphSearchTraveledPath<V, E>> examinedSolutions = new ArrayList<>();
			
			for(GraphSearchTraveledPath<V, E> k:openBranches.getReadOnly())
			{
				if(s.contains(k.getFirstVertex()) && d.contains(k.getLastVertex()))
				{
					examinedSolutions.add(k);
				}
			}
			
			if(!examinedSolutions.isEmpty())
			{
				Collection<GraphSearchTraveledPath<V, E>> ob = openBranches.getWriteable();
				Collection<GraphSearchTraveledPath<V, E>> sols = this.solutions.getWriteable();
				
				for(GraphSearchTraveledPath<V, E> sol:examinedSolutions)
				{
					ob.remove(sol);
					sols.add(sol);
					if(null != visitor)
					{
						visitor.solutionFound(sol);
					}
				}
			}
		}
		
		return !controller.needContinueSearch(this);
	}

	public V getFirstDestination()
	{
		return destinationVertexes.getReadOnly().get(0);
	}

	public int getIterationCount()
	{
		return iteration;
	}
	
/**************************** extra data attachment ***************************/

	protected Map<String, Object> extraData;
	
	@Override
	public Map<String, Object> getExtraDataMap()
	{
		if(null == extraData)
		{
			extraData = new SmallMap<>();
		}
		return extraData;
	}

/******************* TODO outsource current search state **********************/
	
	@Deprecated
	public void resetSearchHistory()
	{
		openBranches.getWriteable().clear();
		closedBranches.getWriteable().clear();
		solutions.getWriteable().clear();
		
		startVertexes.getWriteable().clear();
		destinationVertexes.getWriteable().clear();
		searchStarted = false;
		noMoreSolution = false;
	}
	
	@Deprecated
	public void gc()
	{
		Set set = createSet();
		for(GraphSearchTraveledPath<V, E> cb:closedBranches.getReadOnly())
		{
			if(cb.hasFork() && !cb.hasUndiscoveredBranch(subject))
			{
				set.add(cb);
			}
		}
		
		closedBranches.getWriteable().removeAll(set);
		System.out.println("GC: "+set.size()+" closed branch removed");
	}
}
