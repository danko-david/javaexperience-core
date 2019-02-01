package eu.javaexperience.math;

import java.util.List;

public class MathTools
{
	public static int clamp(int min, int value, int max)
	{
		if(max < min)
		{
			int tmp = min;
			min = max;
			max = tmp;
		}
		
		if(value < min)
		{
			value = min;
		}
		
		if(value > max)
		{
			value = max;
		}
		
		return value;
	}

	public static boolean inRange(Number Reference, Number Check, Number Range)
	{
		if(null == Reference || null == Check)
		{
			return false;
		}
		
		double range = 0.0;
		
		if(null != Range)
		{
			range = Range.doubleValue();
		}
		
		double ref = Reference.doubleValue();
		double check = Check.doubleValue();
		
		return check-range <= ref && ref <= check+range;
	}
	
	public static boolean inRelativeRange(Number Reference, Number Check, Number RangePercent)
	{
		if(null == Reference || null == Check)
		{
			return false;
		}
		
		double range = 0.0;
		
		double ref = Reference.doubleValue();
		double check = Check.doubleValue();
		
		if(null != RangePercent)
		{
			range = RangePercent.doubleValue()*ref;
		}
		
		return check-range <= ref && ref <= check+range;
	}

	
	public static boolean inRange(long ref, long check, long range)
	{
		return check-range <= ref && ref <= check+range;
	}
	/**
	 * 
	 * @param ids
	 * @return highest values in array or Integer.MIN_VAL
	 */
	public static int getMax(int[] ids)
	{
		int ret = Integer.MIN_VALUE;
		for(int i=0;i<ids.length;++i)
		{
			int val = ids[i];
			if(ret < val)
			{
				ret = val;
			}
		}
		return ret;
	}

	public static Long getMaxLong(List<? extends Number> lst)
	{
		boolean assigned = false;
		long ret = 0;
		for(Number n: lst)
		{
			if(null != n)
			{
				if(!assigned)
				{
					ret = n.longValue();
					assigned = true;
				}
				else
				{
					long l = n.longValue();
					if(ret < l)
					{
						ret = l;
					}
				}
			}
		}
		
		return assigned?ret:null;
	}

	public static double clamp(double min, double value, double max)
	{
		if(max < min)
		{
			double tmp = min;
			min = max;
			max = tmp;
		}
		
		if(value < min)
		{
			value = min;
		}
		
		if(value > max)
		{
			value = max;
		}
		
		return value;
	}

	public static Double sawTeeth(double min, double step, double max, double val)
	{
		return MathTools.clamp(min, min+ ((val*step) % ((max-min)+1)), max);
	}

	public static int linearSawteethBetween(int min, int intValue, int max)
	{
		//clamp is for possible negative numbers
		return MathTools.clamp(min, min+ (intValue % ((max-min)+1)), max);
	}

	public static int randomBetween(int min, int max)
	{
		return (int) (min+Math.random()*(max-min));
	}
}
