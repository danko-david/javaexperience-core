package eu.javaexperience.functional;

import eu.javaexperience.annotation.FunctionDescription;
import eu.javaexperience.annotation.FunctionVariableDescription;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class BoolFunctions
{
	protected static final GetBy1<Boolean, Object> NEVER = new GetBy1<Boolean, Object>()
	{
		@Override
		public Boolean getBy(Object a)
		{
			return false;
		}
	};

	@FunctionDescription
	(
		functionDescription = "Creates a function evaluates everything as false.",
		parameters = {},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static <T> GetBy1<Boolean, T> never()
	{
		return (GetBy1<Boolean, T>) NEVER;
	}

	protected static final GetBy1<Boolean, Object> ALWAYS = new GetBy1<Boolean, Object>()
	{
		@Override
		public Boolean getBy(Object a)
		{
			return true;
		}
	};
	
	@FunctionDescription
	(
		functionDescription = "Creates a function evaluates everything as true.",
		parameters = {},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static <T> GetBy1<Boolean, T> always()
	{
		return (GetBy1<Boolean, T>) ALWAYS;
	}
	
	@FunctionDescription
	(
		functionDescription = "Wraps the functions to one with AND logical relation.",
		parameters =
		{
			@FunctionVariableDescription(description = "Boolean returning functions.", mayNull = false, paramName = "functions", type=GetBy1[].class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static <T> GetBy1<Boolean, T> and(final GetBy1<Boolean, T>... funcs)
	{
		if(0 == funcs.length)
		{
			return never();
		}
		
		return new GetBy1<Boolean, T>()
		{
			@Override
			public Boolean getBy(T a)
			{
				for(GetBy1<Boolean, T> f : funcs)
				{
					if(Boolean.TRUE != f.getBy(a))
					{
						return false;
					}
				}
				return true;
			}
		};
	}
	
	@FunctionDescription
	(
		functionDescription = "Wraps the functions to one with OR logical relation.",
		parameters =
		{
			@FunctionVariableDescription(description = "Boolean returning functions.", mayNull = false, paramName = "functions", type=GetBy1[].class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static <T> GetBy1<Boolean, T> or(final  GetBy1<Boolean, T>... funcs)
	{
		if(0 == funcs.length)
		{
			return never();
		}
		
		return new GetBy1<Boolean, T>()
		{
			@Override
			public Boolean getBy(T a)
			{
				for(GetBy1<Boolean, T> f : funcs)
				{
					if(Boolean.TRUE == f.getBy(a))
					{
						return true;
					}
				}
				return false;
			}
		};
	}
	
	@FunctionDescription
	(
		functionDescription = "Negates the given function.",
		parameters =
		{
			@FunctionVariableDescription(description = "Boolean returning functions.", mayNull = false, paramName = "functions", type=GetBy1[].class),
		},
		returning = @FunctionVariableDescription(description="", mayNull=false, paramName="",type=GetBy1.class)
	)
	public static <T> GetBy1<Boolean, T> not(final GetBy1<Boolean, T> check)
	{
		return new GetBy1<Boolean, T>()
		{
			@Override
			public Boolean getBy(T a)
			{
				if(Boolean.TRUE != check.getBy(a))
				{
					return true;
				}
				
				return false;
			}
		};
	}
	
}
