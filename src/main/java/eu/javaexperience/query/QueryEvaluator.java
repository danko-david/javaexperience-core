package eu.javaexperience.query;

import java.util.Collection;

import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;

public class QueryEvaluator<T>
{
	protected GetBy2<Boolean, T, AtomicCondition> unitMatcher;
	
	public QueryEvaluator(GetBy2<Boolean, T, AtomicCondition> unitMatcher)
	{
		this.unitMatcher = unitMatcher;
	}
	
	public boolean eval(T element, LogicalGroup query)
	{
		LogicalRelation rel = query.getLogicalRelation();
		switch(rel)
		{
		case and:
			for(LogicalGroup g:query.getLogicalGroups())
			{
				if(!eval(element, g))
				{
					return false;
				}
			}
			return true;
			
		case or:
			for(LogicalGroup g:query.getLogicalGroups())
			{
				if(eval(element, g))
				{
					return true;
				}
			}
			return false;
			
		case unit:
			return unitMatcher.getBy(element, query.getAtomicCondition());
			
			default: throw new UnimplementedCaseException(rel);
		}
	}
	
	public int select(Collection<T> dst, Collection<T> from, LogicalGroup query)
	{
		int n=0;
		
		for(T f:from)
		{
			if(eval(f, query))
			{
				dst.add(f);
				++n;
			}
		}
		
		return n;
	}
}
