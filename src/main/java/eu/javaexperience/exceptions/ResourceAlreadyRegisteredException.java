package eu.javaexperience.exceptions;

public class ResourceAlreadyRegisteredException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyRegisteredException(String str)
	{
		super(str);
	}
	
	public ResourceAlreadyRegisteredException(Exception e)
	{
		super(e);
	}
	
	public ResourceAlreadyRegisteredException(String str, Exception e)
	{
		super(str,e);
	}
}