package eu.javaexperience.struct;

import java.util.Map.Entry;

import eu.javaexperience.reflect.Mirror;

public class GenericStruct2<A,B> extends GenericStruct1<A> implements Entry<A, B>
{
	public GenericStruct2()
	{
		
	}
	
	public GenericStruct2(A a, B b)
	{
		this.a = a;
		this.b = b;
	}
	
	private static final long serialVersionUID = 1L;
	public B b;
	
	@Override
	public boolean equals(Object e)
	{
		if(!(e instanceof GenericStruct2))
			return false;
		
		GenericStruct2<A,B> s = (GenericStruct2) e;
		
		return Mirror.equals(s.a, a) && Mirror.equals(s.b, b);
	}
	
	@Override
	public int hashCode()
	{
		if(a == null && b == null)
		{
			return 0;
		}
		
		int ret = 31;
		
		if(a != null)
		{
			ret = ret * 17 + a.hashCode();
		}
		
		if(null != b)
		{
			ret = ret * 17 + b.hashCode();
		}
	
		return ret;
	}

	@Override
	public A getKey()
	{
		return a;
	}

	@Override
	public B getValue()
	{
		return b;
	}

	@Override
	public B setValue(B value)
	{
		throw new UnsupportedOperationException("GenericStruct2.setValue is not supported.");
	}	
}