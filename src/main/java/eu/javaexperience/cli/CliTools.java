package eu.javaexperience.cli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.exceptions.CheckedIllegalArgumentException;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;

public class CliTools
{
	private CliTools() {}
	
	public static Map<String, List<String>> parseCliOpts(String... args)
	{
		Map<String, List<String>> params = new HashMap<>();
		
		List<String> options = null;
		for (int i = 0; i < args.length; i++)
		{
			final String a = args[i];
			
			if (a.charAt(0) == '-')
			{
				if (a.length() < 2)
				{
					throw new IllegalArgumentException("Error at argument " + a);
				}

				options = new ArrayList<>();
				params.put(a.substring(1), options);
			}
			else if (options != null)
			{
				options.add(a);
			}
			else
			{
				throw new IllegalArgumentException("Illegal parameter usage: "+a);
			}
		}
		
		return params;
	}
	
	public static String renderListAllOption(CliEntry... ent)
	{
		StringBuilder sb = new StringBuilder();
		for(CliEntry<?> e:ent)
		{
			sb.append(e.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static <T> GetBy2<T, CliEntry<T>, Map<String,List<String>>> createParser(final GetBy1<T, String> parser, final GetBy1<String, Map<String, List<String>>> examiner)
	{
		return (a, b)->
		{
			String ret = examiner.getBy(b);
			if(null != ret)
			{
				return parser.getBy(ret);
			}
			
			return null;
		};
	}

	public static String getFirstUnknownParam(Map<String, List<String>> pa, CliEntry... ents)
	{
		HashSet<String> opts = new HashSet<>();
		for(CliEntry<?> ent:ents)
		{
			CollectionTools.copyInto(ent.getOptionNames(), opts);
		}
		for(String s:pa.keySet())
		{
			if(!opts.contains(s))
			{
				return s;
			}
		}
		
		return null;
	}
	
	protected static Map<String, List<String>> CLI_OPTIONS = null;
	
	public static Map<String, List<String>> storeCliOptions(boolean override, String... args) throws CheckedIllegalArgumentException
	{
		if(null == CLI_OPTIONS || override)
		{
			Map<String, List<String>> parsed = parseCliOpts(args);
			Map<String, List<String>> store = new HashMap<>();
			for(Entry<String, List<String>> kv:parsed.entrySet())
			{
				store.put(kv.getKey(), Collections.unmodifiableList(kv.getValue()));
			}
			store = Collections.unmodifiableMap(store);
			CLI_OPTIONS = store;
		}
		
		return CLI_OPTIONS;
	}
	
	public static Map<String, List<String>> getStoredCliOptions()
	{
		return CLI_OPTIONS;
	}
	
	public static void printHelpAndExit(String programName, int exitCode, CliEntry... entries)
	{
		System.err.println("Usage of "+programName+":\n");
		System.err.println(CliTools.renderListAllOption(entries));
		System.exit(exitCode);
	}
}
