package eu.javaexperience.measurement;

public class AvgStdDev
{
	protected double avg;
	protected double dev;
	
	public AvgStdDev(double avg, double dev)
	{
		this.avg = avg;
		this.dev = dev;
	}
	
	protected double getAverage()
	{
		return avg;
	}
	
	protected double getStandardDeviation()
	{
		return dev;
	}
	
}
