package eu.javaexperience.database.pojodb;

import java.lang.reflect.Field;

public interface Model
{
	public String getTable();
	public Field[] getFields();
	public Field getIdField();
}
