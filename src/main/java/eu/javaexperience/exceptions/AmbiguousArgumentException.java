package eu.javaexperience.exceptions;

public class AmbiguousArgumentException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AmbiguousArgumentException(String msg)
	{
		super(msg);
	}
	
	public AmbiguousArgumentException(Exception e)
	{
		super(e);
	}
}