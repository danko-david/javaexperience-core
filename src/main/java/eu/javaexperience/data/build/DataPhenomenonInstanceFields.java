package eu.javaexperience.data.build;

import eu.javaexperience.patterns.creational.builder.unit.BuildFields;

public enum DataPhenomenonInstanceFields implements BuildFields
{
	phenomenon, name, required, validator
	
	
	;

	@Override
	public String getFielName()
	{
		return name();
	}

	@Override
	public boolean isRequired()
	{
		return true;
	}
}
