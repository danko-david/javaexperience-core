package eu.javaexperience.verify;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult<T>
{
	public boolean valid;
	public long id;
	public List<T> reportEntries = new ArrayList<>();
}
