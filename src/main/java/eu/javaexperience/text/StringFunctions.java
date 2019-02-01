package eu.javaexperience.text;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.generic.annotations.Ignore;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.regex.RegexTools;
import eu.javaexperience.annotation.FunctionDescription;
import eu.javaexperience.annotation.FunctionVariableDescription;

public class StringFunctions
{
	/**
	 * Filter szabály:
	 * A bemenő string sose null
	 * A kimenő string lehet null, ekkor eldobjuk.
	 * */
	public static final GetBy1<String, String> FILTER_WIPE_WHITESPACES = new GetBy1<String, String>()
	{
		@Override
		public String getBy(String a)
		{
			return RegexTools.MATCH_WHITESPACES.matcher(a).replaceAll("");
		}
	};
	
	public static final GetBy1<String, String> FILTER_DROP_EMPTY_STRING = new GetBy1<String, String>()
	{
		@Override
		public String getBy(String a)
		{
			if(0 == a.trim().length())
			{
				return null;
			}
			
			return a;
		}
	};
	
	@FunctionDescription
	(
		functionDescription = "Creates a function drops (returns null) the strings, matches the given regex.",
		parameters =
		{
			@FunctionVariableDescription(description = "Perl regular expression.", mayNull = false, paramName = "regex", type=String.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static GetBy1<String, String> generateDropMatchingFilter(String regex)
	{
		return generateMatchingFilter(regex, false);
	}
	
	@FunctionDescription
	(
		functionDescription = "Creates a function passes only (othervise return null) the strings, matches the given regex.",
		parameters =
		{
			@FunctionVariableDescription(description = "Perl regular expression.", mayNull = false, paramName = "regex", type=String.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static GetBy1<String, String> generatePassMatchingFilter(String regex)
	{
		return generateMatchingFilter(regex, true);
	}
	
	@FunctionDescription
	(
		functionDescription = "Creates a filter function using the given regex, and by the \"mode\" parameter it is pass (on true) or drop (on false) if string matches.",
		parameters =
		{
			@FunctionVariableDescription(description = "Perl regular expression.", mayNull = false, paramName = "regex", type=String.class),
			@FunctionVariableDescription(description = "Mode. on true: create a pass filter. on false: create a drop filter", mayNull = false, paramName = "mode", type=Boolean.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static GetBy1<String, String> generateMatchingFilter
	(
		final String regex,
		final boolean truePass_falseDrop
	)
	{
		return generateMatchingFilter(Pattern.compile(regex), truePass_falseDrop);
	}
	
	@Ignore
	public static GetBy1<String, String> generateMatchingFilter
	(
		final Pattern regex,
		final boolean truePass_falseDrop
	)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				boolean matching = regex.matcher(a).matches();
				if(matching == truePass_falseDrop)
				{
					return a;
				}
				
				return null;
			}
		};
	}
	
	@FunctionDescription
	(
		functionDescription = "Creates a transformator function that returns the part of a string after the first occurrence of \"search\" string."
			+ "if not found it returns null. eg.: (search: \"id_val_\")\"map_id_val_3_12\" => \"3_12\" and (\"map_id_val\" => null)",
		parameters =
		{
			@FunctionVariableDescription(description = "String to search.", mayNull = false, paramName = "search", type=String.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static GetBy1<String, String> getSubstringAfterFirstString(final String search)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getSubstringAfterFirstString(a, search, null);
			}
		};
	}
	
	@FunctionDescription
	(
		functionDescription = "Creates a transformator function that returns the part of a string after the first occurrence of \"search\" string."
			+ "if not found it returns the whole string. eg.: (search: \"id_val_\")\"map_id_val_3_12\" => \"3_12\" and (\"map_id_val\" => \"map_id_val\")",
		parameters =
		{
			@FunctionVariableDescription(description = "String to search.", mayNull = false, paramName = "search", type=String.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static GetBy1<String, String> getSubstringAfterFirstStringOrPass(final String search)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getSubstringAfterFirstString(a, search, a);
			}
		};
	}
	
	@FunctionDescription
	(
		functionDescription = "Creates a transformator function that returns the part of a string after the last occurrence of \"search\" string."
			+ "if not found it returns null. eg.: (search: \".\")\"/tmp/1.jpg\" => \"jpg\" and (\"/tmp/file\" => null)",
		parameters =
		{
			@FunctionVariableDescription(description = "String to search.", mayNull = false, paramName = "search", type=String.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static GetBy1<String, String> getSubstringAfterLastString(final String search)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getSubstringAfterLastString(a, search, null);
			}
		};
	}
	
	@FunctionDescription
	(
		functionDescription = "Creates a transformator function that returns the part of a string after the last occurrence of \"search\" string."
			+ "if not found it returns the whole string. eg.: (search: \".\")\"/tmp/1.jpg\" => \"jpg\" and (\"/tmp/file\" => \"/tmp/file\")",
		parameters =
		{
			@FunctionVariableDescription(description = "String to search.", mayNull = false, paramName = "search", type=String.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static GetBy1<String, String> getSubstringAfterLastStringOrPass(final String search)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getSubstringAfterLastString(a, search, a);
			}
		};
	}
	
	public static GetBy1<String, String> getSubstringBeforeFirstString(final String search)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getSubstringBeforeFirstString(a, search, null);
			}
		};
	}
	
	public static GetBy1<String, String> getSubstringBeforeFirstStringOrPass(final String search)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getSubstringBeforeFirstString(a, search, a);
			}
		};
	}
	
