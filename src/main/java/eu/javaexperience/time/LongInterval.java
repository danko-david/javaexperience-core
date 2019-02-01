package eu.javaexperience.time;

public class LongInterval
{
	public final long min;
	public final long max;
	
	public LongInterval(long min,long max)
	{
		if(min > max)
			throw new IllegalArgumentException("min nem lehet nagyobb mint max");
		
		this.min = min;
		this.max = max;
	}

	public boolean isBetween(long val)
	{
		return min <= val && val <= max;
	}
	
	public boolean isBetween(long min,long max)
	{
		return this.min <= min && max <= this.max;
	}
	
}