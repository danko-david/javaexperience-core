package eu.javaexperience.resource.pool;


import java.util.concurrent.TimeUnit;

import eu.javaexperience.semantic.references.MayNotNull;
import eu.javaexperience.semantic.references.MayNull;

/**
 * A generic interface to monitor resource pools.
 * */
public interface ResourcePool<T>
{
	/**
	 * Try to acquire a resource represented by an instance.
	 * Behaviours depends from the implementations, can return immediately,
	 * or block until free resource available, etc. 
	 * */
	public @MayNotNull T acquireResource() throws InterruptedException;
	
	/**
	 * Try acquire a resource, if available  within the given time.
	 * returns null if time is out. 
	 * */
	public @MayNull T tryAcquireResource(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException;
	
	/**
	 * Returns a resource, if available but won't try to create if there's no
	 * free instance. In this case return null.
	 * 
	 * */
	public @MayNull T pollResource();
	
	/**
	 * Releases the acquired resource represented by given instance.
	 * Behaviours depends from the implementations,
	 * like {@link TrackedResourcePool} throws exception if a resource double freed.
	 * */
	public void releaseResource(@MayNotNull T resource);
	
	/**
	 * Destroys the acquired resource represented by given instance.
	 * Behaviours depends from the implementations,
	 * like {@link TrackedResourcePool} throws exception if a resource double destroyed.
	 * */
	public void destroyResource(@MayNotNull T resource);

	/**
	 * Returns the number of currently acquirable resources.
	 * (how many free resources are in this pool?) 
	 * */
	public int getFreeResourcesCount();
	
	/**
	 * Returns the number of currently used resources.
	 * (how many resources are issued)
	 * */
	public int getIssuedResourcesCount();
	
	/**
	 * Returns the number of maximal creatable instances,
	 * the means that free and issued resource count can never exceed this number.
	 * (theorically, see {@link SimplifiedResourcePool#getFreeResourcesCount()}) 
	 * {@link Integer#MAX_VALUE} or negative value if pool is unlimited.
	 * */
	public int getResourceLimitCount();
}
