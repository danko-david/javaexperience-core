package eu.javaexperience.reflect;

//import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(FieldNameDialects.class)
public @interface FieldNameDialect
{
	public String dialect();
	public String name();
}
