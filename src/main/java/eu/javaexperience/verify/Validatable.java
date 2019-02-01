package eu.javaexperience.verify;

import java.util.Collection;

public interface Validatable<V>
{
	/**
	 * Validates the underlying unit and:
	 *  - returns true if unit are valid.
	 *  - fills the errors and warnings into the given collection.
	 * 
	 * if true returned, the unit is valid and can be used but even in this case
	 * warnings are permitted to be written into the collection.
	 * */
	public boolean validate(Collection<V> errors_and_warnings);
}
