package eu.javaexperience.struct;

public class GenericStruct3<A,B,C> extends GenericStruct2<A, B>
{
	public GenericStruct3()
	{}
	
	public GenericStruct3(A a, B b, C c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	@Override
	public String toString()
	{
		return "a: "+a+", b:"+b+", c:"+c;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public C c;
}