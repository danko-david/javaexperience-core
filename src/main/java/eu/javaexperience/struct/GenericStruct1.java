package eu.javaexperience.struct;

import java.io.Serializable;

public class GenericStruct1<A> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public A a;
	
	public boolean equals(Object e)
	{
		if(!(e instanceof GenericStruct1))
			return false;
		
		GenericStruct1<A> s = (GenericStruct1) e;
		
		if(a == null && s.a == null)
			return true;
		
		if(a != null)
			return a.equals(s.a);
		
		return false;
	}

	public int hashCode()
	{
		return a == null? 0 : a.hashCode();
	}
}