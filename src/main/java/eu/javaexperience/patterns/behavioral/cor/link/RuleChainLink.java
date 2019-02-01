package eu.javaexperience.patterns.behavioral.cor.link;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
;

public class RuleChainLink<CTX> extends CorChainLink<CTX>
{
	public RuleChainLink(GetBy1<Boolean, CTX> check, SimplePublish1<CTX> action)
	{
		AssertArgument.assertNotNull(this.check = check, "checker");
		this.action = action;
	}

	protected GetBy1<Boolean, CTX> check;
	protected SimplePublish1<CTX> action;
	
	@Override
	public boolean dispatch(CTX ctx)
	{
		if(Boolean.TRUE == check.getBy(ctx))
		{
			if(null != action)
			{
				action.publish(ctx);
			}
			return true;
		}
		
		return false;
	}
}
