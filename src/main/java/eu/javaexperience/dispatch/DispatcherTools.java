package eu.javaexperience.dispatch;

import java.util.List;

import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.reflect.Mirror;

public class DispatcherTools
{
	public static final Dispatcher[] emptyDispatcherArray = new Dispatcher[0];
	
	public static <CTX> boolean subDispatch(CTX ctx, List<Dispatcher<CTX>> dispatchers)
	{
		if(null == dispatchers)
		{
			return false;
		}
		
		if(!dispatchers.isEmpty())
		{
			for(Dispatcher<CTX> c: dispatchers.toArray(emptyDispatcherArray))
			{
				if(c.dispatch(ctx))
				{
					return true;
				}
			}
		}
		
		return false;
	}

	public static <CTX> SimplePublish1<CTX> toSimplePublish(final Dispatcher<CTX> actor)
	{
		return new SimplePublish1<CTX>()
		{
			@Override
			public void publish(CTX a)
			{
				actor.dispatch(a);
			}
		};
	}

	public static <CTX> Dispatcher<CTX> toDispatcher(final SimplePublish1<CTX> act)
	{
		return new Dispatcher<CTX>()
		{
			@Override
			public boolean dispatch(CTX ctx)
			{
				act.publish(ctx);
				return true;
			}
		};
	}

	public static <CTX> SimplePublish1<CTX> toDispatcherAll(final Dispatcher<CTX>... dispatchers)
	{
		return new SimplePublish1<CTX>()
		{
			@Override
			public void publish(CTX a)
			{
				for(Dispatcher<CTX> d:dispatchers)
				{
					d.dispatch(a);
				}
			}
		};
	}
}
