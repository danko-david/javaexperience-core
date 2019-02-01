package eu.javaexperience.cli;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.exceptions.CheckedIllegalArgumentException;

/**
 * Functions for parsed CLI (parsed by the {@link CliTools#parseCliOpts(String...)})
 * options encapsulated into an instance. 
 * */
public class ParsedCliOptions
{
	protected Map<String, List<String>> opts;

	public ParsedCliOptions(Map<String, List<String>> opts)
	{
		AssertArgument.assertNotNull(this.opts = opts, "cli_options");
	}
	
	public boolean hasOptionAnyOf(String... opt)
	{
		for(String s: opt)
		{
			if(opts.containsKey(s))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean hasOption(String opt)
	{
		return opts.containsKey(opt);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(Entry<String, List<String>> kv:opts.entrySet())
		{
			sb.append("-");
			sb.append(kv.getKey());
			sb.append(" ");
			for(String s:kv.getValue())
			{
				sb.append(s);
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public static ParsedCliOptions parse(String... opts) throws CheckedIllegalArgumentException
	{
		return new ParsedCliOptions(CliTools.parseCliOpts(opts));
	}

	public String getFirstOptMatch(String... keys)
	{
		for(String s:keys)
		{
			if(opts.containsKey(s))
			{
				return s;
			}
		}
		
		return null;
	}

	public List<String> getOptArguments(String key)
	{
		return opts.get(key);
	}

	public Map<String, List<String>> getMap()
	{
		return opts;
	}
}
