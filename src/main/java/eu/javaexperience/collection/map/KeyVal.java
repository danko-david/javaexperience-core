package eu.javaexperience.collection.map;

import java.io.Serializable;
import java.util.Map.Entry;

import eu.javaexperience.reflect.Mirror;

public class KeyVal<K,V> implements Entry<K, V>, Serializable
{
	private static final long serialVersionUID = 1L;

	public static KeyVal[] emptyArrayInstance = new KeyVal[0];

	K k;
	V v;
	private transient volatile int hash = 0;

	public KeyVal(K k,V v)
	{
		this.k = k;
		this.v = v;
	}

	public KeyVal(Entry<K, V> kv)
	{
		this(kv.getKey(), kv.getValue());
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

	public K setKey(K arg0)
	{
		K old = k;
		hash = 0;
		k = arg0;
		return old;
	}
	
	@Override
	public V getValue()
	{
		return v;
	}

	@Override
	public V setValue(V arg0)
	{
		hash = 0;
		V old = v;
		v = arg0;
		return old;
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

		return Mirror.equals(k, test.getKey()) && Mirror.equals(v, test.getValue());
	}
	
	public boolean isKeyEquals(Object key)
	{
		if(key == k)
			return true;
		
		if(k != null)
			return k.equals(key);
		
		return false;
	}
	
	public boolean isValueEquals(Object value)
	{
		if(value == v)
			return true;
		
		if(v != null)
			return v.equals(value);
		
		return false;
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

	public static void main(String[] arssdfsdfsf)
	{
		KeyVal<String,String> nullnull = new KeyVal<String, String>(null, null);
		KeyVal<String,String> anullnull = new KeyVal<String, String>(null, null);
		KeyVal<String, String> fullfull = new KeyVal<String, String>("a","b");
		KeyVal<String, String> ffullfull = new KeyVal<String, String>("a","b");
		KeyVal<String, String> afullfull = new KeyVal<String, String>("a","c");

		KeyVal<String, String> a = new KeyVal<String, String>("alkatresz","jfet");
		KeyVal<String, String> b = new KeyVal<String, String>("alkatresz","jfet");

		System.out.println
		(
				nullnull.equals(nullnull)+"\n"+
				nullnull.equals(anullnull)+"\n"+
				anullnull.equals(nullnull)+"\n"+
				nullnull.equals(fullfull)+"\n"+
				fullfull.equals(fullfull)+"\n"+
				ffullfull.equals(ffullfull)+"\n"+
				fullfull.equals(ffullfull)+"\n"+
				a.equals(b)+"\n"+
				b.equals(a)+"\n"+
				"Vágás\n"+
				nullnull.equals(fullfull)+"\n"+
				fullfull.equals(nullnull)+"\n"+
				fullfull.equals(afullfull)
		);
	}
}