package eu.javaexperience.data.build;

import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.data.DataPhenomenon;
import eu.javaexperience.data.DataPhenomenonEnvironment;
import eu.javaexperience.data.DataPhenomenonInstance;
import eu.javaexperience.data.DataPhenomenonUnit;
import eu.javaexperience.verify.EnvironmentDependValidator;

public class BuiltDataPhenomenonInstance implements DataPhenomenonInstance
{
	protected final DataPhenomenon phenomenon;
	protected final String name;
	protected final boolean required;
	protected final EnvironmentDependValidator<DataPhenomenonEnvironment, DataPhenomenonUnit, String> validator;
	
	public BuiltDataPhenomenonInstance
	(
		DataPhenomenon dataPhenomenon,
		String name,
		boolean req,
		EnvironmentDependValidator<DataPhenomenonEnvironment, DataPhenomenonUnit, String> validator
	)
	{
		this.phenomenon = dataPhenomenon;
		this.name = name;
		this.required = req;
		this.validator = validator;
	}
	
	protected static Map<String, Object> passOrInit(Map<String, Object> map)
	{
		if(null != map)
		{
			return map;
		}
		
		return new HashMap<>();
	}
	
	protected Map<String, Object> map;
	
	@Override
	public Map<String, Object> getExtraDataMap()
	{
		return passOrInit(map);
	}

	@Override
	public DataPhenomenon getPhenomenon()
	{
		return phenomenon;
	}

	@Override
	public String getFieldName()
	{
		return name;
	}

	@Override
	public boolean isRequired()
	{
		return required;
	}

	@Override
	public EnvironmentDependValidator<DataPhenomenonEnvironment, DataPhenomenonUnit, String> getValidator()
	{
		return validator;
	}
}
