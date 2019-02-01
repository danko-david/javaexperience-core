package eu.javaexperience.data;

public class DataPhenomenonTools
{
	public static <T> T examineExtraAttribute(DataPhenomenonInstance instance, String key)
	{
		T ret = (T) instance.getExtraDataMap().get(key);
		if(null != ret)
		{
			return ret;
		}
		
		ret = (T) instance.getPhenomenon().getExtraDataMap().get(key);
		if(null != ret)
		{
			return ret;
		}
		
		return null;
	}
}
