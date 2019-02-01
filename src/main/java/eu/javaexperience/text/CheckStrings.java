package eu.javaexperience.text;

public class CheckStrings
{
	//TODO minusz számok
	public static boolean isNumber(String str)
	{
		if(str.length() == 0)
			return false;
		
		for(int i=0;i< str.length();i++)
		{
			char c = str.charAt(i);
			if(c < '0' || c > '9')
				return false;
		}
		return true;
	}
	
	public static boolean strLengthLongnerThan(String str, int len)
	{
		if(str == null)
			return false;
		
		return str.length() > len; 
	}
	
	public static boolean strTrimLengthLongnerThan(String str, int len)
	{
		if(str == null)
			return false;
		
		str = str.trim();
		
		return str.length() > len; 
	}
}
