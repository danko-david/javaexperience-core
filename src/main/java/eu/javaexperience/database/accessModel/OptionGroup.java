package eu.javaexperience.database.accessModel;

import java.util.ArrayList;
import java.util.List;

import eu.javaexperience.reflect.Mirror;

public class OptionGroup
{
	protected String userFriendlyName;
	protected String systemName;
	protected List<OptionEntry> options = new ArrayList<>();
	
	public OptionGroup(WellKnownAccessQueryOptions systemName, String userFiendlyName, OptionEntry... options)
	{
		this(systemName.name(), userFiendlyName, options);
	}
	
	public OptionGroup(String systemName, String userFiendlyName, OptionEntry... options)
	{
		this.systemName = systemName;
		this.userFriendlyName = userFiendlyName;
		for(OptionEntry o:options)
		{
			this.options.add(o);
			o.owner = this;
		}
	}

	public String getSystemName()
	{
		return systemName;
	}

	public void addOptions(OptionEntry... opt)
	{
		for(OptionEntry o:opt)
		{
			this.options.add(o);
			o.owner = this;
		}
	}

	public void overrideOptions(OptionEntry... opts)
	{
		options.clear();
		addOptions(opts);
	}

	public boolean isValuePermitted(String val)
	{
		for(OptionEntry e:options)
		{
			if(Mirror.equals(val, e.optionAddress))
			{
				return true;
			}
		}
		
		return false;
	}
}
