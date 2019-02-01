package eu.javaexperience.data.build;

import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.data.DataPhenomenon;
import eu.javaexperience.data.DataPhenomenonClass;
import eu.javaexperience.data.DataPhenomenonEnvironment;
import eu.javaexperience.verify.EnvironmentDependValidator;

public class BuiltDataPhenomenon implements DataPhenomenon
{
	protected final int fieldDataLength;
	protected final DataPhenomenonClass dataClass;
	protected final EnvironmentDependValidator<DataPhenomenonEnvironment, String, String> validator;
	
	public BuiltDataPhenomenon
	(
		int fieldDataLength,
		DataPhenomenonClass dataClass,
		EnvironmentDependValidator<DataPhenomenonEnvironment, String, String> validator
	)
	{
		this.fieldDataLength = fieldDataLength;
		this.dataClass = dataClass;
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
	public int getFieldDataLength()
	{
		return fieldDataLength;
	}

	@Override
	public DataPhenomenonClass getDataClass()
	{
		return dataClass;
	}

	@Override
	public EnvironmentDependValidator<DataPhenomenonEnvironment, String, String> getValidator()
	{
		return validator;
	}
}
