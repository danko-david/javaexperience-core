package eu.javaexperience.classes;

import java.lang.annotation.Annotation;

import eu.javaexperience.interfaces.ExternalDataAttached;

public interface ClassAnnotationDescriptor extends ExternalDataAttached
{
	public String getType();
}
