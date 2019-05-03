package eu.javaexperience.database.pojodb;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.ConnectionPoolDataSource;

import eu.javaexperience.database.ConnectionCreator;
import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.JdbcConnectionPool;
import eu.javaexperience.reflect.Mirror;

public class SqlDatabase implements Database
{
	protected ConnectionCreator cc;
	
	protected JdbcConnectionPool pool;
	
	public SqlDatabase(ConnectionCreator cc)
	{
		this.cc = cc;
		this.pool = new JdbcConnectionPool(cc);
	}
	
	@Override
	public <T extends Model> T getInstanceById(Class<T> cls, Object id) throws SQLException, InstantiationException, IllegalAccessException
	{
		return getInstance(cls, "id", id);
	}
	
	public <T extends Model> T getInstance(Class<T> cls, String field, Object id) throws SQLException, InstantiationException, IllegalAccessException
	{
		T ret = cls.newInstance();
		try(Connection conn = pool.getConnection())
		{
			try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM `"+ret.getTable()+"` WHERE `"+field+"`= ?"))
			{
				ps.setObject(1, id);
				ResultSet rs = ps.executeQuery();
				if(!rs.next())
				{
					return null;
				}
				else
				{
					JDBC.simpleReadIntoJavaObject(rs, ret.getFields(), ret);
				}
			}
		}
		return ret;
	}
	
	public JdbcConnectionPool getPool()
	{
		return pool;
	}
	
	@Override
	public Connection getConnection() throws SQLException
	{
		return pool.getConnection();
	}

	@Override
	public void insert(Model m) throws SQLException
	{
		try(Connection conn = pool.getConnection())
		{
			try
			{
				JDBC.simpleInsertIntoTableFromJavaObject(conn, m.getFields(), m.getTable(), m, m.getIdField());
			}
			catch (Exception e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
			}
		}
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
		
		return null;
	}

	@Override
	public void updateById(Model m) throws SQLException
	{
		try(Connection conn = pool.getConnection())
		{
			try
			{
				JDBC.simpleUpdateTableFromJavaObject(conn, m.getFields(), m.getTable(), m, m.getIdField().getName()+" =? ", m.getIdField().get(m));
			}
			catch (Exception e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
			}
		}
	}

	@Override
	public void delete(Model m) throws SQLException
	{
		try(Connection conn = pool.getConnection())
		{
			try
			{
				JDBC.executePrepared(conn, "DELETE FROM `"+m.getTable()+"` WHERE `"+m.getIdField().getName()+"` = ? ", m.getIdField().get(m));
			}
			catch (IllegalAccessException | IllegalArgumentException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
			}
		}
	}

	public <T extends Model> int getAllInstance(Class<T> cls, Collection<T> dst) throws InstantiationException, IllegalAccessException, SQLException
	{
		int nums = 0;
		T ret = cls.newInstance();
		try(Connection conn = pool.getConnection())
		{
			try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM `"+ret.getTable()+"`"))
			{
				ResultSet rs = ps.executeQuery();
				while(rs.next())
				{
					ret = cls.newInstance();
					JDBC.simpleReadIntoJavaObject(rs, ret.getFields(), ret);
					dst.add(ret);
					++nums;
				}
			}
		}
		
		return nums;
	}
}
