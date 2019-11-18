package eu.javaexperience.text;

import java.util.Arrays;

import eu.javaexperience.reflect.Mirror;

/**
 * A mutable version of String which implements {@link CharSequence}.
 * I basically use for text processing purpose.  
 * */
public class MutaString implements CharSequence
{
	protected int len = 0;
	protected char[] data = Mirror.emptyCharArray;

	public MutaString()
	{}

	public MutaString(String str)
	{
		replaceWith(str);
	}

	@Override
	public char charAt(int index)
	{
		return data[index];
	}

	@Override
	public int length()
	{
		return len;
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		if(end < start)
			return null;

		MutaString ret = new MutaString();
		ret.data = new char[(ret.len = end - start)];
		for (int i = 0; i < ret.len; i++)
			ret.data[i] = data[start + i];

		return ret;
	}

	public boolean removeBTW(int start, int end)
	{
		int diff = end - start;
	
		if(diff < 1)
			return false;
		
		len -= diff;
		for (int i = start; i < len; i++)
			data[i] = data[i + diff];
		
		return true;
	}

	@Override
	public String toString()
	{
		return new String(data, 0, len);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof MutaString))
			return false;
		
		return equals((MutaString) o);
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	public boolean equals(MutaString obj)
	{
		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (obj.len != len)
			return false;

		for (int i = 0; i < len; i++)
			if (obj.data[i] != data[i])
				return false;

		return true;
	}

	/**
	 * Removes the first occurrence from the MutaString.
	 * 
	 * @param str given String to remove.
	 * @return String found and removed?true:false  
	 * */
	public boolean removeStringOnce(String str)
	{
		if(str == null)
			return false;

		int indexof = this.indexOf(str);
		if(indexof != -1)
		{
			removeBTW(indexof, indexof+str.length());
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Origin from: java.lang.String
	 *@param   source       the characters being searched.
	 * @param   sourceOffset offset of the source string.
	 * @param   sourceCount  count of the source string.
	 * @param   target       the characters being searched for.
	 * @param   targetOffset offset of the target string.
	 * @param   targetCount  count of the target string.
	 * @param   fromIndex    the index to begin searching from.
	 */
	static int indexOf(CharSequence source,CharSequence target, int fromIndex,int endindex)
	{
		if(target == null)
			return -1;

		if (fromIndex >= endindex)
		{
			return (target.length() == 0 ? endindex : -1);
		}
		if (fromIndex < 0)
		{
			fromIndex = 0;
		}
		if (target.length() == 0)
		{
			return fromIndex;
		}

		char first = target.charAt(0);
		int max = endindex - target.length();

		for (int i = fromIndex; i <= max; i++)
		{
			/* Look for first character. */
			if (source.charAt(i) != first)
			{
				while (++i <= max && source.charAt(i) != first)
				{}
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max)
			{
				int j = i + 1;
				int end = j + target.length() - 1;
				for (int k = 1; j < end	&& source.charAt(j) == target.charAt(k); j++, k++)
				{}

				if (j == end)
				{
					/* Found whole string. */
					return i;
				}
			}
		}
		return -1;
	}

	static int indexOf(CharSequence source,CharSequence target, int fromIndex)
	{
		if(target == null)
			return -1;

		if (fromIndex >= source.length())
		{
			return (target.length() == 0 ? source.length() : -1);
		}
		if (fromIndex < 0)
		{
			fromIndex = 0;
		}
		if (target.length() == 0)
		{
			return fromIndex;
		}

		char first = target.charAt(0);
		int max = source.length() - target.length();

		for (int i = fromIndex; i <= max; i++)
		{
			/* Look for first character. */
			if (source.charAt(i) != first)
			{
				while (++i <= max && source.charAt(i) != first)
				{}
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max)
			{
				int j = i + 1;
				int end = j + target.length() - 1;
				for (int k = 1; j < end	&& source.charAt(j) == target.charAt(k); j++, k++)
				{}

				if (j == end)
				{
					/* Found whole string. */
					return i;
				}
			}
		}
		return -1;
	}

	public int indexOf(CharSequence str)
	{
		return indexOf(str, 0);
	}

	public int indexOf(CharSequence str, int fromIndex)
	{
		return indexOf(this, str, fromIndex);
	}

	public int indexOf(CharSequence str, int fromIndex, int toIndex)
	{
		return indexOf(this, str, fromIndex,toIndex);
	}

	public void replaceWith(String str)
	{
		data = new char[len = str.length()];
		for (int i = 0; i < len; i++)
			data[i] = str.charAt(i);
	}
	
	public void replaceWith(char[] cbuf, int off, int len)
	{
		data = Arrays.copyOfRange(cbuf, off, off+len);
		this.len = len; 
	}
	
	/**
	 * It wraps the given char array if offset is 0.
	 * It might be a dangerous operation beacuse modifying this MutaString
	 * affects the char array given here  
	 * */
	@Deprecated
	public void actAs(char[] cbuf, int off, int len)
	{
		if(0 == off)
		{
			this.data = cbuf;
			this.len = len;
		}
		else
		{
			replaceWith(cbuf, off, len);
		}
	}

}