package eu.javaexperience.verify;

import eu.javaexperience.interfaces.ObjectWithProperty;
import eu.javaexperience.interfaces.ObjectWithPropertyStorage;
import eu.javaexperience.log.LoggingDetailLevel;

public class WeightedValidationReportEntry<T> implements ObjectWithProperty
{
	protected final LoggingDetailLevel weight;
	protected final T description;
	
	public WeightedValidationReportEntry(LoggingDetailLevel weight, T descr)
	{
		this.weight = weight;
		this.description = descr;
	}

	public LoggingDetailLevel getWeight()
	{
		return weight;
	}

	public T getDescription()
	{
		return description;
	}
	
	@Override
	public String toString()
	{
		return "WeightedValidationReportEntry: weight:"+weight+", description: "+description;
	}
	
	protected static ObjectWithPropertyStorage<WeightedValidationReportEntry> PROPS = new ObjectWithPropertyStorage<>();
	
	static
	{
		PROPS.addExaminer("weight", (e)->e.weight.getLabel());
		PROPS.addExaminer("description", (e)->e.description);
	}
	
	@Override
	public Object get(String key)
	{
		return PROPS.get(this, key);
	}

	@Override
	public String[] keys()
	{
		return PROPS.keys();
	}
}