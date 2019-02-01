package eu.javaexperience.parse;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.reflect.CastTo;

public class ParsePrimitiveFunctions
{
	public static final GetBy1<Integer, String> INT_PARSER = new GetBy1<Integer, String>()
	{
		@Override
		public Integer getBy(String a)
		{
			return ParsePrimitive.tryParseInt(a);
		}
	};
	
	public static GetBy1<Integer, String> createParserOrDefault(final int def)
	{
		return new GetBy1<Integer, String>()
		{
			@Override
			public Integer getBy(String a)
			{
				return ParsePrimitive.tryParseInt(a, def);
			}
		};
	}

	public static GetBy1<Boolean, String> parseBooleanOrDefault(final boolean b)
	{
		return new GetBy1<Boolean, String>()
		{			
			@Override
			public Boolean getBy(String a)
			{
				Object ret = CastTo.Boolean.cast(a);
				if(null != ret)
				{
					return (Boolean)ret;
				}
				return b;
			}
		};
	}
}
