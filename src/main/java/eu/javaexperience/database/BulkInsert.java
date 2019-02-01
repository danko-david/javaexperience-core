package eu.javaexperience.database;

import java.sql.Connection;
import java.sql.SQLException;

import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.UnitConcaterator;

public class BulkInsert extends UnitConcaterator
{
	protected Connection conn;
	public BulkInsert(Connection conn, String start, String end)
	{
		super(start, ", ", end);
		this.conn = conn;
	}

	@Override
	public void commit(String txt, int count)
	{
		if(count > 0)
		{
			try
			{
				JDBC.execute(conn, txt);
			}
			catch(SQLException e)
			{
				Mirror.propagateAnyway(e);
			}
		}
	}
}