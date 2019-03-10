package eu.javaexperience.pdw;

import java.lang.reflect.Method;

import eu.javaexperience.semantic.references.MayNull;

public class BeanTools
{
	/**
	 * getAsdf => ["get", "Asdf"]
	 * isAsdf => ["is", "Asdf"]
	 * setAsdf => ["set", "Asdf"]
	 * get =>["get"]
	 * "" => [""]
	 * "GetValue" => ["GetValue"]
	 * "nextValue" => ["next", "Value"]
	 * 
	 * "casMyValue" => ["cas", "MyValue"]
	 * */
	public static String[] decomposeCommand(String method)
	{
		if(0 == method.length())
		{
			return new String[] {""};
		}
		
		for(int i=0;i<method.length();++i)
		{
			if(Character.isUpperCase(method.charAt(i)))
			{
				if(0 == i)
				{
					return new String[] {method};
				}
				return new String[]{method.substring(0, i), method.substring(i, method.length())};
			}
		}
		
		return new String[]{method};
	}
	
	public static String getCLikeBeanName(Method m)
	{
		return getCLikeBeanNameFromMethodName(m.getName());
	}
	
	public static @MayNull String getCLikeBeanNameFromMethodName(String m)
	{
		String[] cmd = decomposeCommand(m);
		if(cmd.length < 2)
		{
			return null;
		}
		
		String name = cmd[1];
			
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<name.length();++i)
		{
			char c = name.charAt(i);
			if(Character.isUpperCase(c))
			{
				if(i != 0)
				{
					sb.append("_");
				}
				
				sb.append(Character.toLowerCase(c));
			}
			else
			{
				sb.append(c);
			}
		}
		
		name = sb.toString();
		
		return name;
	}
}
