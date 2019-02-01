package eu.javaexperience.dispatch;

import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

public class DispatcherFunctions
{
	public static <CTX> SimplePublish1<CTX> wrap_dispatcher
	(
		Dispatcher<CTX>... disp
	)
	{
		return DispatcherTools.toDispatcherAll(disp);
	}
	
	/*public static <CTX> SimplePublish1<CTX> subRule
	(
		GetBy1<Boolean, CTX> checker,
		SimplePublish1<CTX> actor
	)
	{
		
	}*/
}
