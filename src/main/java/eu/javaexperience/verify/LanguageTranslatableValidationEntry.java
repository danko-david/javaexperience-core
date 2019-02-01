package eu.javaexperience.verify;

import java.util.Map;

import eu.javaexperience.log.LoggingDetailLevel;

public class LanguageTranslatableValidationEntry extends WeightedValidationReportEntry<TranslationFriendlyValidationEntry>
{
	public LanguageTranslatableValidationEntry
	(
		LoggingDetailLevel weight,
		TranslationFriendlyValidationEntry descr
	)
	{
		super(weight, descr);
	}
	
	public LanguageTranslatableValidationEntry
	(
		LoggingDetailLevel weight,
		String propertyName,
		String translationSymbol,
		Map<String, String> transaltionVariables
	)
	{
		super(weight, new TranslationFriendlyValidationEntry(propertyName, translationSymbol, transaltionVariables));
	}
}
