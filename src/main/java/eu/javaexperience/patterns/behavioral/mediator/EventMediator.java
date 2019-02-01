package eu.javaexperience.patterns.behavioral.mediator;

import java.util.ArrayList;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.SimplePublishTools;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish3;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;
import eu.javaexperience.semantic.references.MayNull;

public class EventMediator<E>
{
	protected static Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("EventMediator"));
	
	protected final ArrayList<SimplePublish1<E>> listeners = new ArrayList<>();
	
	public boolean addEventListener(SimplePublish1<E> listener)
	{
		AssertArgument.assertNotNull(listener, "listener");
		synchronized(this)
		{
			if(listeners.contains(listener))
			{
				return false;
			}
			
			listeners.add(listener);
			return true;
		}
	}
	
	public boolean removeEventListener(SimplePublish1<E> listener)
	{
		AssertArgument.assertNotNull(listener, "listener");
		synchronized(this)
		{
			return listeners.remove(listener);
		}
	}
	
	public boolean isListenerRegistered(SimplePublish1<E> listener)
	{
		AssertArgument.assertNotNull(listener, "listener");
		synchronized(this)
		{
			return listeners.contains(listener);
		}
	}
	
	public boolean swapListenersToOrder(SimplePublish1<E> before, SimplePublish1<E> after)
	{
		AssertArgument.assertNotNull(before, "before listener");
		AssertArgument.assertNotNull(after, "after listener");
		synchronized (this)
		{
			int a = listeners.indexOf(before);
			if(a < 0)
			{
				throw new RuntimeException("Listener \"before\" is not registered in the EventMediator");
			}
			
			int b = listeners.indexOf(after);
			
			if(b < 0)
			{
				throw new RuntimeException("Listener \"before\" is not registered in the EventMediator");
			}
			
			if(a < b)
			{
				return false;
			}
			
			listeners.set(a, before);
			listeners.set(b, after);
			
			return true;
		}
	}
	
	public SimplePublish1<E>[] getListeners()
	{
		synchronized (this)
		{
			return listeners.toArray(SimplePublishTools.emptySimplePublish1Array);
		}
	}
	
	public int getNumbersOfListeners()
	{
		synchronized (this)
		{
			return listeners.size();
		}
	}
	
	public void dispatchEvent(E event, @MayNull SimplePublish3<SimplePublish1<E>, E, Throwable> exceptionHandler)
	{
		AssertArgument.assertNotNull(event, "event to dispatch");
		for(SimplePublish1<E> l:getListeners())
		{
			try
			{
				l.publish(event);
			}
			catch(Throwable e)
			{
				if(null != exceptionHandler)
				{
					exceptionHandler.publish(l, event, e);
				}
			}
		}
	}
	
	public static final SimplePublish3<SimplePublish1, Object, Throwable> DEFAULT_EXCEPTION_HANDLER = new SimplePublish3<SimplePublish1, Object, Throwable>()
	{
		@Override
		public void publish(SimplePublish1 a, Object b, Throwable c)
		{
			LoggingTools.tryLogFormatException(LOG, LogLevel.WARNING, c, "DefaultExceltionHandler: exception during dispatching: event `%s` to handler: `%s`", b, a);
		}
	};
	
	public void dispatchEvent(E event)
	{
		dispatchEvent(event, (SimplePublish3) DEFAULT_EXCEPTION_HANDLER);
	}

	public boolean hasListener()
	{
		return !listeners.isEmpty();
	}
}
