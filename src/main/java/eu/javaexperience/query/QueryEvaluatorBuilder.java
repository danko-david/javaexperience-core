package eu.javaexperience.query;

import java.util.Collection;
import java.util.regex.Pattern;

import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.interfaces.ObjectWithProperty;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.reflect.CastTo;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.PrimitiveTools;

public class QueryEvaluatorBuilder<T>
{
	public GetBy2<Object, T, String> extractor = getMirrorFieldExtractor();
	
	public static <T> GetBy2<Object, T, String> getMirrorFieldExtractor()
	{
		return Mirror::tryGetFieldValue;
	}
	
	public static <T> GetBy2<Object, T, String> getObjectWithPropertyExtractor()
	{
		return (a, b)->
		{
			if(a instanceof ObjectWithProperty)
			{
				return ((ObjectWithProperty)a).get(b);
			}
			
			return null;
		};
	}
	
	public GetBy2<Boolean, Object, Object> equals = Mirror::equals;
	
	protected static Boolean processPossibleCollections
	(
		Object expected,
		Object actual,
		boolean restrictToAll_or_consessiveToAll,
		GetBy2<Boolean, Object, Object> op
	)
	{
		if(null == actual || null == expected)
		{
			return null;
		}
		
		if(actual instanceof Collection)
		{
			Collection ca = (Collection) actual;
			if(expected instanceof Collection)
			{
				Collection ce = (Collection) expected;
				if(ca.isEmpty() && ce.isEmpty())
				{
					return false;
				}
				
				boolean hasPassing = false;
				for(Object a:ca)
				{
					for(Object e:ce)
					{
						Boolean eq = op.getBy(e, a);
						if(restrictToAll_or_consessiveToAll && Boolean.TRUE != eq)
						{
							return false;
						}
						
						if(Boolean.TRUE == eq)
						{
							if(!restrictToAll_or_consessiveToAll)
							{
								return true;
							}
							hasPassing = true;
						}
						else if(restrictToAll_or_consessiveToAll)
						{
							return false;
						}
					}
				}
				return hasPassing;
			}
			else
			{
				boolean hasPassing = false;
				for(Object a:ca)
				{
					Boolean eq = op.getBy(expected, a);
					if(restrictToAll_or_consessiveToAll && Boolean.TRUE != eq)
					{
						return false;
					}
					
					if(Boolean.TRUE == eq)
					{
						if(!restrictToAll_or_consessiveToAll)
						{
							return true;
						}
						hasPassing = true;
					}
					else if(restrictToAll_or_consessiveToAll)
					{
						return false;
					}
				}
				return hasPassing;
			}
		}
		else if(expected instanceof Collection)
		{
			//actual can only be a non collection object
			boolean hasPassing = false;
			Collection ce = (Collection) expected;
			for(Object e:ce)
			{
				Boolean eq = op.getBy(e, actual);
				if(restrictToAll_or_consessiveToAll && Boolean.TRUE != eq)
				{
					return false;
				}
				
				if(Boolean.TRUE == eq)
				{
					if(!restrictToAll_or_consessiveToAll)
					{
						return true;
					}
					hasPassing = true;
				}
				else if(restrictToAll_or_consessiveToAll)
				{
					return false;
				}
			}
			return hasPassing;
		}
		else
		{
			return op.getBy(expected, actual);
		}
	}
	
	public GetBy2<Boolean, Object, Object> contains = new GetBy2<Boolean, Object, Object>()
	{
		@Override
		public Boolean getBy(Object a, Object b)
		{
			return processPossibleCollections(a, b, true, (x, y)->x.toString().contains(y.toString()));
		}
	};
	
	public GetBy2<Integer, Object, Object> compare = new GetBy2<Integer, Object, Object>()
	{
		@Override
		public Integer getBy(Object a, Object b)
		{
			if(null == a || null == b)
			{
				return null;
			}
			
			if(a.getClass() != b.getClass())
			{
				if(a instanceof String)
				{
					b = b.toString();
				}
				else if(b instanceof Number)
				{
					b = CastTo.getCasterForTargetClass(a.getClass()).cast(b);
				}
			}
			
			if(a.getClass() == b.getClass())
			{
				if(a instanceof Comparable)
				{
					return ((Comparable) b).compareTo(a);
				}
			}
			
			return null;
		}
	};
	
	public GetBy2<Boolean, Object, Object> in = new GetBy2<Boolean, Object, Object>()
	{
		@Override
		public Boolean getBy(Object a, Object b)
		{
			return processPossibleCollections(a, b, false, Mirror::equals);
		}
	};
	
	public GetBy2<Boolean, Object, Object> match = new GetBy2<Boolean, Object, Object>()
	{
		@Override
		public Boolean getBy(Object a, Object b)
		{
			return processPossibleCollections
			(
				a,
				b,
				true,
				(x, y)->
				{
					Pattern p = null;
					
					if(a instanceof Pattern)
					{
						p = (Pattern) x;
					}
					else
					{
						p = Pattern.compile(x.toString());
					}
					
					return p.matcher(y.toString()).matches();
				}
			);
		}
	};
	
	//TO contains, matches, in, using object extractor function
	protected static Boolean tryCmp(boolean gt_lt, boolean eq, Integer val)
	{
		if(null == val)
		{
			return null;
		}
		
		if(eq && PrimitiveTools.INT_ZERO.equals(val))
		{
			return true;
		}
		
		if(gt_lt)
		{
			return val > 0;
		}
		else
		{
			return val < 0;
		}
	}
	
	public GetBy2<Boolean, T, AtomicCondition> build()
	{
		return new GetBy2<Boolean, T, AtomicCondition>()
		{
			@Override
			public Boolean getBy(T a, AtomicCondition b)
			{
				F op = b.getOperator();
				Object expected = b.getValue();
				Object actual = extractor.getBy(a, b.getFieldName());
				Boolean eval = null;
				switch(op)
				{
					case contains:
						eval = contains.getBy(expected, actual);
						break;
						
					case eq:
						eval = equals.getBy(expected, actual);
						break;
						
					case gt:
						eval = tryCmp(true, false, compare.getBy(expected, actual));
						break;
						
					case gte:
						eval = tryCmp(true, true, compare.getBy(expected, actual));
						break;
						
					case lt:
						eval = tryCmp(false, false, compare.getBy(expected, actual));
						break;
						
					case lte:
						eval = tryCmp(false, true, compare.getBy(expected, actual));
						break;
						
					case in:
						eval = in.getBy(expected, actual);
						break;
						
					case match:
						eval = match.getBy(expected, actual);
						break;
						
					default: throw new UnimplementedCaseException(op);
				}
				
				if(null == eval)
				{
					return false;
				}
				
				return b.isNegated() != eval;
			}
		};
	}
}
