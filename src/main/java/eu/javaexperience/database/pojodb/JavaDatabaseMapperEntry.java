package eu.javaexperience.database.pojodb;

import java.lang.reflect.Field;

public interface JavaDatabaseMapperEntry
{
	public Class<? extends Model> getModelledClass();
	public String getDatabaseTableName();
	
	public Field getIdField();
}