package eu.javaexperience.verify;

import java.util.Collection;

public interface EnvironmentDependValidator<E, T, R>
{
	public boolean validate(E env, T subject, Collection<R> reportDestination);
}
