package eu.javaexperience.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.javaexperience.collection.map.OneShotMap;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Mirror;

public class EnvValidationFunctions
{
	public static List<TranslationFriendlyValidationEntry> append(List<TranslationFriendlyValidationEntry> arr, TranslationFriendlyValidationEntry add)
	{
		if(null == arr)
		{
			arr = new ArrayList<>();
		}
		
		arr.add(add);
		return arr;
	}
	
	public static List<TranslationFriendlyValidationEntry> append(List<TranslationFriendlyValidationEntry> arr, List<TranslationFriendlyValidationEntry> add)
	{
		if(null == arr)
		{
			arr = new ArrayList<>();
		}
		
		arr.addAll(add);
		return arr;
	}
	
	public static List<TranslationFriendlyValidationEntry> appendOrThrow(List<TranslationFriendlyValidationEntry> arr, Throwable add)
	{
		if(add instanceof MultiValidationException)
		{
			return append(arr, ((MultiValidationException)add).getResult());
		}
		else if(add instanceof ValidationException)
		{
			return append(arr, ((ValidationException)add).getResult());
		}
		else if(add instanceof ValidationResultException)
		{
			return append(arr, (List<TranslationFriendlyValidationEntry>) (Object) ((ValidationResultException)add).getResult().reportEntries);
		}
		else
		{
			Mirror.propagateAnyway(add);
			return null;
		}
	}
	
	public static void reportOrCarryOn(List<TranslationFriendlyValidationEntry> ents)
	{
		if(null != ents && !ents.isEmpty())
		{
			throw new MultiValidationException(ents);
		}
	}
	
	/*
	TODO
	@FunctionDescription
	(
		functionDescription = "",
		parameters =
		{
			@FunctionVariableDescription(description = "", mayNull = false, paramName = "", type=String.class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=SimplePublish1.class)
	)*/
	public static SimplePublish1<Map<String, Object>> multiAssertVariables(String err_symbol, GetBy1<Boolean, Object> checker, String... vars)
	{
		return new SimplePublish1<Map<String,Object>>()
		{
			@Override
			public void publish(Map<String, Object> a)
			{
				List<TranslationFriendlyValidationEntry> ents = null;
				for(String var:vars)
				{
					Object val = a.get(var);
					if(Boolean.TRUE != checker.getBy(val))//wrapping into try-catch, we can add sub checker's validation exception to this throwing one   
					{
						ents = append(ents, new TranslationFriendlyValidationEntry(var, err_symbol, new OneShotMap<String, String>("value", String.valueOf(val))));
					}
				}

				reportOrCarryOn(ents);
			}
		};
	}
	
	public static <C extends Comparable<C>> SimplePublish1<Map<String, Object>> multiAssertVariablesBetween
	(
		String err_symbol,
		C _min,
		C _max,
		String... vars
	)
	{
		return new SimplePublish1<Map<String,Object>>()
		{
			@Override
			public void publish(Map<String, Object> a)
			{
				List<TranslationFriendlyValidationEntry> ents = null;
				for(String var:vars)
				{
					C val = (C) a.get(var);
					
					CastTo ct = CastTo.getCasterRestrictlyForTargetClass(val.getClass());
					
					C min = (C) ct.cast(_min);
					C max = (C) ct.cast(_max);
					
					if(null == min || null == max)
					{
						if(null == min)
						{
							ents = append(ents, variableTypeNotSuitable(var, val.getClass(), String.valueOf(_min)));
						}
						
						if(null == max)
						{
							ents = append(ents, variableTypeNotSuitable(var, val.getClass(), String.valueOf(_max)));
						}
					}
					else if
					(
						!(	
							min.compareTo(val) <= 0
						&&
							val.compareTo(max) <= 0
						)
					)//wrapping into try-catch, we can add sub checker's validation exception to this throwing one   
					{
						SmallMap add = new SmallMap<>();
						add.put("value", String.valueOf(val));
						add.put("min", String.valueOf(min));
						add.put("max", String.valueOf(max));
						ents = append(ents, new TranslationFriendlyValidationEntry(var, err_symbol, add));
					}
				}

				reportOrCarryOn(ents);
			}
		};
	}
	
	
	protected static TranslationFriendlyValidationEntry variableTypeNotSuitable(String variable, Class type, String value)
	{
		Map<String, String> add = new SmallMap<>();
		add.put("value", value);
		add.put("asType", type.getName());
		
		return new TranslationFriendlyValidationEntry(variable, "Incompatible type", add);
	}
	
	public static SimplePublish1<Map<String, Object>> multiValidate(SimplePublish1<Map<String, Object>>... validators)
	{
		return new SimplePublish1<Map<String,Object>>()
		{
			@Override
			public void publish(Map<String, Object> a)
			{
				List<TranslationFriendlyValidationEntry> ents = null;				
				for(SimplePublish1<Map<String, Object>> validator:validators)
				{
					try
					{
						validator.publish(a);
					}
					catch(Throwable t)
					{
						ents = appendOrThrow(ents, t);
					}
				}
				
				reportOrCarryOn(ents);
			}
		};
	}
	
	/*public static SimplePublish1<Map<String, Object>> assertVariables(SimplePublish1<Object> checker, String... variables)
	{
		
	}*/
}
