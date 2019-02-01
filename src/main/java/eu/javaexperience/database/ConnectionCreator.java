package eu.javaexperience.database;

import java.sql.Connection;
import java.sql.SQLException;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimpleGet;
import eu.javaexperience.reflect.Mirror;

public abstract class ConnectionCreator implements SimpleGet<Connection>
{
	public static ConnectionCreator fromConnectionBuilder(final ConnectionBuilder builder, final String host, final int port, final String user, final String password, final String db)
	{
		AssertArgument.assertNotNull(builder, "type");
		//AssertArgument.assertNotNull(user, "username");
		//AssertArgument.assertNotNull(password, "password");
		//AssertArgument.assertNotNull(db, "db");
		
		return new ConnectionCreator()
		{
			@Override
			public Connection get()
			{
				try
				{
					if(port < 0)
					{
						if(host == null)
						{
							return builder.openConnection(user, password, db);
						}
						else
						{
							return builder.openConnection(host, user, password, db);
						}
					}
					else
					{
						return builder.openConnection(host, port, user, password, db);
					}
				}
				catch (SQLException e)
				{
					Mirror.propagateAnyway(e);
					return null;
				}
			}
		};
	}
}
