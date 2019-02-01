package eu.javaexperience.version;

import java.util.Comparator;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.parse.ParsePrimitive;
import eu.javaexperience.text.StringTools;

public class StdVersion implements Version
{
	protected final int major;
	protected final int minor;
	protected final int patch;
	protected final int tweak;
	
	public StdVersion(int major, int minor, int patch, int tweak)
	{
		AssertArgument.assertGreaterOrEqualsThan(this.major = major, 0, "major");
		AssertArgument.assertGreaterOrEqualsThan(this.minor = minor, 0, "minor");
		AssertArgument.assertGreaterOrEqualsThan(this.patch = patch, 0, "patch");
		AssertArgument.assertGreaterOrEqualsThan(this.tweak = tweak, 0, "tweak");
	}
	
	
	public int getMajor()
	{
		return major;
	}


	public int getMinor()
	{
		return minor;
	}


	public int getPatch()
	{
		return patch;
	}


	public int getTweak()
	{
		return tweak;
	}
	
	public StdVersion withOther
	(
		int major,
		int minor,
		int patch,
		int tweak
	)
	{
		return new StdVersion
		(
			major < 0? this.major:major,
			minor < 0? this.minor:minor,
			patch < 0? this.patch:patch,
			tweak < 0? this.tweak:tweak
		);
	}
	
	public static StdVersion parse(String str)
	{
		String[] vers = str.split("\\.");
		int[] a = new int[4];
		
		int ep = 0;
		for(int i=0; ep < 4 && i < vers.length;++i)
		{
			String s = StringTools.passOnlyNumbers(vers[i]);
			int val = ParsePrimitive.tryParseInt(s, -1);
			if(val >= 0)
			{
				a[ep++] = val;
			}
		}
		
		return new StdVersion(a[0], a[1], a[2], a[3]);
	}
	
	@Override
	public String toString()
	{
		return "v"+major+"."+minor+"."+patch+"."+tweak;
	}


	public boolean isThisNewerThan(StdVersion thanThis)
	{
		int mag = this.compareTo(thanThis);
		return mag > 0;
	}
	
	@Override
	public int hashCode()
	{
		int ret = 31;
		ret = 37 * ret + major;
		ret = 37 * ret + minor;
		ret = 37 * ret + tweak;
		ret = 37 * ret + patch;
		return ret;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(!(o instanceof StdVersion))
		{
			return false;
		}
		
		StdVersion v = (StdVersion) o;
		
		return
				major == v.major
			&&
				minor == v.minor
			&&
				patch == v.patch
			&&
				tweak == v.tweak;
		
	}
	
	@Override
	public int compare(Version _this, Version thanThis)
	{
		return VariVersion.COMPARATOR.compare(this, thanThis);
	}
	
	@Override
	public int compareTo(Version thanThis)
	{
		return compare(this, thanThis);
	}
	
	public static void main(String[] args)
	{
		StdVersion old = StdVersion.parse("0.1.0.0");
		StdVersion newes = StdVersion.parse("1.0.0.0");
		
		AssertArgument.assertEquals(false, old.isThisNewerThan(newes), "a");
		AssertArgument.assertEquals(true, newes.isThisNewerThan(old), "b");
		
		AssertArgument.assertEquals(false, old.isThisNewerThan(old), "c");
		AssertArgument.assertEquals(false, newes.isThisNewerThan(newes), "d");
		
		AssertArgument.assertEquals(true, newes.isThisNewerThan(null), "e");
		AssertArgument.assertEquals(-1, VariVersion.COMPARATOR.compare(null, newes), "f");
		
		
		System.out.println("all passed");
	}

	protected VariVersion variVersion;
	
	@Override
	public VariVersion asVariVersion()
	{
		if(null == variVersion)
		{
			variVersion = new VariVersion(new int[]{major,minor,patch,tweak});
		}
		return variVersion;
	}


	@Override
	public boolean isThisNewerThan(Version thanThis)
	{
		int mag = this.compareTo(thanThis);
		return mag > 0;
	}


	@Override
	public int versionLevel(int lvl)
	{
		switch (lvl)
		{
		case 0: return major;
		case 1: return minor;
		case 2: return patch;
		case 3: return tweak;
		default: return 0;
		}
	}
	
	protected void assertNonNegativeSubversion(int num)
	{
		if(num < 0)
		{
			throw new RuntimeException("Invalid negative version: "+this);
		}
	}
	
	/**
	 * Returns with this reference is version is valid or throws a {@link RuntimeException}
	 * */
	@Override
	public StdVersion assertValidVersion()
	{
		assertNonNegativeSubversion(major);
		assertNonNegativeSubversion(minor);
		assertNonNegativeSubversion(patch);
		assertNonNegativeSubversion(tweak);
		return this;
	}


	@Override
	public Version withDifferentOf(int level, int diff)
	{
		int major = this.major;
		int minor = this.minor;
		int patch = this.patch;
		int tweak = this.tweak;
		
		switch (level)
		{
		case 0: major += diff;break;
		case 1: minor += diff;break;
		case 2: patch += diff;break;
		case 3: tweak += diff;break;
		default:
			break;
		}
	
		return new StdVersion(major, minor, patch, tweak).assertValidVersion();
	}
}
