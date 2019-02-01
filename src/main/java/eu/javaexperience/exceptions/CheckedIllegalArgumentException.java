package eu.javaexperience.exceptions;

public class CheckedIllegalArgumentException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckedIllegalArgumentException(String msg)
	{
		super(msg);
	}
	
	public CheckedIllegalArgumentException(Exception e)
	{
		super(e);
	}
}