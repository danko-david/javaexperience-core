package eu.javaexperience.database.collection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.collection.map.KeyVal;
import eu.javaexperience.collection.set.ArrayListSeemsSet;
import eu.javaexperience.reflect.Mirror;

public class JdbcMap<K, V> implements Map<K, V>
{
	public static interface JdbcMapImplProvider<K, V>
	{
		public Connection getConnection();
		public void releaseConnection(Connection conn);
		
		public ResultSet selectWhereKey(Connection conn, K key);
		public ResultSet selectWhereValue(Connection conn, V val);
		
		public ResultSet selectAll(Connection conn);
		
		public K extractKey(ResultSet rs);
		public V extractValue(ResultSet rs);
		
		public V insertOrUpdate(Connection conn, K key, V value);
		public V removeByKey(Connection conn, Object key);
		
		
		int getMappingCount(Connection conn);
		public void emptyOutMapping(Connection conn);
	}
	
	protected JdbcMapImplProvider<K, V> prov;
	
	public JdbcMap(JdbcMapImplProvider<K, V> prov)
	{
		AssertArgument.assertNotNull(this.prov = prov, "jdbc map implemntation provider");
	}
	
	@Override
	public int size()
	{
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			return prov.getMappingCount(conn);
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
	}

	@Override
	public boolean isEmpty()
	{
		return 0 == size();
	}

	@Override
	public boolean containsKey(Object key)
	{
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			try(ResultSet rs = prov.selectWhereKey(conn, (K) key))
			{
				return rs.next();
			}
			catch (SQLException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
				return false;
			}
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
	}

	@Override
	public boolean containsValue(Object value)
	{
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			try(ResultSet rs = prov.selectWhereValue(conn, (V) value))
			{
				return rs.next();
			}
			catch (SQLException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
				return false;
			}
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
	}

	@Override
	public V get(Object key)
	{
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			try(ResultSet rs = prov.selectWhereKey(conn, (K) key))
			{
				if(rs.next())
				{
					return prov.extractValue(rs);
				}
				
				return null;
			}
			catch (SQLException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
				return null;
			}
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
	}

	@Override
	public V put(K key, V value)
	{
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			return prov.insertOrUpdate(conn, key, value);
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
	}

	@Override
	public V remove(Object key)
	{
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			return prov.removeByKey(conn, key);
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m)
	{
		for(java.util.Map.Entry<? extends K, ? extends V> kv:m.entrySet())
		{
			this.put(kv.getKey(), kv.getValue());
		}
	}

	@Override
	public void clear()
	{
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			prov.emptyOutMapping(conn);
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
	}

	@Override
	public Set<K> keySet()
	{
		ArrayListSeemsSet<K> ret = new ArrayListSeemsSet<>();
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			 
			try(ResultSet rs = prov.selectAll(conn))
			{
				while(rs.next())
				{
					ret.add(prov.extractKey(rs));
				}
			}
			catch (SQLException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
			}
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
		
		return ret;
	}

	@Override
	public Collection<V> values()
	{
		ArrayList<V> ret = new ArrayList<>();
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			 
			try(ResultSet rs = prov.selectAll(conn))
			{
				while(rs.next())
				{
					ret.add(prov.extractValue(rs));
				}
			}
			catch (SQLException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
			}
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
		
		return ret;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet()
	{
		ArrayListSeemsSet<java.util.Map.Entry<K, V>> ret = new ArrayListSeemsSet<>();
		Connection conn = null;
		try
		{
			conn = prov.getConnection();
			 
			try(ResultSet rs = prov.selectAll(conn))
			{
				while(rs.next())
				{
					ret.add(new KeyVal<>(prov.extractKey(rs), prov.extractValue(rs)));
				}
			}
			catch (SQLException e)
			{
				Mirror.throwSoftOrHardButAnyway(e);
			}
		}
		finally
		{
			if(null != conn)
			{
				prov.releaseConnection(conn);
			}
		}
		
		return ret;
	}
}
