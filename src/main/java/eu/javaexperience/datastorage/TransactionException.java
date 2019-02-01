package eu.javaexperience.datastorage;

public class TransactionException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1590028201878386394L;

	public TransactionException(String msg)
	{
		super(msg);
	}
	
	public TransactionException(Exception e)
	{
		super(e);
	}
	
	public TransactionException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
}
