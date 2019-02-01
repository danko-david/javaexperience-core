package eu.javaexperience.exceptions;

public class UnimplementedCaseException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnimplementedCaseException()
	{
		super();
	} 

	public UnimplementedCaseException(String s)
	{
		super(s);
	}
	
	public UnimplementedCaseException(Throwable t)
	{
		super(t);
	}
	
	public UnimplementedCaseException(String arg0,Throwable arg1)
	{
		super(arg0, arg1);
	}
	
	protected Enum _case;
	
	public UnimplementedCaseException(Enum _case)
	{
		super(null == _case? "": _case.getClass().getName()+"."+_case.name());
		this._case = _case;
	}
	
	public UnimplementedCaseException(Enum _case, String s)
	{
		super(s);
		this._case = _case;
	}
	
	public UnimplementedCaseException(Enum _case, Throwable t)
	{
		super(t);
		this._case = _case;
	}
	
	public UnimplementedCaseException(Enum _case, String arg0,Throwable arg1)
	{
		super(arg0, arg1);
		this._case = _case;
	}
	
	public Enum getUnimplementedCase()
	{
		return _case;
	}
}
