package eu.javaexperience.cli;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.javaexperience.collection.CollectionReadOnlyFunctions;
import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.ReadOnlyAndRwCollection;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;

public class CliEntry<T>
{
	protected ReadOnlyAndRwCollection<List<String>> opts = new ReadOnlyAndRwCollection<>(new ArrayList<>(), (GetBy1)(Object)CollectionReadOnlyFunctions.MAKE_LIST_READ_ONLY);
	protected String description;
	protected GetBy2<T, CliEntry<T>, Map<String, List<String>>> parser;
	
	public CliEntry(GetBy2<T, CliEntry<T>, Map<String, List<String>>> parser, String descr, String... opt)
	{
		this.parser = parser;
		CollectionTools.copyInto(opt, opts.getWriteable());
	}
	
	private CliEntry(String descr, String... opt)
	{
		this.description = descr;
		CollectionTools.copyInto(opt, opts.getWriteable());
	}
	
	public static <T> CliEntry<T> createFirstArgParserEntry(GetBy1<T, String> parser, String descr, String... opt)
	{
		CliEntry<T> ret = new CliEntry<T>(descr, opt);
		ret.parser = CliTools.createParser(parser, ret.createFirstArgExaminer());
		return ret;
	}
	
	public GetBy1<String, Map<String, List<String>>> createFirstArgExaminer()
	{
		return new GetBy1<String, Map<String,List<String>>>()
		{
			@Override
			public String getBy(Map<String, List<String>> a)
			{
				return getSimple(a);
			}
		};
	}
	
	public List<String> getOptionNames()
	{
		return opts.getReadOnly();
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public T tryParse(Map<String, List<String>> from)
	{
		try
		{
			return parser.getBy(this, from);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("Error on parsing CliEntry: "+this.toString());
		}
	}
	
	public T tryParseOrDefault(Map<String, List<String>> from, T def)
	{
		T ret = parser.getBy(this, from);
		if(null != ret)
		{
			return ret;
		}
		
		return def;
	}
	
	public boolean hasOption(Map<String, List<String>> from)
	{
		for(String name: getOptionNames())
		{
			if(from.containsKey(name))
			{
				return true;
			}
		}
		return false;
	}
	
	public String getSimple(Map<String, List<String>> from)
	{
		for(String s:opts.getReadOnly())
		{
			List<String> re = from.get(s);
			if(null != re && re.size() > 0)
			{
				return re.get(0);
			}
		}
		
		return null;
	}
	
	public List<String> getFirst(Map<String, List<String>> from)
	{
		for(String s:opts.getReadOnly())
		{
			List<String> re = from.get(s);
			if(null != re)
			{
				return re;
			}
		}
		
		return null;
	}
	
	public List<String> getAll(Map<String, List<String>> from)
	{
		ArrayList<String> ret = null;
		for(String s:opts.getReadOnly())
		{
			List<String> re = from.get(s);
			if(null != re)
			{
				if(null == ret)
				{
					ret = new ArrayList<>();
				}
				
				CollectionTools.copyInto(re, ret);
			}
		}
		
		return ret;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("\t");
		int n = 0;
		for(String o:getOptionNames())
		{
			if(++n > 1)
			{
				sb.append(", ");
			}
			sb.append("-");
			sb.append(o);
		}
		
		sb.append("\t\t");
		sb.append(getDescription());
		return sb.toString();
	}
}
