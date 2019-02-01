package eu.javaexperience.exceptions;

public class OperationNeverEndsException extends Exception
{

	public OperationNeverEndsException(String str)
	{
		super(str);
	}
	
	public OperationNeverEndsException(Throwable str)
	{
		super(str);
	}
	
	public OperationNeverEndsException(String str,Throwable t)
	{
		super(str,t);
	}
}
