package eu.javaexperience.resource.pool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TrackedLimitedResourcePoolTest
{
	@Test
	public void testPool1() throws Exception
	{
		TrackedLimitedResourcePool<String> pool = new TrackedLimitedResourcePool<>(()->"asdf", 10);
		
		//zero at start, because we don't acquired one yet [not a bug, it's a feature :)]
		assertEquals(0, pool.getFreeResourcesCount());
		assertEquals(10, pool.getResourceLimitCount());
		assertEquals(0, pool.getIssuedResourcesCount());
		
		try(IssuedResource<String> res = pool.acquireResource())
		{
			assertEquals("asdf", res.getResource());
			
			assertEquals(0, pool.getFreeResourcesCount());
			assertEquals(10, pool.getResourceLimitCount());
			assertEquals(1, pool.getIssuedResourcesCount());
		}
		
		assertEquals(1, pool.getFreeResourcesCount());
		assertEquals(10, pool.getResourceLimitCount());
		assertEquals(0, pool.getIssuedResourcesCount());
		
		IssuedResource<String> res = pool.acquireResource();
		
		assertEquals(0, pool.getFreeResourcesCount());
		assertEquals(10, pool.getResourceLimitCount());
		assertEquals(1, pool.getIssuedResourcesCount());
		
		pool.destroyResource(res);
		
		assertEquals(0, pool.getFreeResourcesCount());
		assertEquals(10, pool.getResourceLimitCount());
		assertEquals(0, pool.getIssuedResourcesCount());
	}
	
	/**
	 * This test shows you to don't mix resource cleanup (destroy)
	 * 	process with try-resource-catch blocks 
	 * */
	@Test(expected=RuntimeException.class)
	public void testPool2() throws Exception
	{
		TrackedLimitedResourcePool<String> pool = new TrackedLimitedResourcePool<>(()->"asdf", 10);
		
		//zero at start, because we don't acquired one yet [not a bug, it's a feature :)]
		assertEquals(0, pool.getFreeResourcesCount());
		assertEquals(10, pool.getResourceLimitCount());
		assertEquals(0, pool.getIssuedResourcesCount());
		
		try(IssuedResource<String> res = pool.acquireResource())
		{
			assertEquals("asdf", res.getResource());
			
			assertEquals(0, pool.getFreeResourcesCount());
			assertEquals(10, pool.getResourceLimitCount());
			assertEquals(1, pool.getIssuedResourcesCount());
			
			pool.destroyResource(res);
		}//double release RuntimeException
	}
	
	@Test
	public void testPool3() throws Exception
	{
		TrackedLimitedResourcePool<String> pool = new TrackedLimitedResourcePool<>(()->"asdf", 2);
		
		//zero at start, because we don't acquired one yet [not a bug, it's a feature :)]
		assertEquals(0, pool.getFreeResourcesCount());
		assertEquals(2, pool.getResourceLimitCount());
		assertEquals(0, pool.getIssuedResourcesCount());
		
		{
			pool.acquireResource();
			assertEquals(0, pool.getFreeResourcesCount());
			assertEquals(2, pool.getResourceLimitCount());
			assertEquals(1, pool.getIssuedResourcesCount());
		}
		{
			pool.acquireResource();
			assertEquals(0, pool.getFreeResourcesCount());
			assertEquals(2, pool.getResourceLimitCount());
			assertEquals(2, pool.getIssuedResourcesCount());
		}
		
		{
			long t0 = System.currentTimeMillis();
			assertNull(pool.tryAcquireResource(1, TimeUnit.SECONDS));
			assertTrue(System.currentTimeMillis()-t0 > 900);
		}
	}
	
}
