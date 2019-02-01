package eu.javaexperience.collection.set;

import java.util.Set;

import eu.javaexperience.collection.NullCollection;

public class NullSet<T> extends NullCollection<T> implements Set<T>
{
	public static final NullSet instance = new NullSet();
}