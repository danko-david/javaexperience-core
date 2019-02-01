package eu.javaexperience.patterns.behavioral.cor.link;

import eu.javaexperience.collection.list.RWArrayList;

import eu.javaexperience.dispatch.Dispatcher;
import eu.javaexperience.dispatch.DispatcherTools;
import eu.javaexperience.dispatch.RWListSubdispatchVariator;
import eu.javaexperience.dispatch.SubdispatchVariator;
import eu.javaexperience.dispatch.VariableSubDispatch;
import eu.javaexperience.patterns.behavioral.cor.link.CorChainLink;

public abstract class CorWrappedDispatch<CTX, T> extends CorChainLink<CTX> implements VariableSubDispatch<CTX>
{
	protected final RWArrayList<Dispatcher<CTX>> DISPATCH = new RWArrayList<>();
	
	protected final RWListSubdispatchVariator<CTX> vari = new RWListSubdispatchVariator<>(DISPATCH);
	
	protected abstract T doBefore(CTX ctx);
	
	protected abstract void doAfter(CTX ctx, T extraData);
	
	@Override
	public boolean dispatch(CTX ctx)
	{
		T elem = doBefore(ctx);
		
		try
		{
			return DispatcherTools.subDispatch(ctx, DISPATCH);
		}
		finally
		{
			doAfter(ctx, elem);
		}
	}

	@Override
	public SubdispatchVariator<CTX> getDispatchVariator()
	{
		return vari;
	}
}