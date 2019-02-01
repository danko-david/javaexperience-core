package eu.javaexperience.data.build;

import eu.javaexperience.patterns.creational.builder.unit.BuildFields;

public enum DataPhenomenonFields implements BuildFields
{
	fieldDataLength,
	dataClass,
	validator
	
	
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
