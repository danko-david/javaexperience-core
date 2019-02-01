package eu.javaexperience.prog;

public class Debug
{
	public static void printStackTraceNow()
	{
		new Exception().printStackTrace();
	}
	
	
	public static <T> T passPrint(T p)
	{
		System.out.println(p);
		return p;
	}


	public static void placeholderForBreakpoint()
	{
		//empty method, what a surprise
	}


	public static void abort()
	{
		System.out.println("fail");
		Debug.placeholderForBreakpoint();
		System.exit(10);
	}
}
