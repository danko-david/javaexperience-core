package eu.javaexperience.exceptions.checked;

public class CheckedIllegalOperationException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckedIllegalOperationException(String msg)
	{
		super(msg);
	}
	
	public CheckedIllegalOperationException(Exception ex)
	{
		super(ex);
	}
	
	public CheckedIllegalOperationException(String msg,Exception ex)
	{
		super(msg,ex);
	}
}