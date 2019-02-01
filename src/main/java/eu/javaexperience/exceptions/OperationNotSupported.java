package eu.javaexperience.exceptions;

public class OperationNotSupported extends Exception
{
	private static final long serialVersionUID = 1L;

	public OperationNotSupported(String msg)
	{
		super(msg);
	}
	
	public OperationNotSupported(Exception e)
	{
		super(e);
	}
	
	public OperationNotSupported(String msg, Throwable t)
	{
		super(msg, t);
	}
}