package eu.javaexperience.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FunctionVariableDescription
{
	public String paramName();
	public String description();
	public boolean mayNull();
	public Class<?> type();
	public Class<?> autocomplete() default Object.class;
}
