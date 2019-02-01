package eu.javaexperience.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionDescription
{
	public String functionDescription();
	
	public FunctionVariableDescription returning();
	public FunctionVariableDescription[] parameters();
	
}
