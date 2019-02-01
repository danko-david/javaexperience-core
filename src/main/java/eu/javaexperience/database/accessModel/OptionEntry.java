package eu.javaexperience.database.accessModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OptionEntry
{
	public OptionEntry(String userFriendlyName,String optionAddress, OptionEntry... values)
	{
		this.optionUserFreindlyName = userFriendlyName;
		this.optionAddress = optionAddress;
		for(OptionEntry v:values)
		{
			optionValues.add(v);
		}
	}
	
	protected String optionUserFreindlyName;
	
	protected String optionAddress;
	protected OptionGroup owner;
	
	protected List<OptionEntry> optionValues = new ArrayList<>();

	public String getOptionUserFreindlyName()
	{
		return optionUserFreindlyName;
	}
	
	public String getOptionAddress()
	{
		return optionAddress;
	}
	
	public void fillValues(Collection<OptionEntry> opts)
	{
		for(OptionEntry o:optionValues)
		{
			opts.add(o);
		}
	}
	
	protected Object etc;
	
	public OptionEntry setEtcData(Object o)
	{
		this.etc = o;
		return this;
	}
	
	public Object getEtcData()
	{
		return etc;
	}

	public OptionGroup getGroup()
	{
		return owner;
	}
}
