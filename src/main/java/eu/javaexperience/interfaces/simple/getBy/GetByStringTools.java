package eu.javaexperience.interfaces.simple.getBy;

import eu.javaexperience.text.StringTools;

public class GetByStringTools
{
	public static final GetBy1<String,String> STRING_TOLOWERCASE = new GetBy1<String, String>()
	{
		@Override
		public String getBy(String a)
		{
			if(null == a)
			{
				return null;
			}
			return a.toLowerCase();
		}
	};
	
	public static final GetBy1<String,String> STRING_TOUPPERCASE = new GetBy1<String, String>()
	{
		@Override
		public String getBy(String a)
		{
			if(null == a)
			{
				return null;
			}
			return a.toUpperCase();
		}
	};
	
	public static final GetBy1<String,String> STRING_DEACCENT = new GetBy1<String, String>()
	{
		@Override
		public String getBy(String a)
		{
			if(null == a)
			{
				return null;
			}
			return StringTools.deAccent(a);
		}
	};
	
}
