package eu.javaexperience.patterns.creational.builder;

import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

public abstract class PublisherBuilder<P, R, I> implements SimplePublish1<P> 
{
	protected R result;
	
	public void assertInitialized()
	{
		if(null == result)
		{
			throw new RuntimeException("PublisherBuilder is not yet initialized.");
		}
	}
	
	public void assertNotInitialized()
	{
		if(null != result)
		{
			throw new RuntimeException("PublisherBuilder is already initialized.");
		}
	}
	
	
	public void initialize(I init)
	{
		assertNotInitialized();
		result = internalInitialize(init);
		if(null == result)
		{
			throw new RuntimeException("PublisherBuilder initialization failed");
		}
	}
	
	public void close(){}
	
	protected abstract R internalInitialize(I init);
	
	
	public R getResult()
	{
		return result;
	}

	public boolean isOpened()
	{
		return null != result;
	}
}
