package eu.javaexperience.algorithm.search.graph;

import java.util.Iterator;
import java.util.NoSuchElementException;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.exceptions.IllegalOperationException;
import eu.javaexperience.reflect.Mirror;

public class GraphPath<V, E> implements Cloneable, Iterable<GraphSearchVertexTransition<V, E>>
{
	protected GraphSearchVertexTransition<V, E> first;
	protected GraphSearchVertexTransition<V, E> last;
	protected int transitions = 0;
	
	
	public GraphSearchVertexTransition<V, E> addInitialVertext(V init)
	{
		AssertArgument.assertNotNull(init, "Vertex");
		if(null != first || null != last)
		{
			throw new IllegalStateException("GraphPath is already initalized");
		}
		
		++transitions;
		
		 first = last = new GraphSearchVertexTransition<>(this, init, null, init);
		 return last;
	}
	
	@Override
	protected GraphPath<V, E> clone() throws CloneNotSupportedException
	{
		GraphPath<V, E> ret = (GraphPath<V, E>) super.clone();
		
		GraphSearchVertexTransition<V, E> crnt = first;
		if(null != crnt)
		{
			GraphSearchVertexTransition<V, E> build = (GraphSearchVertexTransition<V, E>) crnt.clone();
			ret.first = build;
			build.setOwner(ret);
			while(null != (crnt = crnt.next))
			{
				GraphSearchVertexTransition<V, E> n = (GraphSearchVertexTransition<V, E>) crnt.clone();
				n.setOwner(ret);
				
				build.next = n;
				n.prev = build;
				
				
				build = n;
			}
			
			ret.last = build;
		}
		
		return ret;
	}
	
	protected void registerFirst(GraphSearchVertexTransition<V, E> first)
	{
		if(first.next != this.first)
		{
			throw new IllegalOperationException("Bad chaining, new first Node is not pointed to the current first Node");
		}
		++transitions;
		this.first = first;
	}
	
	public void registerLast(GraphSearchVertexTransition<V, E> last)
	{
		if(last.prev != this.last)
		{
			throw new IllegalOperationException("Bad chaining, new last Node is not pointed to the current last Node");
		}
		++transitions;
		this.last = last;
	}

	public int getTransitionsCount()
	{
		return transitions;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("GraphPath: ");
		
		GraphSearchVertexTransition<V, E> crnt = first;
		if(null != crnt)
		{
			sb.append("[");
			sb.append(crnt.getFrom());
			sb.append("]");
			
			if(!crnt.isInitial())
			{
				do
				{
					sb.append(" ---");
					sb.append(crnt.getEdge());
					sb.append("---> [");
					sb.append(crnt.getTo());
					sb.append("]");
				}
				while(null != (crnt = crnt.next));
			}
		}
		
		return sb.toString();
	}
	
	protected void reOwn(GraphPath<V, E> owner)
	{
		GraphSearchVertexTransition<V, E> crnt = first;
		if(null != crnt)
		{
			crnt.setOwner(owner);
			while(null != (crnt = crnt.next))
			{
				crnt.setOwner(owner);
			}
		}
	}
	
	
	public GraphPath<V, E> merge(GraphPath<V, E> with)
	{
		if(!last.getTo().equals(with.first.getFrom()))
		{
			throw new IllegalOperationException("Can't merge path "+this+" to "+with);
		}
		
		try
		{
			GraphPath<V, E> ret = this.clone();
			GraphPath<V, E> w = with.clone();
			
			ret.last.next = w.first;
			ret.last = w.last;
			w.reOwn(ret);			
			ret.transitions = transitions+w.transitions;
			
			return ret;
		}
		catch (CloneNotSupportedException e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}

	public GraphSearchVertexTransition<V, E> getFirst()
	{
		return first;
	}

	public GraphSearchVertexTransition<V, E> getLast()
	{
		return last;
	}
	
	@Override
	public Iterator<GraphSearchVertexTransition<V, E>> iterator()
	{
		return new Iterator<GraphSearchVertexTransition<V,E>>()
		{
			GraphSearchVertexTransition<V, E> crnt = null;
			
			protected GraphSearchVertexTransition<V, E> getNext()
			{
				return null == crnt?first:crnt.next;
			}
			
			@Override
			public boolean hasNext()
			{
				return null != getNext();
			}

			@Override
			public GraphSearchVertexTransition<V, E> next()
			{
				crnt = getNext();
				if(null == crnt)
				{
					throw new NoSuchElementException("GraphPath has no more Transition");
				}
				return crnt;
			}
		};
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
		{
			return true;
		}
		
		if(!(obj instanceof GraphPath))
		{
			return false; 
		}
		
		GraphPath other = (GraphPath) obj;
		
		if(transitions != other.transitions)
		{
			return false;
		}
		
		if(!first.equals(other.first))
		{
			return false;
		}
		
		if(!last.equals(other.last))
		{
			return false;
		}
		
		GraphSearchVertexTransition<V, E> tr = first;
		GraphSearchVertexTransition<V, E> otr = other.first;
		
		while(null != tr)
		{
			if(!tr.equals(otr))
			{
				return false;
			}
			tr = tr.next;
		}
		
		return true;
	}
	
	protected int hashCode = 0;
	
	@Override
	public int hashCode()
	{
		if(0 == hashCode)
		{
			int ret = 27;
			
			GraphSearchVertexTransition<V, E> tr = first;
			
			while(null != tr)
			{
				ret = 31* ret + tr.hashCode();
				tr = tr.next;
			}
			
			hashCode = ret;
		}
		return hashCode;
	}
}
