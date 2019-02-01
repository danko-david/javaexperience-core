package eu.javaexperience.verify;

import java.util.Map;

public class TranslationFriendlyValidationEntry
{
	public final String propertyName;
	public final String translationSymbol;
	public final Map<String, String> translationVariables;
	
	public TranslationFriendlyValidationEntry
	(
		String propertyName,
		String translationSymbol,
		Map<String, String> transaltionVariables
	)
	{
		this.propertyName = propertyName;
		this.translationSymbol = translationSymbol;
		this.translationVariables = transaltionVariables;
	}
}