	public static GetBy1<String, String> getSubstringBeforeLastString(final String search)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getSubstringBeforeLastString(a, search, null);
			}
		};
	}
	
	public static GetBy1<String, String> getSubstringFirstBetween(final String before, final String after)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getFirstBetween(a, before, after, null);
			}
		};
	}
	
	public static GetBy1<String, String> getSubstringLastBetween(final String before, final String after)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.getLastBetween(a, before, after, null);
			}
		};
	}
	
	public static GetBy1<String, String> getFromNamedRegex(final String regex, final String group_name)
	{
		return new GetBy1<String, String>()
		{
			Pattern p = Pattern.compile(regex);
			
			@Override
			public String getBy(String a)
			{
				Matcher m = p.matcher(a);
				if(m.find())
				{
					return m.group(group_name);
				}
				
				return null;
			}
		};
	}
	
	public static final GetBy1<String, String> TRIM = new GetBy1<String, String>()
	{
		@Override
		public String getBy(String a)
		{
			return a.trim();
		}
	};

	public static final GetBy1<String, String> DEACCENT = new GetBy1<String, String>()
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

	public static final GetBy1<String, String> LOWER_CASE = new GetBy1<String, String>()
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
	
	public static final GetBy1<String, String> UPPER_CASE = new GetBy1<String, String>()
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
	
	public static GetBy1<String, String> chainTransformatorsDropNull(final GetBy1<String, String>... fs)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				for(GetBy1<String, String> f: fs)
				{
					if(null == a)
					{
						return null;
					}
					a = f.getBy(a);
				}
				
				return a;
			}
		};
	}
	
	
	/*public static void main(String[] args)
	{
		//4 true, because string are identicall in this case
		System.out.println(null == generateDropMatchingFilter("asdf").getBy("asdf"));
		System.out.println("asa" == generateDropMatchingFilter("asdf").getBy("asa"));
		
		System.out.println("asdf" == generatePassMatchingFilter("asdf").getBy("asdf"));
		System.out.println(null == generatePassMatchingFilter("asdf").getBy("asa"));
	}*/

	public static GetBy1<String, String> withPrefix(final String prefix)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return prefix+a;
			}
		};
	}
	
	public static GetBy1<String, String> withPostfix(final String postfix)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return a+postfix;
			}
		};
	}
	
	public static GetBy1<String, String> withPreAndPostfix(final String prefix, final String postfix)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return prefix+a+postfix;
			}
		};
	}

	public static GetBy1<String, String> dropIfEquals(final String eq)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				if(null == a)
				{
					return null;
				}
				
				if(eq.equals(a))
				{
					return null;
				}
				else
				{
					return a;
				}
			}
		};
	}
	
	public static GetBy1<String, String> swapExactString(final Map<String, String> swap)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				String ret = swap.get(a);
				if(null != ret)
				{
					return ret;
				}
				
				return a;
			}
		};
	}
	
	public static GetBy1<String, String> ensureStartWith(final String prefix)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				if(null == a)
				{
					return null;
				}
				
				if(a.startsWith(prefix))
				{
					return a;
				}
				else
				{
					return prefix+a;
				}
			}
		};
	}
	
	
	
	public static GetBy1<Boolean, String> isEquals(final String str)
	{
		return new GetBy1<Boolean, String>()
		{
			@Override
			public Boolean getBy(String a)
			{
				return Mirror.equals(a, str);
			}
		};
	}
	
	public static GetBy1<Boolean, String> isIn(final String... arr)
	{
		return new GetBy1<Boolean, String>()
		{			
			@Override
			public Boolean getBy(String a)
			{
				return ArrayTools.contains(arr, a);
			}
		};
	}

	protected static GetBy2<Boolean, String, String> EQUALS_CASE_INSENSITIVE = new GetBy2<Boolean, String, String>()
	{
		@Override
		public Boolean getBy(String a, String b)
		{
			if(null == a && a == b)
			{
				return true;
			}
			
			return b.equalsIgnoreCase(a);
		}
	};
	public static GetBy1<Boolean, String> isInCaseInsensitive(final String... arr)
	{
		return new GetBy1<Boolean, String>()
		{			
			@Override
			public Boolean getBy(String a)
			{
				return ArrayTools.contains(EQUALS_CASE_INSENSITIVE, arr, a);
			}
		};
	}
	
	public static GetBy1<Boolean, String> isMatches(final String p)
	{
		return isMatches(Pattern.compile(p));
	}
	
	@Ignore
	public static GetBy1<Boolean, String> isMatches(final Pattern p)
	{
		return new GetBy1<Boolean, String>()
		{
			@Override
			public Boolean getBy(String a)
			{
				return p.matcher(a).find();
			}
		};
	}
	
	public static GetBy1<Boolean, String> isContains(final String str)
	{
		return new GetBy1<Boolean, String>()
		{
			@Override
			public Boolean getBy(String a)
			{
				return a.contains(str);
			}
		};
	}
	
	public static GetBy1<Boolean, String> isContainsCaseInsensitive(String str)
	{
		final String cmp = str.toLowerCase();
		return new GetBy1<Boolean, String>()
		{
			@Override
			public Boolean getBy(String a)
			{
				return a.toLowerCase().contains(cmp);
			}
		};
	}
	
	public static GetBy1<Boolean, String> isStartsWith(final String start)
	{
		return new GetBy1<Boolean, String>()
		{
			@Override
			public Boolean getBy(String a)
			{
				return a.startsWith(start);
			}
		};
	}
	
	public static GetBy1<Boolean, String> isEndsWith(final String ends)
	{
		return new GetBy1<Boolean, String>()
		{
			@Override
			public Boolean getBy(String a)
			{
				return a.endsWith(ends);
			}
		};
	}

	public static GetBy1<String, String> ensureEndsWith(final String postfix)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				if(null == a)
				{
					return null;
				}
				
				if(a.endsWith(postfix))
				{
					return a;
				}
				else
				{
					return a+postfix;
				}
			}
		};
	}
	
	public static final GetBy1<byte[], String> GET_BYTES = new GetBy1<byte[], String>()
	{
		@Override
		public byte[] getBy(String a)
		{
			if(null == a)
			{
				return null;
			}
			
			return a.getBytes();
		}
	};

	public static final GetBy1 PASS_TROUGHT = new GetBy1<String, String>()
	{
		@Override
		public String getBy(String a)
		{
			return a;
		}
	};

	public static GetBy1<String, String> replaceAll(final String from, final String to)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				return StringTools.replaceAllStrings(a, from, to);
			}
		};
	}

	public static GetBy1<String, String> filterPass(GetBy1<Boolean, String> func)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				if(Boolean.TRUE == func.getBy(a))
				{
					return a;
				}
				return null;
			}
		};
	}
	
	public static GetBy1<String, String> filterDrop(GetBy1<Boolean, String> func)
	{
		return new GetBy1<String, String>()
		{
			@Override
			public String getBy(String a)
			{
				if(Boolean.TRUE != func.getBy(a))
				{
					return a;
				}
				return null;
			}
		};
	}
	
	/**/
	
}
