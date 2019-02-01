package eu.javaexperience.text;

import java.io.ByteArrayOutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class StringTools
{
	private StringTools(){};
	
	protected static final String RANDOM_STRING_CHARS = "0123456789qwertzuiopasdfghjklyxcvbnmQWERTZUIOPASDFGHJKLYXCVBNM";

	/**
	 * len hosszúságú véletlen stringet ad vissza. (angol kis,nagy betűk és számok)
	 * */
	public static String randomString(int len)
	{
		return randomStringFrom(RANDOM_STRING_CHARS, len);
	}
	
	public static String randomStringFrom(String set, int len)
	{
		final int strlen = set.length();
		StringBuilder sb = new StringBuilder(len);
		for(int i=0;i<len;i++)
		{
			sb.append(set.charAt((int) (Math.random()*strlen)));
		}
		sb.trimToSize();
		return sb.toString();
	}

	
	/**
	 * csak 0-9 akraktereket engedi át se . se , se E vagy e 
	 * */
	public static String passOnlyNumbers(String str)
	{
		char[] chrs = str.toCharArray();
		int ep = 0;
		for(int i=0;i<chrs.length;i++)
			if(chrs[i] >= '0' && chrs[i] <='9')
				chrs[ep++] = chrs[i];
		
		return new String(chrs, 0, ep);
	}
	
	/**
	 * Deprecated beacuse of an ugly behaviour: if `search` not found in the
	 * `src` the `src` string returned instead of a specified.
	 * Hoverer old codes used this and somtimes counting on this behavior. 
	 * */
	@Deprecated
	public static String getSubstringAfterLastString(String src, String search)
	{
		return getSubstringAfterLastString(src, search, src);
	}
	
	public static String getSubstringAfterLastString(String src, String search, String _default)
	{
		int last = src.lastIndexOf(search);
		if(last == -1)
		{
			return _default;
		}
		
		return src.substring(last+search.length());
	}
	
	/**
	 * Deprecated beacuse of an ugly behaviour: if `search` not found in the
	 * `src` the `src` string returned instead of a specified.
	 * Hoverer old codes used this and somtimes counting on this behavior. 
	 * */
	@Deprecated
	public static String getSubstringBeforeLastString(String src, String search)
	{
		return getSubstringBeforeLastString(src, search, src);
	}
	
	public static String getSubstringBeforeLastString(String src, String search, String _default)
	{
		int last = src.lastIndexOf(search);
		if(last == -1)
		{
			return _default;
		}
		
		return src.substring(0,last);
	}
	
	/**
	 * Deprecated beacuse of an ugly behaviour: if `before` and `after` not
	 * found in the `src` the `src` string reeturned instead of a specified,
	 * */
	@Deprecated
	public static String getSubstringAfterFirstString(String src, String search)
	{
		return getSubstringAfterFirstString(src, search, src);
	}
	
	public static String getSubstringAfterFirstString(String src, String search, String _default)
	{
		int last = src.indexOf(search);
		if(last == -1)
		{
			return _default;
		}
		return src.substring(last+search.length());
	}
	
	/**
	 * Deprecated beacuse of an ugly behaviour: if `before` and `after` not
	 * found in the `src` the `src` string reeturned instead of a specified,
	 * */
	@Deprecated
	public static String getSubstringBeforeFirstString(String src, String search)
	{
		return getSubstringBeforeFirstString(src, search, src);
	}
	
	public static String getSubstringBeforeFirstString(String src, String search, String _default)
	{
		int last = src.indexOf(search);
		if(last == -1)
		{
			return _default;
		}
		
		return src.substring(0,last);
	}
	
	public static String untilTerminator(String str,char terminator)
	{
		if(null == str)
		{
			return null;
		}
		
		int ind = str.indexOf(terminator);
		if(ind < 0)
			return str;
		return str.substring(0,ind);
	}
	
	public static String repeatChar(char c, int length)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<length;i++)
			sb.append(c);
		
		return sb.toString();
	}
	
	public static String repeatString(String c, int length)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<length;i++)
			sb.append(c);
		
		return sb.toString();
	}
	
	public static String[] whitoutNullAndEmptyString(String... arr)
	{
		for(String s:arr)
			if(s == null ||s.isEmpty())
				return removeNullAndEmptyStrings(arr);
		
		return arr;
	}
	
	private static String[] removeNullAndEmptyStrings(String... arr)
	{
		String[] ret = new String[arr.length];
		int ep = 0;
		
		for(String s:arr)
			if(s != null && !s.isEmpty())
				ret[ep++] = s;
		
		return Arrays.copyOf(ret, ep);
	}
	
	public static String replaceAllStrings(String source, String search, String replacement)
	{
		int s = source.indexOf(search);
		if(s < 0)
			return source;
		
		int prev = 0;
		StringBuilder sb = new StringBuilder();
		int l = search.length();
		
		while(s > -1)
		{
			sb.append(source.subSequence(prev, s));
			sb.append(replacement);
			prev = s+l;
			s = source.indexOf(search, s+l);
		}
		
		sb.append(source.substring(prev));
		
		return sb.toString();
	}
	
	public static String replaceAllStringsCaseInsensitive(String source, String search, String replacement)
	{
		String lower_source = source.toLowerCase();
		String lower_search = search.toLowerCase();
		
		int s = lower_source.indexOf(lower_search);
		if(s < 0)
		{
			return source;
		}
		
		int prev = 0;
		StringBuilder sb = new StringBuilder();
		int l = search.length();
		
		while(s > -1)
		{
			sb.append(source.subSequence(prev, s));
			sb.append(replacement);
			prev = s+l;
			s = lower_source.indexOf(lower_search, s+l);
		}
		
		sb.append(source.substring(prev));
		
		return sb.toString();
	}
	
	public static String toString(Object o)
	{
		return o == null? "null":o.toString();
	}
	
	public static String clampLength(String str, int maxlength)
	{
		if(null == str)
			return null;
		
		if(str.length() > maxlength)
		{
			return str.substring(0, maxlength);
		}
		
		return str;
	}
	
	/**
	 * Deprecated beacuse of an ugly behaviour: if `before` and `after` not
	 * found in the `src` the `src` string reeturned instead of a specified,
	 * */
	@Deprecated
	public static String getFirstBetween(String src, String before, String after)
	{
		return getFirstBetween(src, before, after, src);
	}
	
	public static String getFirstBetween(String src, String before, String after, String _default)
	{
		int min = src.indexOf(before);
		if(min < 0)
		{
			return _default;
		}
		min+= before.length();
		
		int max = src.indexOf(after, min);
		if(max < 0)
		{
			return _default;
		}
		
		return src.substring(min, max);
	}
	
	/**
	 * Deprecated beacuse of an ugly behaviour: if `before` and `after` not
	 * found in the `src` the `src` string reeturned instead of a specified,
	 * */
	@Deprecated
	public static String getLastBetween(String src, String before, String after)
	{
		return getLastBetween(src, before, after, src);
	}
	
	public static String getLastBetween(String src, String before, String after, String _default)
	{
		int min = src.lastIndexOf(before);
		if(min < 0)
		{
			return _default;
		}
		
		int max = -1;
		if(before.equals(after))
		{
			int n = src.lastIndexOf(before, min-before.length());
			if(n < 0)
			{
				return _default;
			}
			else
			{
				max = min;
				min = n;
			}
		}
		else
		{
			max = src.indexOf(after, min+before.length());
		}
		 
		if(max < 0)
		{
			return _default;
		}
		
		return src.substring(min+before.length(), max);
	}
	
	public static final Pattern diacritics = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); 

	public static String deAccent(String str)
	{
		String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
		return diacritics.matcher(nfdNormalizedString).replaceAll("");
	}
	
	public static String[] plainSplit(String source, String splitter)
	{
		int s = source.indexOf(splitter);
		if(s < 0)
		{
			return new String[]{source};
		}
		
		int prev = 0;
		int l = splitter.length();
		
		ArrayList<String> result = new ArrayList<>();
		while(s > -1)
		{
			result.add(source.substring(prev, s));
			prev = s+l;
			s = source.indexOf(splitter, s+l);
		}
		
		result.add(source.substring(prev));
		
		return result.toArray(Mirror.emptyStringArray);
	}

	public static String cutToBytes(String str, int size)
	{
		//TODO single char => String => getBytes => append or return. 
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		//int ep = 0;
		for(int i=0;i<str.length();++i)
		{
			String val = String.valueOf(str.charAt(i));
			byte[] b = val.getBytes();
			if(out.size() + b.length > size)
			{
				break;
			}
			else
			{
				out.write(b, 0, b.length);
			}
		}
		
		return new String(out.toByteArray());
	}
	
	public static String escapeToJavaString(String str)
	{
		str = StringTools.replaceAllStrings(str, "\b", "\\b");
		str = StringTools.replaceAllStrings(str, "\n", "\\n");
		str = StringTools.replaceAllStrings(str, "\t", "\\t");
		str = StringTools.replaceAllStrings(str, "\r", "\\r");
		str = StringTools.replaceAllStrings(str, "\f", "\\f");
		str = StringTools.replaceAllStrings(str, "\"", "\\\"");
		str = StringTools.replaceAllStrings(str, "\\", "\\\\");
		return str;
	}
	
	public static String orEmptyString(String str)
	{
		if(null == str)
		{
			return "";
		}
		return str;
	}
	

	public static boolean isNullOrEmpty(String nev)
	{
		if(null == nev || nev.length() == 0)
		{
			return true;
		}
		return false;
	}
	
	public static boolean isNullOrTrimEmpty(String nev)
	{
		if(null == nev || nev.trim().length() == 0)
		{
			return true;
		}
		return false;
	}

	public static String ensureStartsWith(String subject, String start)
	{
		if(subject.startsWith(start))
		{
			return subject;
		}
		
		return start+subject;
	}
	
	public static String multiReplaceAllString
	(
		String subject,
		Map<String, String> map
	)
	{
		String ret = subject;
		for(Entry<String, String> kv: map.entrySet())
		{
			ret = StringTools.replaceAllStrings(ret, kv.getKey(), kv.getValue());
		}
		
		return ret;
	}

	public static String ensureEndsWith(String subject, String end)
	{
		if(!subject.endsWith(end))
		{
			return subject+end;
		}
		return subject;
	}

	public static boolean isIn(String subject, String... possible)
	{
		for(String p:possible)
		{
			if(Mirror.equals(subject, p))
			{
				return true;
			}
		}
		
		return false;
	}

	public static String concat(String... elements)
	{
		StringBuilder sb = new StringBuilder();
		for(String s:elements)
		{
			sb.append(s);
		}
		return sb.toString();
	}

	public static String concatNotNull(String... strings)
	{
		StringBuilder sb = new StringBuilder();
		for(String s:strings)
		{
			if(null != s)
			{
				sb.append(s);
			}
		}
		return sb.toString();
	}

	public static String toStringOrNull(Object object)
	{
		if(null == object)
		{
			return null;
		}
		else
		{
			return object.toString();
		}
	}

	public static int occurrenceIn(String subject, String search)
	{
		if(search.length() < 1)
		{
			return -1;
		}
		
		int occ = 0;
		int start = 0;
		do
		{
			int index = subject.indexOf(search, start);
			if(index < 0)
			{
				return occ;
			}
			
			++occ;
			start = index+search.length();
		}
		while(true);
	}

	public static String join(String delimiter, Collection<String> values)
	{
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(String s:values)
		{
			if(++i > 1)
			{
				sb.append(delimiter);
			}
			sb.append(s);
		}
		
		return sb.toString();
	}
	
	public static String join(String delimiter, String... strArray)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<strArray.length;++i)
		{
			if(0 != i)
				sb.append(delimiter);
			
			sb.append(strArray[i]);
		}
		
		return sb.toString();
	}
	
	public static String[] replaceAll(String[] arr, String from, String to)
	{
		String[] ret = ArrayTools.copy(arr);
		
		for(int i=0;i<ret.length;++i)
		{
			ret[i] = StringTools.replaceAllStrings(ret[i], from, to);
		}
		
		return ret;
	}

	public static String splitModifyJoin(String text, Pattern split, GetBy1<String, String> transform, String joinGlue)
	{
		String[] ss = split.split(text);
		for(int i=0;i<ss.length;++i)
		{
			ss[i] = transform.getBy(ss[i]);
		}
		
		return join(joinGlue, ss);
	}


	public static String tryTrim(String str)
	{
		if(null == str)
		{
			return null;
		}
		
		return str.trim();
	}

	public static String passOnlyNumbersAndSmallAscii(String etc)
	{
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<etc.length();++i)
		{
			char c = etc.charAt(i);
			if
			(
				('0' <= c && c<= '9')
				||
				('a' <= c && c <= 'z')
			)
			{
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
}