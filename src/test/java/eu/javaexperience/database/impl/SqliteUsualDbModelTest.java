package eu.javaexperience.database.impl;

import java.io.File;

import eu.javaexperience.database.ConnectionBuilder;
import eu.javaexperience.database.ConnectionCreator;
import eu.javaexperience.database.pojodb.SqlDatabase;
import eu.javaexperience.database.pojodb.dialect.SqliteDialect;

public class SqliteUsualDbModelTest extends UsualDbModelTest
{
	protected static String deleteOnExit(String file)
	{
		new File(file).deleteOnExit();
		return file;
	}

	@Override
	public SqlDatabase createConnection(boolean resetDatabase) throws Exception
	{
		return new SqlDatabase
		(
			ConnectionCreator.fromConnectionBuilder
			(
				ConnectionBuilder.sqlite, 
				"", -1, "", "",
				deleteOnExit("/tmp/test."+System.currentTimeMillis()+".sqlite")
			),
			new SqliteDialect()
		);
	}

}
