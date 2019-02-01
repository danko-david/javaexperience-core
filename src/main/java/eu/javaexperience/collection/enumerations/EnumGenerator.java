package eu.javaexperience.collection.enumerations;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.regex.RegexTools;
import eu.javaexperience.text.StringTools;

public class EnumGenerator
{
	
	public static void main(String[] args) throws Throwable
	{
		args = new String[]{"/tmp/1"};
		
		GetBy1<String, String> comment = null;
		
		try
		(
			FileInputStream fis = new FileInputStream(args[0]);
			InputStreamReader reader = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(reader);
		)
		{
			generate(System.out, br, comment, false, false);
		}
	}
	
	/**
	 * adsfdfsd ááklhkl.-fdgdfg gfddfg	1	6	15	izé => adsfdfsd_ááklhkl_fdgdfg_gfddfg("adsfdfsd ááklhkl.-fdgdfg gfddfg", 1, 6, 15, "izé"),
	 * @throws IOException 
	 * */
	public static void generate(Appendable dst, BufferedReader read, GetBy1<String, String> comment, Boolean... isString) throws IOException
	{
		HashSet<String> registeredEnumNames = new HashSet<>();
		String line = null;
		while(null != (line = read.readLine()))
		{
			String[] elems = RegexTools.TAB.split(line); 
			
			if(elems.length < 1)
			{
				continue;
			}
			
			String enumName = enumName(elems[0], registeredEnumNames);

			
			String c = null;
			if(null != comment)
			{
				c = comment.getBy(elems[0]);
				if(null != c)
				{
					dst.append(c);
				}
			}
			dst.append("\t");
			dst.append(enumName);
			dst.append("(\"");
			dst.append(StringTools.escapeToJavaString(elems[0]));
			dst.append("\"");
			
			for(int i=1;i<elems.length && i < isString.length+1;++i)
			{
				dst.append(", ");
				if(Boolean.TRUE == isString[i-1])
				{
					dst.append("\"");
					dst.append(StringTools.escapeToJavaString(elems[i]));
					dst.append("\"");
				}
				else if(Boolean.FALSE == isString[i-1])
				{
					dst.append(elems[i]);
				}
			}
			
			dst.append("),\n");
		}
	}
	
	public static String enumName(String str,Collection<String> coll)
	{
		str = str.trim();
		str = str.toUpperCase();
		str = str.replace("\\.", "");
		str = str.replace("–", "");
		str = str.replace(" ", "_");
		str = str.replace("-", "_");
		str = str.replace(",", "_");
		str = str.replace("(", "");
		str = str.replace(")", "");
		str = StringTools.deAccent(str);
		while(true)
		{
			if(coll.contains(str))
			{
				str = str+"_";
			}
			else
			{
				coll.add(str);
				return str;
			}
		}
	}
}
