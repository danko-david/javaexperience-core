package eu.javaexperience.collection.map;

import java.io.Serializable;
import java.util.Map.Entry;

public class ImmutableKeyVal<K,V> implements Entry<K, V>, Serializable
{
	private static final long serialVersionUID = 1L;

	final K k;
	final V v;
	private transient volatile int hash = 0;

	public ImmutableKeyVal(K k,V v)
	{
		this.k = k;
		this.v = v;
	}

	public KeyVal<K,V> clone()
	{
		return new KeyVal<K, V>(k, v);
	}

	public String toString()
	{
		return "{\""+k+"\":\""+v+"\"}";
	}

	@Override
	public K getKey()
	{
		return k;
	}

	@Override
	public V getValue()
	{
		return v;
	}

	public boolean isYourKeyValPar(K key,V val)
	{
		if(this.k != null)
			if(!k.equals(key))
				return false;

		if(this.v != null)
			if(!v.equals(val))
				return false;

		return true;
	}

	public boolean equals(Object o)
	{
		if(!(o instanceof Entry))
			return false;

		if(o == this)
			return true;

		Entry test = (Entry) o;

		if(this.k != null)
		{
			if(!this.k.equals(test.getKey()))
				return false;
		}
		else if(test.getKey() != null)
			return false;

		if(this.v != null)
		{
			if(!this.v.equals(test.getValue()))
				return false;
		}
		else if(test.getKey() != null)
			return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int ret = hash;
		if(ret == 0)
		{
			ret = 17;
			if(k != null)
				ret = 31 * ret + k.hashCode();
			if(v != null)
				ret = 31 * ret +v.hashCode();

			hash = ret;
		}
		return ret;
	}

	@Override
	public V setValue(V value)
	{
		throw new IllegalArgumentException("Modifying ImmutableKeyVal is not permitted!");
	}

}