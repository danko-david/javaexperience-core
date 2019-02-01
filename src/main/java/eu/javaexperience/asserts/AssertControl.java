package eu.javaexperience.asserts;

public class AssertControl
{
	public static void contolMayNotReachThisLine()
	{
		throw new RuntimeException("Control may not reach this line.");
	}
}