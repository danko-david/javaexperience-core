package eu.javaexperience.interfaces;

import java.util.Map;

public interface ExternalDataAttached
{
	public Map<String, Object> getExtraDataMap();
	
	/*
	protected Map<String, Object> extraData;
	
	@Override
	public Map<String, Object> getExtraDataMap()
	{
		if(null == extraData)
		{
			extraData = new SmallMap<>();
		}
		return extraData;
	}
	 */
}
