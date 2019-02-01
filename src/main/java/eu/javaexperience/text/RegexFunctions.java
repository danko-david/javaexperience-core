package eu.javaexperience.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.javaexperience.generic.annotations.Ignore;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class RegexFunctions
{
	public static GetBy1<String, String> regexReplace(String pattern, final boolean once, final String replace)
	{
		final Pattern p = Pattern.compile(pattern);
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				StringBuffer sb = new StringBuffer();
				boolean permit = true;
				Matcher m = p.matcher(a);
				
				while(permit && m.find())
				{
					m.appendReplacement(sb, replace);
					
					if(once)
					{
						permit = false;
					}
				}
				m.appendTail(sb);
				return sb.toString();
			}
		};
	}
	
	@Ignore
	public static GetBy1<String, String> findAndExamine(final Pattern pattern)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				Matcher m = pattern.matcher(a);
				if(m.find())
				{
					return m.group();
				}
				
				return null;
			}
		};
	}
	
	public static GetBy1<String, String> findAndExamine(String pattern)
	{
		return findAndExamine(Pattern.compile(pattern));
	}
	
	@Ignore
	public static GetBy1<String, String> findAndExamineNamed(final Pattern pattern, final String grpName)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				Matcher m = pattern.matcher(a);
				if(m.find())
				{
					return m.group(grpName);
				}
				
				return null;
			}
		};
	}
	
	public static GetBy1<String, String> findAndExamineNamed(String pattern, String grpName)
	{
		return findAndExamineNamed(Pattern.compile(pattern), grpName);
	}
}
