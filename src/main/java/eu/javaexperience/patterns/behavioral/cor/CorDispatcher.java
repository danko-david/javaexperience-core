package eu.javaexperience.patterns.behavioral.cor;

import java.util.LinkedHashMap;
import java.util.Map;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.collection.map.RWLockMap;
import eu.javaexperience.dispatch.Dispatcher;
import eu.javaexperience.patterns.behavioral.cor.CorChain;

/**
 * chain of desponsibility dispatcher
 * */
public class CorDispatcher<CTX> implements Dispatcher<CTX>
{
	protected Map<String, CorChain<CTX>> chains = new RWLockMap<String, CorChain<CTX>>(new LinkedHashMap<String, CorChain<CTX>>());
	
	protected CorChain<CTX> root_chain;
	
	public void setRootChain(CorChain<CTX> root)
	{
		AssertArgument.assertNotNull(root, "root");
		this.root_chain = root;
	}
	
	public CorChain<CTX> addChain(CorChain<CTX> chain)
	{
		return chains.put(chain.getChainName(), chain);
	}
	
	public CorChain<CTX> getChainByName(String name)
	{
		return chains.get(name);
	}
	
	@Override
	public boolean dispatch(CTX ctx)
	{
		if(null == root_chain)
		{
			if(!chains.isEmpty())
			{
				for(CorChain<CTX> c:chains.values())
				{
					if(c.dispatch(ctx))
					{
						return true;
					}
				}
			}
			return false;
		}
		else
		{
			return root_chain.dispatch(ctx);
		}
	}
}