package eu.javaexperience.asserts;

public class AssertArgument
{
	public static void assertNotNull(Object o,String varname)
	{
		if(o == null)
			throw new IllegalArgumentException(varname+" can not be null");
	}
	
	public static void assertGreaterThan(int vari,int limit,String varname)
	{
		if(vari <= limit)
			throw new IllegalArgumentException(varname+" should be greater than "+limit);
	}
	
	public static void assertGreaterOrEqualsThan(int vari,int limit,String varname)
	{
		if(vari < limit)
			throw new IllegalArgumentException(varname+" should be greater than or equals "+limit);
	}
	
	public static void assertLessThan(int vari,int limit,String varname)
	{
		if(vari >= limit)
			throw new IllegalArgumentException(varname+" should be less than "+limit);
	}
	
	public static void assertLessOrEqualsThan(int vari,int limit,String varname)
	{
		if(vari > limit)
			throw new IllegalArgumentException(varname+" should be less or equals "+limit);
	}
	
	public static void assertEquals(Object vari,Object val,String varname)
	{
		if(!((val == null && vari == null) || (vari != null && vari.equals(val) || (val != null && val.equals(vari)))))
			throw new IllegalArgumentException(varname+" should be equals with "+val+", "+vari+" given");
	}
	
	public static void main(String[] args)
	{
		Object o = new Object();
		assertEquals(null, null, "ads");
		assertEquals(o, o, "ads");
		
		try
		{
			assertEquals(o, null, "ads");
		}
		catch(Exception e){e.printStackTrace();}
		
		try
		{
			assertEquals(null, o, "ads");
		}
		catch(Exception e){e.printStackTrace();}
	}

	public static void errorOnNull(Object o, String msg)
	{
		if(null == o)
		{
			throw new IllegalArgumentException(msg);
		}
	}
	
	public static void errorOnEq(boolean cond, boolean rise, String msg)
	{
		if(cond == rise)
		{
			throw new RuntimeException(msg);
		}
	}

	public static void assertTrue(boolean a, String txt)
	{
		if(!a)
		{
			throw new RuntimeException(txt);
		}
	}
	
}