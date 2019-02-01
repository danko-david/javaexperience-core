package eu.javaexperience.query;

public interface ConditionInterface
{
	public LogicalGroup not(String symbol,Object obj);
	public LogicalGroup is(String symbol,Object obj);
}
