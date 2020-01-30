package eu.javaexperience.database.annotations.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import eu.javaexperience.database.pojodb.Model;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModelAssociation
{
	public String associationName();
	public String localField();
	public Class<? extends Model> foreingClass();
	public String foreingField();
}
