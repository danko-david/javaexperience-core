package eu.javaexperience.exceptions;

public class IllegalOperationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public IllegalOperationException(String str)
	{
		super(str);
	}
	
	public IllegalOperationException(Exception e)
	{
		super(e);
	}
	
	public IllegalOperationException(String str, Exception e)
	{
		super(str,e);
	}	
}
