package eu.javaexperience.regex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.javaexperience.reflect.Mirror;

public class RegexTools
{
	public static final Pattern MATCH_WHITESPACES = Pattern.compile("\\s+");

	public static final Pattern ZERO_TO_Z = Pattern.compile("[0-9a-zA-Z]+");

	public static final Pattern TAB = Pattern.compile("\t");
	
	public static final Pattern DASHES = Pattern.compile("-+");
	
	public static final Pattern SPACES = Pattern.compile(" +");
	
	public static final Pattern DOT = Pattern.compile("\\.");
	
	public static final Pattern COMMA = Pattern.compile(",");
	
	public static final Pattern COLON = Pattern.compile(":");
	
	public static final Pattern HEADER_LINES = Pattern.compile(" *: *");
	
	public static final Pattern QUESTION_MARK = Pattern.compile("\\?");
	
	public static final Pattern SLASHES = Pattern.compile("/+");
	
	public static final Pattern SLASHES_LINUX_WINDOWS = Pattern.compile("[/\\\\]+");
	
	public static final Pattern CONTROL_CHARACTER =  Pattern.compile("\\p{Cntrl}");
	
	public static final Pattern C_VARIABLE_NAME = Pattern.compile("^[a-zA-Z_$][a-zA-Z_$0-9]*$");
	
	public static final Pattern LINUX_NEW_LINE = Pattern.compile("\n");
	
	public static String getNamedGroupOrDefault(String source,Pattern pat, String group, String def)
	{
		if(source == null)
			return null;
		Matcher m = pat.matcher(source);
		if(m.find())
			return m.group(group);
		
		return def;
	}
	
	private static final Pattern ngf = Pattern.compile("\\?<(\\w+)>");


	public static String[] getNamedGroupNames(String regex)
	{
		ArrayList<String> ret = new ArrayList<String>();
		Matcher m = ngf.matcher(regex);
		while(m.find())
			ret.add(m.group(1));

		return ret.toArray(Mirror.emptyStringArray);
	}
	
	/**
	 * Visszaadja a forrásban található groupName csoport értéket, amit a regex megtalál.
	 * Ha megtalálta begyűjti, majd a megtalált szöveget kivágja. és még egyszer keres 
	 * 
	 * source: "method(param0,num,base)";
	 * regex: "\\w*(((?<arg>\\*w),*))$";
	 * groupName: arg
	 * eremény: "param0","num","base"
	 * */
	public static String[] getNamedGroups(String source,Pattern regex,String groupName,int limit)
	{
		String[] ret = new String[limit];
		int ep = 0;
		for(int i=0;i<limit;i++)
		{
			Matcher m = regex.matcher(source);
			if(!m.find())
				break;
			
			ret[ep++] = m.group(groupName);
		}
		return Arrays.copyOf(ret, ep);
	}
	
	public static Map<String,String> collectNamedGroups(String source,Pattern regex)
	{
		return collectNamedGroups(source, regex, getNamedGroupNames(regex.toString()));
	}
	
	public static Map<String,String> collectNamedGroups(String source,Pattern regex,String... groups)
	{
		Map<String,String> ret = new HashMap<>();
		Matcher m = regex.matcher(source);
		if(m.matches())
			for(String s:groups)
				ret.put(s,m.group(s));
		
		return ret;
	}

	public static String quoteRegex(String regex)
	{
		return Pattern.quote(regex);
	}
	
	public static Pattern captureStrings(String... words)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		for(int i=0;i<words.length;++i)
		{
			if(sb.length() > 1)
			{
				sb.append("|");
			}
			sb.append(RegexTools.quoteRegex(words[i]));
		}
		
		sb.append(")");
		return Pattern.compile(sb.toString());
	}

	public static String findNamedRegexGroup(String subject, Pattern pattern, String grpName)
	{
		Matcher m = pattern.matcher(subject);
		if(m.find())
		{
			return m.group(grpName);
		}
		
		return null;
	}
}
