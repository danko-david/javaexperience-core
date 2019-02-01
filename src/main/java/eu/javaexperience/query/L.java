package eu.javaexperience.query;

public class L
{
	public static LogicalGroup and(LogicalGroup... grps)
	{
		return new LogicalGroup(LogicalRelation.and,grps);
	}
	
	public static LogicalGroup or(LogicalGroup... grps)
	{
		return new LogicalGroup(LogicalRelation.or,grps);
	}
	
	static LogicalGroup u(AtomicCondition cond)
	{
		return new LogicalGroup(cond);
	}
}