package eu.javaexperience.text.tokenize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenGroup<T>
{
	protected T group;
	protected Pattern pattern;
	
	public TokenGroup(T group, Pattern p)
	{
		this.group = group;
		this.pattern = p;
	}
	
	public ParsedToken<T> tryMatch(int from, String cs)
	{
		Matcher m = pattern.matcher(cs);
		if(m.find(from))
		{
			if(from == m.start())
			{
				return new ParsedToken<>(this, m.group(), from);
			}
		}
		
		return null;
	}

	public T getGroup()
	{
		return group;
	}
}