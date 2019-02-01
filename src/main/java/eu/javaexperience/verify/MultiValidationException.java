package eu.javaexperience.verify;

import java.util.List;

public class MultiValidationException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	protected List<TranslationFriendlyValidationEntry> res;
	
	public MultiValidationException(List<TranslationFriendlyValidationEntry> ents)
	{
		this.res = ents;
	}
	
	public List<TranslationFriendlyValidationEntry> getResult()
	{
		return res;
	}
}
