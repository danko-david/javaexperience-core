package eu.javaexperience.exceptions;

public class UnsupportedMethodException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedMethodException()
	{
		super();
	} 
	
	public UnsupportedMethodException(String s)
	{
		super(s);
	}
	
	public UnsupportedMethodException(Throwable t)
	{
		super(t);
	}
	
	public UnsupportedMethodException(String arg0,Throwable arg1)
	{
		super(arg0, arg1);
	}
}