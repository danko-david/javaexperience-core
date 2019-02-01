package eu.javaexperience.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import eu.javaexperience.interfaces.simple.publish.SimplePublish2;
import eu.javaexperience.reflect.Mirror;

public class TokenTools
{
	public static class KeywordGroupIndex
	{
		public Map<String, Integer> tokenFrequency = new HashMap<>();
		public Map<String, String[]> stringTokens = new HashMap<>();
	}
	
	public static KeywordGroupIndex indexTokens
	(
		Collection<String> strings,
		SimplePublish2<String, Collection<String>> tokenize,
		boolean ignoreDuplicateTokensInString
	)
	{
		Collection<String> tokens = ignoreDuplicateTokensInString?
			new HashSet<String>()
		:
			new ArrayList<String>();
		
		KeywordGroupIndex ret = new KeywordGroupIndex();
		
		for(String s:strings)
		{
			tokens.clear();
			tokenize.publish(s, tokens);

			ret.stringTokens.put(s, tokens.toArray(Mirror.emptyStringArray));
			
			for(String t:tokens)
			{
				Integer i = ret.tokenFrequency.get(t);
				if(null == i)
				{
					i = 1;
				}
				else
				{
					++i;
				}
				ret.tokenFrequency.put(t, i);
			}
		}
		
		return ret;
	}
}
