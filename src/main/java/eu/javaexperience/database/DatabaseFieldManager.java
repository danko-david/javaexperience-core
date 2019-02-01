package eu.javaexperience.database;

import java.lang.reflect.Field;

import eu.javaexperience.database.pojodb.Model;

public class DatabaseFieldManager
{
	protected final String tableName;
	protected final Field[] SQL_FIELDS;
	protected final Field ID_FIELD;
	
	public DatabaseFieldManager(Class<? extends Model> cls, String table, String id_field)
	{
		this.tableName = table;
		SQL_FIELDS = JDBC.simpleSelectClassSqlFileds(cls);
		ID_FIELD = whereFieldName(SQL_FIELDS, id_field);
	}

	public static Field whereFieldName(Field[] sqlFields, String name)
	{
		for(Field f: sqlFields)
		{
			if(name.equals(f.getName()))
			{
				return f;
			}
		}
		
		throw new RuntimeException("Field "+name+" not found.");
	}

	public String getTable()
	{
		return tableName;
	}


	public Field[] getFields()
	{
		return SQL_FIELDS;
	}


	public Field getIdField()
	{
		return ID_FIELD;
	}
}
