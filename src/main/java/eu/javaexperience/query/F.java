package eu.javaexperience.query;

public enum F implements ConditionInterface 
{
	eq,
	lt,
	gt,
	gte,
	lte,
	contains,
	match,
	in,
	;

	@Override
	public LogicalGroup not(String symbol, Object obj)
	{
		return L.u(new AtomicCondition(this, true, symbol, obj));
	}

	@Override
	public LogicalGroup is(String symbol, Object obj)
	{
		return L.u(new AtomicCondition(this, false, symbol, obj));
	}
}

