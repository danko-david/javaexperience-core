package eu.javaexperience.verify;

public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	protected TranslationFriendlyValidationEntry res;
	
	public ValidationException(TranslationFriendlyValidationEntry res)
	{
		this.res = res;
	}
	
	public TranslationFriendlyValidationEntry getResult()
	{
		return res;
	}
}
