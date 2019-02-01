package eu.javaexperience.generic;

import java.io.Closeable;

public interface ResourceLike extends Acquireable, Closeable
{
	public boolean isAcquired();
}
