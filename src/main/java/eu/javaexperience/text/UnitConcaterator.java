package eu.javaexperience.text;

import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

public abstract class UnitConcaterator implements SimplePublish1<String>
{
	protected String start;
	protected String end;
	protected String delimiter;
	
	protected StringBuilder sb = new StringBuilder();
	protected int nums;
	
	public UnitConcaterator(String start, String delmimiter, String end)
	{
		this.start = start;
		this.delimiter = delmimiter;
		this.end = end;
	}

	public void start()
	{
		this.nums = 0;
		sb.delete(0, sb.length());
		sb.append(start);
	}
	
	public int getAppendedUnitsCount()
	{
		return nums;
	}
	
	public void flush()
	{
		if(this.nums > 0)
		{
			this.end();
			this.start();
		}
	}
	
	@Override
	public void publish(String val)
	{
		val = filterAdd(val);
		if(null != val)
		{
			if(++this.nums > 1)
			{
				sb.append(delimiter);
			}
	
			sb.append(val);
		}
	}
	
	protected String filterAdd(String add)
	{
		return add;
	}
	
	public void end()
	{
		sb.append(end);
		commit(sb.toString(), nums);
	}
	
	protected abstract void commit(String txt, int units);
}
