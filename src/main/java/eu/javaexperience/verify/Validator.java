package eu.javaexperience.verify;

import java.util.Collection;

public interface Validator<T, R>
{
	public boolean validate(T subject, Collection<R> reportDestination);
}
