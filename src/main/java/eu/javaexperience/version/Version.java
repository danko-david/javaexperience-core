package eu.javaexperience.version;

import java.util.Comparator;

public interface Version extends Comparable<Version>, Comparator<Version>
{
	public int getMajor();
	public int getMinor();
	public int getPatch();
	public int getTweak();
	
	/**
	 * Get the version level:
	 * 0: major
	 * 1: minor
	 * 2: patch
	 * 3: tweak
	 * 
	 * ... n: build
	 * ... m: ...
	 * ..
	 * ..
	 * ..
	 * */
	public int versionLevel(int lvl);
	
	public Version withDifferentOf(int level, int diff);
	
	public boolean isThisNewerThan(Version thanThis);
	public VariVersion asVariVersion();
	
	public Version assertValidVersion();
}