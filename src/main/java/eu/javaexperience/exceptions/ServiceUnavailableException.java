package eu.javaexperience.exceptions;

public class ServiceUnavailableException extends Exception
{
	private static final long serialVersionUID = 1L;

	public ServiceUnavailableException(String str)
	{
		super(str);
	}
	
	public ServiceUnavailableException(Throwable str)
	{
		super(str);
	}
	
	public ServiceUnavailableException(String str,Throwable t)
	{
		super(str,t);
	}
}