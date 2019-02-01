package eu.javaexperience.version;

import java.util.Arrays;
import java.util.Comparator;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.text.StringTools;

public class VariVersion implements Version
{
	protected final int[] version;
	
	public VariVersion(int[] ver)
	{
		this.version = ver;
	}
	
	public static VariVersion parse(String str)
	{
		String[] vers = str.split("\\.");
		int[] a = new int[vers.length];
		
		int ep = 0;
		for(int i=0;i < vers.length;++i)
		{
			String s = StringTools.passOnlyNumbers(vers[i]);
			int val = ParsePrimitive.tryParseInt(s, -1);
			if(val >= 0)
			{
				a[ep++] = val;
			}
		}
		
		return new VariVersion(cutTrailingZero(a, ep));
	}
	
	protected static int[] cutTrailingZero(int[] arr, int ep)
	{
		for(int i = ep-1;i >=0 ;--i)
		{
			if(0 == arr[i])
			{
				ep = i;
			}
			else
			{
				break;
			}
		}
		
		if(arr.length == ep)
		{
			return arr;
		}
		else
		{
			return Arrays.copyOf(arr, ep);
		}
	}
	
	
	@Override
	public int hashCode()
	{
		int ret = 31;
		for(int i=0;i<version.length;++i)
		{
			ret = 37 * ret + version[i];
		}
		return ret;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof VariVersion))
		{
			return false;
		}
		
		VariVersion v = (VariVersion) o;
		
		if(version.length != v.version.length)
		{
			return false;
		}
		
		for(int i=0;i< version.length;++i)
		{
			if(version[i] != v.version[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static final Comparator<Version> COMPARATOR = new VariVersion(Mirror.emptyIntArray);
	
	@Override
	public int compare(Version other_this, Version other_thanThis)
	{
		if(null == other_this && null == other_thanThis)
		{
			return 0;
		}
		
		if(null == other_thanThis)
		{
			return 1;
		}
		
		if(null == other_this)
		{
			return -1;
		}
		
		VariVersion _this = other_this.asVariVersion();
		VariVersion thanThis = other_thanThis.asVariVersion();
		
		int maxlen = Math.min(_this.version.length, thanThis.version.length);
		
		for(int i=0;i<maxlen;++i)
		{
			if(_this.version[i] > thanThis.version[i])
			{
				return 1;
			}
			else if(_this.version[i] < thanThis.version[i])
			{
				return -1;
			}
		}
		
		if(_this.version.length > thanThis.version.length)
		{
			return 1;
		}
		else if(_this.version.length < thanThis.version.length)
		{
			return -1;
		}
		
		//minden egyezik
		return 0;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("v");
		for(int i=0;i<version.length;++i)
		{
			if(0 != i)
			{
				sb.append(".");
			}
			sb.append(version[i]);
		}
		
		return sb.toString();
	}
	
	@Override
	public VariVersion asVariVersion()
	{
		return this;
	}

	@Override
	public int compareTo(Version o)
	{
		return compare(this, o);
	}
	
	protected static void assertCmp(String a, String b, int mag)
	{
		VariVersion _a = parse(a);
		VariVersion _b = parse(b);
		
		int res = _a.compareTo(_b);
		
		if(mag == res)
		{
			System.out.println("pass");
		}
		else
		{
			System.out.println("cmp fail: "+_a+" <> "+_b+" => "+res+", req: "+mag);
		}
	}
	
	protected static void assertNewest(String a, String b, boolean a_newest)
	{
		if(a_newest != VariVersion.parse(a).isThisNewerThan(VariVersion.parse(b)))
		{
			throw new RuntimeException("fail");
		}
	}
	
	public static void main(String[] args)
	{
		AssertArgument.assertEquals(parse("1.6.23.1.54.2.4").toString(), "v1.6.23.1.54.2.4", "a");
		AssertArgument.assertEquals(parse("").toString(), "v", "b");
		AssertArgument.assertEquals(parse("1.0.0.1").toString(), "v1.0.0.1", "c");
		AssertArgument.assertEquals(parse("1.0.1.0.0").toString(), "v1.0.1", "d");
		
		assertCmp("", "", 0);
		assertCmp("1", "1", 0);
		assertCmp("1.6.12", "1.6.12", 0);
		assertCmp("1.6.0", "1.6", 0);
		assertCmp("1", "2", -1);
		assertCmp("1.2", "1.3", -1);
		assertCmp("1.3", "1.2", 1);
		assertCmp("1.6.11.5.4.3", "2.0.0.0.0", -1);
		assertCmp("2.0.0.0.0", "1.6.11.5.4.3", 1);
		
		assertNewest("", "", false);
		assertNewest("1", "1", false);
		assertNewest("1.6.12", "1.6.12", false);
		assertNewest("1.6.0", "1.6", false);
		assertNewest("1", "2", false);
		assertNewest("1.2", "1.3", false);
		assertNewest("1.3", "1.2", true);
		assertNewest("1.6.11.5.4.3", "2.0.0.0.0", false);
		assertNewest("2.0.0.0.0", "1.6.11.5.4.3",true);
		
		System.out.println("all pass");
	}
	
	

	@Override
	public boolean isThisNewerThan(Version thanThis)
	{
		int mag = this.compareTo(thanThis);
		return mag > 0;
	}

	@Override
	public int versionLevel(int n)
	{
		if(version.length <= n)
		{
			return 0;
		}
		return version[n];
	}
	
	@Override
	public int getMajor()
	{
		return versionLevel(0);
	}

	@Override
	public int getMinor()
	{
		return versionLevel(1);
	}

	@Override
	public int getPatch()
	{
		return versionLevel(2);
	}

	@Override
	public int getTweak()
	{
		return versionLevel(3);
	}
	
	
	/**
	 * Returns with this reference is version is valid or throws a {@link RuntimeException}
	 * */
	@Override
	public VariVersion assertValidVersion()
	{
		for(int i=0;i<version.length;++i)
		{
			if(version[i] < 0)
			{
				throw new RuntimeException("Invalid negative version: "+this);
			}
		}
		
		return this;
	}
	
	@Override
	public VariVersion withDifferentOf(int level, int diff)
	{
		if(0 == diff)
		{
			return this;
		}
		
		int ret[] = null;
		if(version.length > level)
		{
			ret = Arrays.copyOf(version, version.length);
		}
		else
		{
			ret = Arrays.copyOf(version, level+1);
		}
		
		ret[level] += diff;
		return new VariVersion(ret).assertValidVersion();
	}
}
