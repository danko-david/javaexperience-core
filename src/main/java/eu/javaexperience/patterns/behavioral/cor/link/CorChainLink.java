package eu.javaexperience.patterns.behavioral.cor.link;

import eu.javaexperience.dispatch.Dispatcher;
import eu.javaexperience.patterns.behavioral.cor.CorChain;
;

public abstract class CorChainLink<CTX> implements Dispatcher<CTX> 
{
	public static final CorChainLink[] emptyCorChainLinkArray = new CorChainLink[0];
	
	protected CorChain<CTX> ownerChain;
	
	public void setOwner(CorChain<CTX> owner)
	{
		ownerChain = owner;
	}
}
