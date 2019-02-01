package eu.javaexperience.verify;

public class ValidationResultException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ValidationResult res;
	
	public ValidationResultException(ValidationResult res)
	{
		this.res = res;
	}
	
	public <T> ValidationResult<T> getResult()
	{
		return res;
	}
}
