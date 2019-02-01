package eu.javaexperience.patterns.behavioral.cor.link;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.javaexperience.collection.map.KeyVal;
import eu.javaexperience.dispatch.Dispatcher;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.Mirror.ClassData;

public class CorTools
{
	/*public static GetBy1<Boolean, Context> checkUrlRegexMatch(String regex)
	{
		final Pattern p = Pattern.compile(regex);
		return new GetBy1<Boolean, Context>()
		{
			@Override
			public Boolean getBy(Context a)
			{
				PreparedURL pu = a.getRequestUrl();
				String url = pu.getUrl();
				return p.matcher(url).matches();
			}
		};
	}
	
	public static CorChainLink<Context> rewriteRule(String match, String rewrite)
	{
		Pattern pn = Pattern.compile(match);
		return new CorChainLink<Context>()
		{
			@Override
			public boolean dispatch(Context ctx)
			{
				
				
				
				return false;
			}
		};
	}*/
	
	public static void main(String[] args)
	{
		Pattern p = Pattern.compile("http://localhost.hu(.*)");
		String url = "http://(.*)localhost.hu/page/article";
		
		Matcher m = p.matcher(url);
		if(!m.find())
		{
			throw new RuntimeException("Not match");
		}
		
		
		
	}
	
	public static <CTX> CorChainLink<CTX> wrapCheckerActor
	(
		final GetBy1<Boolean, CTX> checker,
		final SimplePublish1<CTX> actor
	)
	{
		return new CorChainLink<CTX>()
		{
			@Override
			public boolean dispatch(CTX ctx)
			{
				if(Boolean.TRUE == checker.getBy(ctx))
				{
					actor.publish(ctx);
					return true;
				}
				
				return false;
			}
		};
	}
	
	public static <CTX> CorChainLink<CTX> wrap
	(
		final GetBy1<Boolean, CTX> checker,
		final SimplePublish1<CTX> actor
	)
	{
		return new CorChainLink<CTX>()
		{
			@Override
			public boolean dispatch(CTX ctx)
			{
				if(Boolean.TRUE == checker.getBy(ctx))
				{
					actor.publish(ctx);
					return true;
				}
				
				return false;
			}
		};
	}
	
	public static <CTX> CorChainLink<CTX> exitChain
	(
		final GetBy1<Boolean, CTX> checker
	)
	{
		return new CorChainLink<CTX>()
		{
			@Override
			public boolean dispatch(CTX ctx)
			{
				if(Boolean.TRUE == checker.getBy(ctx))
				{
					this.ownerChain.exitChain();
					return true;
				}
				
				return false;
			}
		};
	}
	
	public static <CTX> CorChainLink<CTX> jumpChain
	(
		final GetBy1<Boolean, CTX> checker,
		final String chain_name
	)
	{
		return new CorChainLink<CTX>()
		{
			@Override
			public boolean dispatch(CTX ctx)
			{
				if(Boolean.TRUE == checker.getBy(ctx))
				{
					this.ownerChain.jumpToChain(ctx, chain_name);
					return true;
				}
				
				return false;
			}
		};
	}
	
	public static <CTX> CorChainLink<CTX> includeChain
	(
		final GetBy1<Boolean, CTX> checker,
		final String chain_name
	)
	{
		return new CorChainLink<CTX>()
		{
			@Override
			public boolean dispatch(CTX ctx)
			{
				if(Boolean.TRUE == checker.getBy(ctx))
				{
					return this.ownerChain.includeChain(ctx, chain_name);
				}
				
				return false;
			}
		};
	}
	
	public static KeyVal<String, Dispatcher>[] scanClassStaticMethodsForControllers
	(
		Class cls,
		GetBy1<String, Method> idExaminer,
		GetBy1<Dispatcher, Method> callWrapper
	)
	{
		ArrayList<KeyVal<String, Dispatcher>> ret = new ArrayList<>();
		
		ClassData cd = Mirror.getClassData(cls);
		for(Method m:cd.getAllMethods())
		{
			if(Mirror.isStatic(m) && Mirror.isPublic(m))
			{
				String id = idExaminer.getBy(m);
				
				if(null != id)
				{
					Dispatcher d = callWrapper.getBy(m);
					if(null != d)
					{
						ret.add(new KeyVal<String, Dispatcher>(id, d));
					}
				}
			}
		}
		
		return ret.toArray(KeyVal.emptyArrayInstance);
	}
	
	public static <C> GetBy1<Dispatcher<C>, Method> generateWrapperForStatic1ArgTypedFunction(final Class<C> paramAcceptType)
	{
		return new GetBy1<Dispatcher<C>, Method>()
		{
			@Override
			public Dispatcher<C> getBy(final Method m)
			{
				if(Mirror.isStatic(m) && Mirror.isPublic(m))
				{
					Parameter[] ps = m.getParameters();
					if(1 == ps.length && paramAcceptType.isAssignableFrom(ps[0].getType()))
					{
						return new Dispatcher<C>()
						{
							@Override
							public boolean dispatch(C ctx)
							{
								try
								{
									m.invoke(null, ctx);
								}
								catch(Exception e)
								{
									Mirror.propagateAnyway(e);
								}
								return true;
							}
						};
					}
				}
				return null;
			}
		};
	}
}
