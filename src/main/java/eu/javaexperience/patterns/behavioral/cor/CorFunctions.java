package eu.javaexperience.patterns.behavioral.cor;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.patterns.behavioral.cor.CorChain;
import eu.javaexperience.patterns.behavioral.cor.CorDispatcher;
import eu.javaexperience.patterns.behavioral.cor.link.CorChainLink;
import eu.javaexperience.patterns.behavioral.cor.link.CorTools;
import eu.javaexperience.annotation.FunctionDescription;
import eu.javaexperience.annotation.FunctionVariableDescription;
import eu.javaexperience.dispatch.Dispatcher;
import eu.javaexperience.dispatch.DispatcherTools;

public class CorFunctions
{
	@FunctionDescription
	(
		functionDescription = "Szabálytábla létrehozása",
		parameters =
		{
			@FunctionVariableDescription(description = "Szabályláncok", mayNull = false, paramName = "ruleChains", type = Object.class),
		},
		returning = @FunctionVariableDescription(description="Szabálytábla", mayNull=false, paramName="", type=Object.class) 
	)
	public static <CTX> CorDispatcher<CTX> create_cor_table
	(
		CorChain<CTX>... chains
	)
	{
		CorDispatcher<CTX> ret = new CorDispatcher<>();
		for(CorChain<CTX> c:chains)
		{
			ret.addChain(c);
		}
		return ret;
	}
	
	@FunctionDescription
	(
		functionDescription = "Szabályánc létrehozása",
		parameters =
		{
			@FunctionVariableDescription(description = "Szabálylánc neve", mayNull = false, paramName = "chainName", type = Object.class),
			@FunctionVariableDescription(description = "Alapértelmezett művelet", mayNull = false, paramName = "defaultAction", type = Object.class),
			@FunctionVariableDescription(description = "Szabályok", mayNull = false, paramName = "rules", type = Object.class),
		},
		returning = @FunctionVariableDescription(description="Szabálylánc", mayNull=false, paramName="", type=Object.class) 
	)
	public static <CTX> CorChain<CTX> create_cor_chain
	(
		String chain_name,
		SimplePublish1<CTX> defaultAction,
		CorChainLink<CTX>... links
	)
	{
		CorChain<CTX> cc = new CorChain<>(chain_name);
		if(null != defaultAction)
		{
			cc.setDefaultAction(DispatcherTools.toDispatcher(defaultAction));
		}
		
		for(CorChainLink<CTX> c:links)
		{
			cc.addLink(c);
		}
		
		return cc;
	}
	
	public static <CTX> CorChainLink<CTX> create_cor_link
	(
		GetBy1<Boolean, CTX> checker,
		SimplePublish1<CTX> actor
	)
	{
		return CorTools.wrapCheckerActor(checker, actor);
	}
	
	public static <CTX> CorChainLink<CTX> create_cor_dispatch
	(
		GetBy1<Boolean, CTX> checker,
		Dispatcher<CTX> actor
	)
	{
		return CorTools.wrapCheckerActor(checker, DispatcherTools.toSimplePublish(actor));
	}
	
	public static <CTX> CorChainLink<CTX> create_cor_link_jump_chain
	(
		GetBy1<Boolean, CTX> checker,
		String chain_name
	)
	{
		return CorTools.jumpChain(checker, chain_name);
	}
	
	public static <CTX> CorChainLink<CTX> create_cor_link_include_chain
	(
		GetBy1<Boolean, CTX> checker,
		String chain_name
	)
	{
		return CorTools.includeChain(checker, chain_name);
	}
}
