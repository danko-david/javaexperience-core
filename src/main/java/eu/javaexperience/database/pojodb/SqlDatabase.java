package eu.javaexperience.database.pojodb;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.database.ConnectionCreator;
import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.JdbcConnectionPool;
import eu.javaexperience.reflect.CastTo;
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
	
	public <T extends Model> int getInstances(Class<T> cls, Collection<T> dst, String query, Object... values) throws SQLException, InstantiationException, IllegalAccessException
	{
		T ret = cls.newInstance();
		Field[] fs = ret.getFields();
		int n = 0;
		try(Connection conn = pool.getConnection())
		{
			try(PreparedStatement ps = conn.prepareStatement(query))
			{
				for(int i=0;i<values.length;++i)
				{
					ps.setObject(i+1, values[i]);
				}
				
				ResultSet rs = ps.executeQuery();
				while(rs.next())
				{
					T add = cls.newInstance();
					JDBC.simpleReadIntoJavaObject(rs, fs, add);
					dst.add(add);
					++n;
				}
			}
		}
		return n;
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
				Field id = m.getIdField();
				Field[] fields = m.getFields();
				if(null != id)
				{
					fields = ArrayTools.withoutElementIdentically(m.getFields(), id);
				}
				Map<String, Object> res = JDBC.simpleInsertIntoTableFromJavaObjectResultInsertion(conn, fields, m.getTable(), m);
				
				if(null != id && null != res)
				{
					CastTo cast = CastTo.getCasterRestrictlyForTargetClass(id.getType());
					Object vId = res.get("GENERATED_KEY");
					if(null == vId)
					{
						throw new RuntimeException("No generated id returned after insertion: "+m);
					}
					
					if(null == cast)
					{
						throw new RuntimeException("Unmanagable id type :"+id);
					}
					
					Object set = cast.cast(vId);
					
					if(null == set)
					{
						throw new RuntimeException("Can't cast generated id for raget type. id: "+vId+", field and type: "+id);
					}
					
					id.set(m, set);
				}
			}
			catch (Exception e)
			{
				Mirror.propagateAnyway(e);
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
