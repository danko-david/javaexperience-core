package eu.javaexperience.database.accessModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Cél: ismétlődő lekérdezések egyszerűsítése, rendezéssel, szűréssel.
 * 
 * */
public abstract class DataAccessModel<M, ENV>
{
	protected final String name;
	
	protected Map<String, OptionGroup> queryOptions = new HashMap<>();
	
	
	public String getName()
	{
		return name;
	}
	
	public DataAccessModel(String modelName)
	{
		this.name = modelName;
	}		
	
	public DataAccessModel
	(
		String modelName,
		OptionGroup... options
	)
	{
		this.name = modelName;
		for(OptionGroup g:options)
		{
			queryOptions.put(g.systemName, g);
		}
	}
	
	protected abstract AccessRequest<M, ENV> createAccessRequest();
	
	public AccessRequest<M, ENV> createRequest()
	{
		AccessRequest<M, ENV> req = createAccessRequest();
		req.model = this;
		return req;
	}
	
	
	public void addOption(OptionGroup optionGroup)
	{
		queryOptions.put(optionGroup.systemName, optionGroup);
	}

	
	/**
	 * fills the list with the available options and returns the number of
	 * filled options.
	 * returns -1 if OptionGroup not available
	 * 
	 * */
	public int fillAvailableOptionsOf(String entity, Collection<OptionEntry> orderings)
	{
		OptionGroup og = queryOptions.get(entity);
		if(null == og)
		{
			return -1;
		}
		
		int n = 0;
		for(OptionEntry f:og.options)
		{
			orderings.add(f);
			++n;
		}
		
		return n;
	}
	
	public int fillAvailableOptionsOf(WellKnownAccessQueryOptions entitiy, Collection<OptionEntry> options)
	{
		return fillAvailableOptionsOf(entitiy.name(), options);
	}
	
	public OptionGroup getOptionByName(String name)
	{
		return queryOptions.get(name);
	}

	public Iterable<String> getOptionNames()
	{
		return queryOptions.keySet();
	}
}