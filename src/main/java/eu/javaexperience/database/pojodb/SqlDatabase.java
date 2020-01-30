package eu.javaexperience.database.pojodb;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.javaexperience.database.ConnectionCreator;
import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.JdbcConnectionPool;
import eu.javaexperience.database.pojodb.dialect.SqlDialect;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.semantic.references.MayNull;
import eu.javaexperience.text.StringTools;

public class SqlDatabase implements Database
{
	protected ConnectionCreator cc;
	
	protected JdbcConnectionPool pool;
	
	protected SqlDialect dialect;
	
	public SqlDatabase(ConnectionCreator cc, SqlDialect dialect)
	{
		this.cc = cc;
		this.pool = new JdbcConnectionPool(cc);
		this.dialect = dialect;
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
	
	public <T extends Model> List<T> getWhere(Class<T> cls, @MayNull String where, Object... values) throws SQLException, InstantiationException, IllegalAccessException
	{
		T obj = cls.newInstance();
		List<T> ret = new ArrayList<>();
		String quote = dialect.getFieldQuoteString();
		getInstances(cls, ret, "SELECT * FROM "+quote+obj.getTable()+quote+(StringTools.isNullOrTrimEmpty(where)?"":"WHERE "+where), values);
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
	
	protected static Long ZERO = 0l;
	
	@Override
	public void insert(Model m) throws SQLException
	{
		try(Connection conn = pool.getConnection())
		{
			try
			{
				Field id = m.getIdField();
				Object idObj = null == id?null:id.get(m);
				//if type is number and value i zero 
				if
				(
					idObj instanceof Number
				&&
					0 == ((Number)idObj).longValue() 
				)
				{
					idObj = null;
				}
				
				Field[] fields = m.getFields();
				
				String qoute = dialect.getFieldQuoteString();
				
				StringBuilder sb = new StringBuilder();
				sb.append("INSERT INTO ");
				sb.append(qoute);
				sb.append(m.getTable());
				sb.append(qoute);
				sb.append(" (");
				
				{
					int nums = 0;
					for(int i=0;i<fields.length;++i)
					{
						//if id not present and 
						if(null == idObj && fields[i] == id)
						{
							continue;
						}
						
						if(nums++ > 0)
							sb.append(",");
						
						sb.append(qoute);
						sb.append(fields[i].getName());
						sb.append(qoute);
					}
				}
				
				sb.append(")VALUES("); 
				
				{
					int nums = 0;
					for(int i=0;i<fields.length;++i)
					{
						if(null == idObj && fields[i] == id)
						{
							continue;
						}
						
						if(nums++ > 0)
							sb.append(",");
						
						sb.append("?");
					}
				}
				
				sb.append(");");
				
				try(PreparedStatement ps = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS))
				{
					int nums = 0;
					for(int i=0;i<fields.length;++i)
					{
						if(null == idObj && fields[i] == id)
						{
							continue;
						}
						ps.setObject(++nums, fields[i].get(m));
					}
					
					if(ps.executeUpdate() != 0)
					{
						try(ResultSet generatedKeys = ps.getGeneratedKeys())
						{
							if(generatedKeys.next())
							{
								ResultSetMetaData md = generatedKeys.getMetaData();
								int count = md.getColumnCount();
								for(int i = 1;i <= count;++i)
								{
									String label = md.getColumnLabel(i);
									Object vId = generatedKeys.getObject(i);
									
									if("GENERATED_KEY".equals(label))
									{
										CastTo cast = CastTo.getCasterRestrictlyForTargetClass(id.getType());
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
											throw new RuntimeException("Can't cast generated id for target type. id: "+vId+", field and type: "+id);
										}
									}
								}
							}
						}
					}
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

	public <T extends Model> void ensureTable(Class<T> model) throws SQLException
	{
		try(Connection conn = pool.getConnection())
		{
			try
			{
				SqlTools.alterTableAddFields(conn, model.newInstance(), dialect);
			}
			catch(Exception e)
			{
				Mirror.propagateAnyway(e);
			}
		}
	}
}
