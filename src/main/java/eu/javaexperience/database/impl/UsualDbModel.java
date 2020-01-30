package eu.javaexperience.database.impl;

import java.lang.reflect.Field;

import eu.javaexperience.database.impl.SqlMappingData.FieldData;
import eu.javaexperience.database.pojodb.Model;
import eu.javaexperience.reflect.Mirror;

public abstract class UsualDbModel implements Model
{
	@Override
	public String toString()
	{
		return Mirror.usualToString(this);
	}
	
	@Override
	public abstract String getTable();
	
	public Field getFieldByName(String name)
	{
		FieldData f = SqlMappingData.getOrCreateMapping(this).fieldNames.get(name);
		if(null == f)
		{
			return null;
		}
		return f.field;
	}
	
	@Override
	public Field[] getFields()
	{
		return SqlMappingData.getOrCreateMapping(this).fields;
	}

	@Override
	public Field getIdField()
	{
		FieldData fd = SqlMappingData.getOrCreateMapping(this).id;
		if(null == fd)
		{
			return null;
		}
		return fd.field;
	}

	public Object getId()
	{
		try
		{
			return getIdField().get(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}