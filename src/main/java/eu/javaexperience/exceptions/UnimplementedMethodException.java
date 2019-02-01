package eu.javaexperience.exceptions;

public class UnimplementedMethodException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnimplementedMethodException()
	{
		super();
	} 
	
	public UnimplementedMethodException(String s)
	{
		super(s);
	}
	
	public UnimplementedMethodException(Throwable t)
	{
		super(t);
	}
	
	public UnimplementedMethodException(String arg0,Throwable arg1)
	{
		super(arg0, arg1);
	}
}