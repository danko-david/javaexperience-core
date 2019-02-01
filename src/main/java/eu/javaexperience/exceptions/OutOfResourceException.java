package eu.javaexperience.exceptions;

public class OutOfResourceException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OutOfResourceException(String str)
	{
		super(str);
	}
	
	public OutOfResourceException(Exception e)
	{
		super(e);
	}
	
	public OutOfResourceException(String str, Exception e)
	{
		super(str,e);
	}
}