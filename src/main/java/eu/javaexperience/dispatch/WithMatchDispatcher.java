package eu.javaexperience.dispatch;

public abstract class WithMatchDispatcher<CTX> implements Dispatcher<CTX>, VariableSubDispatch<CTX>
{
	protected final RWListSubdispatchVariator<CTX> subdispatch = new RWListSubdispatchVariator<>();

	public abstract boolean isMatch(CTX ctx);
	
	@Override
	public boolean dispatch(CTX ctx)
	{
		if(!isMatch(ctx))
		{
			return false;
		}
		
		return DispatcherTools.subDispatch(ctx, subdispatch.subject);
	}

	@Override
	public SubdispatchVariator<CTX> getDispatchVariator() 
	{
		return subdispatch;
	}
}
