package eu.javaexperience.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FieldNameDialects
{
	public FieldNameDialect[] value();
}
