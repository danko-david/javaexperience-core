package eu.javaexperience.check;

import eu.javaexperience.generic.annotations.Ignore;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.CastTo;

public class ObjectCheckFunctions
{
	@Ignore
	public static GetBy1<Boolean, Object> isTypeOf(Class cls)
	{
		return new GetBy1<Boolean, Object>()
		{
			@Override
			public Boolean getBy(Object a)
			{
				if(null != a)
				{
					return cls.isAssignableFrom(a.getClass());
				}
				return false;
			}
		};
	}

	public static GetBy1<Boolean, Object> isNotNull()
	{
		return new GetBy1<Boolean, Object>()
		{
			@Override
			public Boolean getBy(Object a)
			{
				return null != a;
			}
		};
	}

	public static GetBy1<Boolean, Object> isNull()
	{
		return new GetBy1<Boolean, Object>()
		{
			@Override
			public Boolean getBy(Object a)
			{
				return null == a;
			}
		};
	}
	
	protected static GetBy1<Boolean, Object> canCastTo(CastTo ct)
	{
		return new GetBy1<Boolean, Object>()
		{
			@Override
			public Boolean getBy(Object a)
			{
				return null != ct.cast(a);
			}
		};
	}
	
	public static GetBy1<Boolean, Object> isBool()
	{
		return canCastTo(CastTo.Boolean);
	}
	
	public static GetBy1<Boolean, Object> isNumber()
	{
		return canCastTo(CastTo.Double);
	}
	
	public static GetBy1<Boolean, Object> isInteger()
	{
		return canCastTo(CastTo.Int);
	}
	
	public static GetBy1<Boolean, Object> isLong()
	{
		return canCastTo(CastTo.Long);
	}
	
	public static GetBy1<Boolean, Object> isFloat()
	{
		return canCastTo(CastTo.Float);
	}
	
	public static GetBy1<Boolean, Object> isDouble()
	{
		return canCastTo(CastTo.Double);
	}
	
	public static GetBy1<Boolean, Object> isString()
	{
		return canCastTo(CastTo.String);
	}
	
	public static GetBy1<Boolean, Object> isDate()
	{
		return canCastTo(CastTo.Date);
	}
}
