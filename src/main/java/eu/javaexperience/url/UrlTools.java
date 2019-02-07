package eu.javaexperience.url;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.reflect.Mirror;

public class UrlTools
{
	private UrlTools() {}
	
	public static final Pattern domSplit = Pattern.compile("\\.");
	public static final Pattern pathSplit = Pattern.compile("/+");
	
	public static void modifyUrlDecode(String[] arr)
	{
		for(int i= 0;i<arr.length;i++)
			arr[i] = URLDecoder.decode(arr[i]);
	}
	
	public static Map<String,String[]> resolvMap(String params)
	{
		String[] pairs = params.split("&");
		Map<String,String[]> ret = new SmallMap<>();
		for (String pair : pairs)
		{
			int idx = pair.indexOf("=");
			String key = idx > 0 ? pair.substring(0, idx): pair;
			String value = idx > 0 && pair.length() > idx + 1 ?pair.substring(idx + 1): "";
			if(null != value)
			{
				value = URLDecoder.decode(value);
			}
			String[] add = ret.get(key);
			if(null == add)
			{
				add = new String[]{value};
			}
			else
			{
				add = Arrays.copyOf(add, add.length+1);
				add[add.length-1] = value;
			}
			ret.put(key, add);
		}
		return ret;
	}
	
	public static Map<String,String> convMapMulti(Map<String,String[]> map)
	{
		Map<String,String> ret = new HashMap<>();
		for(Entry<String, String[]> kv:map.entrySet())
			if(kv.getValue() != null)
				if(kv.getValue().length > 0)
					ret.put(kv.getKey(), kv.getValue()[0]);
		
		return ret;
	}
	
	public static void fillMultiMap(Map<String, String> dst, Map<String,String[]> map)
	{
		for(Entry<String, String[]> kv:map.entrySet())
			if(kv.getValue() != null)
				if(kv.getValue().length > 0)
					dst.put(kv.getKey(), kv.getValue()[0]);
	}
	
	public static Map<String,String> convMap(Map<String,String> map)
	{
		Map<String,String> ret = new HashMap<>();
		for(Entry<String, String> kv:map.entrySet())
			if(kv.getValue() != null)
				ret.put(kv.getKey(), kv.getValue());
		
		return ret;
	}
	
	/**
	 * ami a ? után van: asd=bsd&fgt=jhf
	 * @throws UnsupportedEncodingException 
	 * */
	public static void processArgsRequest(String line, Map<String,String[]> target) throws UnsupportedEncodingException
	{
		if("".equals(line))
		{
			return;
		}
		String[] pairs = line.split("&"); //ez megint gyorsabb
		for (String pair : pairs)
		{
			int idx = pair.indexOf("=");
			String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : "";
			putParam(target, key, value);
		}	
	}
	
	public static void putParam(Map<String,String[]> map, String key, String value)
	{
		String[] in = map.get(key);
		if(in == null)
			map.put(key, new String[]{value});
		else
			map.put(key, ArrayTools.arrayAppend(in, value));
	}
	
	public static String getParam(Map<String,String[]> map, String key)
	{
		return optParam(map, key, null);
	}
	
	public static String optParam(Map<String,String[]> map, String key, String def)
	{
		String[] ks = map.get(key);
		if(ks == null)
			return def;
		
		if(ks.length > 0)
			return ks[0];
		
		return def;
	}
	
	public static String renderRequestParams(Map<String, Object> params)
	{
		StringBuilder sb = new StringBuilder();
		renderRequestParams(params, sb);
		return sb.toString();
	}
	
	public static void renderRequestParams(Map<String, ? extends Object> params, Appendable app)
	{
		if(null == params || params.size() == 0)
		{
			return;
		}
		try
		{
			boolean nfirst = false;
			app.append("?");
			for(Entry<String, ?> kv:params.entrySet())
			{
				String k = kv.getKey();
				Object val = kv.getValue();
				if(null != k)
				{
					if(nfirst)
					{
						app.append("&");
					}
					nfirst = true;
					
					if(val instanceof String[])
					{
						for(String s:(String[])val)
						{
							appendParam(app, k, s);
						}
					}
					else if(val instanceof String)
					{
						appendParam(app, k, (String) val);
					}
					else if(null == val)
					{
						appendParam(app, k, null);
					}
					else
					{
						appendParam(app, k, val.toString());
					}
				}
			}
		}
		catch(Exception e)
		{
			Mirror.throwSoftOrHardButAnyway(e);
		}
	}
	
	public static void appendParam(Appendable app, String key, String val) throws IOException
	{
		app.append(key);
		
		if(null != val && val.length() > 0)
		{
			app.append("=");
			app.append(URLEncoder.encode(val));
		}
	}
}
