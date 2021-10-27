package eu.javaexperience.classes;

import java.util.Map;

import eu.javaexperience.interfaces.ExternalDataAttached;

public interface ClassAnnotationDescriptor extends ExternalDataAttached
{
	public String getType();
	public Map<String, Object> getAnnotationData();
}
