package eu.javaexperience.patterns.behavioral.cor;

import eu.javaexperience.collection.list.RWArrayList;
import eu.javaexperience.dispatch.Dispatcher;
import eu.javaexperience.dispatch.DispatcherTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.patterns.behavioral.cor.CorChain;
import eu.javaexperience.patterns.behavioral.cor.CorDispatcher;
import eu.javaexperience.patterns.behavioral.cor.link.CorChainLink;
import eu.javaexperience.patterns.behavioral.cor.link.RuleChainLink;

/**
 * Az URLNode is valahogy így kezdődött, végülis az is egyfajta dispatcher csak
 * más féle.
 * */
public class CorChain<CTX> implements Dispatcher<CTX>
{
	protected CorDispatcher<CTX> ownerUnit;
	
	protected final String chainName;

	protected Dispatcher<CTX> defaultAction;
	
	protected RWArrayList<CorChainLink<CTX>> links = new RWArrayList<>();
	
	public CorChain(String name)
	{
		this.chainName = name;
	}
	
	public void setDefaultAction(Dispatcher<CTX> act)
	{
		defaultAction = act;
	}
	
	public boolean addLink(CorChainLink<CTX> link)
	{
		if(null == link)
		{
			return false;
		}
		
		links.add(link);
		link.setOwner(this);
		return true;
	}
	
	public boolean addLinkFromClosures
	(
		GetBy1<Boolean, CTX> checker,
		SimplePublish1<CTX> actor
	)
	{
		return addLink(new RuleChainLink<>(checker, actor));
	}
	
	public boolean addSubDispatch
	(
		GetBy1<Boolean, CTX> checker,
		Dispatcher<CTX> actor
	)
	{
		return addLink(new RuleChainLink<>(checker, DispatcherTools.toSimplePublish(actor)));
	}
	
	@Override
	public boolean dispatch(CTX ctx)
	{
		if(!links.isEmpty())
		{
			try
			{
				for(CorChainLink<CTX> link:links)
				{
					if(link.dispatch(ctx))
					{
						return true;
					}
				}
			}
			catch(Error e)
			{
				if(EXIT_CHAIN_DISPATCHED_EXCEPTION == e)
				{
					//return with dispatched
					return true;
				}
				else if(EXIT_CHAIN_EXCEPTION == e)
				{
					//exiting chain
					return false;
				}
				else if(EXIT_CHAIN_DEFAULT_EXCEPTION == e)
				{
					//fall trough to default disaptch
				}
				else
				{
					throw e;
				}
			}
		}
		
		Dispatcher<CTX> def = defaultAction;
		if(null != def)
		{
			return def.dispatch(ctx);
		}
		
		return false;
	}
	
	protected static Error EXIT_CHAIN_EXCEPTION = new Error("Exit Chain Exception");
	
	protected static Error EXIT_CHAIN_DISPATCHED_EXCEPTION = new Error("Exit Chain Dispatched Exception");
	
	protected static Error EXIT_CHAIN_DEFAULT_EXCEPTION = new Error("Exit Chain Default Exception");
	
	public void exitChain()
	{
		throw EXIT_CHAIN_EXCEPTION;
	}

	public void exitChainDefault()
	{
		throw EXIT_CHAIN_DEFAULT_EXCEPTION;
	}
	
	public void exitChainSuccessfullyDispatched()
	{
		throw EXIT_CHAIN_DISPATCHED_EXCEPTION;
	}
	
	public boolean includeChain(CTX ctx, String chain)
	{
		CorDispatcher<CTX> d = ownerUnit; 
		if(null != d)
		{
			CorChain<CTX> cc = d.getChainByName(chain);
			if(null != cc)
			{
				cc.dispatch(ctx);
				return true;
			}
		}
		return false;
	}
	
	public void jumpToChain(CTX ctx, String chain)
	{
		CorDispatcher<CTX> d = ownerUnit; 
		if(null != d)
		{
			CorChain<CTX> cc = d.getChainByName(chain);
			if(null != cc)
			{
				if(cc.dispatch(ctx))
				{
					exitChainSuccessfullyDispatched();
				}
			}
			else
			{
				//log chain not found LoggingTools.tryLogFormat(LOG, level, format, params)
			}
		}
		else
		{
			//log dispatcher not set
			
		}
		exitChain();
	}
	
	public String getChainName()
	{
		return chainName;
	}

	public void setDefaultActionFromClosure(SimplePublish1<CTX> act)
	{
		defaultAction = DispatcherTools.toDispatcher(act);
	}

	public boolean addLinkAsFirst(CorChainLink<CTX> link)
	{
		if(null == link)
		{
			return false;
		}
		
		links.add(0, link);
		link.setOwner(this);
		return true;
	}
}