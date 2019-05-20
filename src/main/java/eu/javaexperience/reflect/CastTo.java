package eu.javaexperience.reflect;

import java.util.IdentityHashMap;
import java.util.Map;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.text.Format.strtotime;

/**
 * boolean
 * byte
 * char
 * short
 * int
 * long
 * float
 * double
 * 
 * String
 * 
 * Date
 * 
 * */
public enum CastTo implements NotatedCaster
{
	Boolean("boolean")
	{
		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Boolean)
				return o;
			
			if(o instanceof Number)
				return ((Number)o).longValue() != 0;
			
			String str = ((String)CastTo.String.cast(o)).toLowerCase();
			try
			{
				return java.lang.Double.parseDouble(str) != 0.0;
			}
			catch(Exception e){}
			
			boolean tr = str.contains("true");
			boolean fa = str.contains("false");

			if(tr != fa)
				return tr;
			
			if(str.length() < 2)
			{
				tr = str.contains("t");
				fa = str.contains("f");
			
				if(tr != fa)
					return tr;
			}
			
			return null;
		}
		
		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Boolean.class.isAssignableFrom(cls) || boolean.class.isAssignableFrom(cls);
		}

		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Boolean.class, boolean.class};
		}
	},
	
	Byte("byte")
	{
		private final Byte zero = 0;
		private final Byte one = 1;

		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Number)
				return ((Number) o).byteValue();

			if(o instanceof Character)
				return (byte) (char)o;
			
			if(o instanceof Boolean)
					return ((byte) (o.equals(java.lang.Boolean.TRUE)? one:zero));

			String str = o.toString().trim();
			try
			{
				return (byte) java.lang.Long.parseLong(str);
			}
			catch(Exception e){}
			
			try
			{
				return (byte) java.lang.Double.parseDouble(str);
			}
			catch(Exception e){}
			
			return null;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Byte.class.isAssignableFrom(cls) || byte.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Byte.class, byte.class};
		}
	},
	
	Char("char")
	{
		private final Character forTrue  = 't';
		private final Character forFalse = '\0';

		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Number)
				return (char)((Number) o).longValue();

			if(o instanceof Boolean)
					return ((o.equals(java.lang.Boolean.TRUE)? forTrue:forFalse));
			
			String str = o.toString();
			if(str.length() == 1)
				return (char)str.charAt(0);
			
			str = str.trim();
			
			if(str.length() == 1)
				return (char)str.charAt(0);

			try
			{
				return (char) java.lang.Long.parseLong(str);
			}
			catch(Exception e){}
			
			try
			{
				return (char) java.lang.Double.parseDouble(str);
			}
			catch(Exception e){}
			
			return null;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Character.class.isAssignableFrom(cls) || char.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Character.class, char.class};
		}
	},
	
	
	Short("short")
	{
		private final Short zero = 0;
		private final Short one = 1;

		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Number)
				return ((Number) o).shortValue();

			if(o instanceof Character)
				return (short) (char)o;
			
			if(o instanceof Boolean)
					return ((o.equals(java.lang.Boolean.TRUE)? one:zero));
			
			String str = o.toString();
			try
			{
				return (short) java.lang.Long.parseLong(str);
			}
			catch(Exception e){}
			
			try
			{
				return (short) java.lang.Double.parseDouble(str);
			}
			catch(Exception e){}
			
			return null;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Short.class.isAssignableFrom(cls) || short.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Short.class, short.class};
		}
	},
	
	Int("int")
	{
		private final Integer zero = 0;
		private final Integer one = 1;
		
		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Number)
				return ((Number) o).intValue();

			if(o instanceof Character)
				return (int) (char)o;
			
			if(o instanceof Boolean)
				return ((o.equals(java.lang.Boolean.TRUE)? one:zero));
			
			String str = ((String)CastTo.String.cast(o)).trim();
			try
			{
				return (int) java.lang.Long.parseLong(str);
			}
			catch(Exception e){}
			
			try
			{
				return (int) java.lang.Double.parseDouble(str);
			}
			catch(Exception e){}
			
			return null;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Integer.class.isAssignableFrom(cls) || int.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Integer.class, int.class};
		}
	},
	
	Long("long")
	{
		private final Long zero = 0l;
		private final Long one = 1l;
		
		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Number)
				return ((Number) o).longValue();

			if(o instanceof Character)
				return (long) (char)o;
			
			if(o instanceof Boolean)
				return ((o.equals(java.lang.Boolean.TRUE)? one:zero));
			
			String str = o.toString().trim();
			try
			{
				return java.lang.Long.parseLong(str);
			}
			catch(Exception e){}
			
			try
			{
				return (long) java.lang.Double.parseDouble(str);
			}
			catch(Exception e){}
			
			return null;
		}
		
		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Long.class.isAssignableFrom(cls) || long.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Long.class, long.class};
		}
	},
	
	Float("float")
	{
		private final Float zero = 0.0f;
		private final Float one = 1.0f;
		
		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Number)
				return ((Number) o).floatValue();

			if(o instanceof Character)
				return (float) (char)o;
			
			if(o instanceof Boolean)
				return ((o.equals(java.lang.Boolean.TRUE)? one:zero));
			
			String str = o.toString().trim();
			try
			{
				return java.lang.Float.parseFloat(str);
			}
			catch(Exception e){}
			
			return null;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Float.class.isAssignableFrom(cls) || float.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Float.class, float.class};
		}
	},
	
	Double("double")
	{
		private final Double zero = 0.0;
		private final Double one = 1.0;
		
		@Override
		public Object cast(Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof Number)
				return ((Number) o).doubleValue();

			if(o instanceof Character)
				return (double) (char)o;
			
			if(o instanceof Boolean)
				return ((o.equals(java.lang.Boolean.TRUE)? one:zero));

			String str = o.toString().trim();
			try
			{
				return java.lang.Double.parseDouble(str);
			}
			catch(Exception e){}
			
			try
			{
				return java.lang.Double.parseDouble(str.replace(',', '.'));
			}
			catch(Exception e){}
			
			return null;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return Double.class.isAssignableFrom(cls) || double.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{Double.class, double.class};
		}
	},
	
	String("String", "java.lang.String")
	{
		@Override
		public Object cast(Object o)
		{
			if(o == null)
			{
				return null;
			}
			
			if(o instanceof byte[])
			{
				return new String((byte[])o);
			}
			
			return o.toString();
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return String.class.isAssignableFrom(cls) || CharSequence.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{String.class, CharSequence.class, StringBuilder.class, StringBuffer.class};
		}
	},
	
	Date("Date", "java.util.Date")
	{
		@Override
		public java.lang.Object cast(java.lang.Object o)
		{
			if(null == o)
			{
				return null;
			}
			
			if(o instanceof java.util.Date)
			{
				return o;
			}
			
			String str = o.toString();
			
			java.util.Date d = strtotime.strtotime(str);
			if(null != d)
			{
				return d;
			}
			
			java.lang.Object time = Long.cast(str);
			
			if(time instanceof Number)
				return new java.util.Date(((Number) time).longValue());
			
			return null;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return java.util.Date.class.isAssignableFrom(cls);
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{java.util.Date.class, java.sql.Date.class};
		}
	},
	
	Object("Object", "java.lang.Object")
	{
		@Override
		public java.lang.Object cast(java.lang.Object o)
		{
			return o;
		}

		@Override
		public boolean canAssignForType(Class<?> cls)
		{
			return true;
		}
		
		@Override
		public Class[] getAssignTypes()
		{
			return new Class[]{};
		}
	}
	
	;
	
	private CastTo(String name)
	{
		shortName = fullQualname = name;
	}
	
	private CastTo(String shortName, String longName)
	{
		this.shortName = shortName;
		this.fullQualname = longName;
	}
	
	protected final String shortName;
	protected final String fullQualname;
	
	@Override
	public java.lang.String getTypeShortName()
	{
		return shortName;
	}
	
	@Override
	public java.lang.String getTypeFullQualifiedName()
	{
		return fullQualname;
	}
	
	/**
	 * Casts tobject to the specified type,
	 * returns null if Object can't casted to the
	 * target type. 
	 * */
	public abstract Object cast(Object o);
	
	protected final GetBy1<Object, Object> converter = new GetBy1<Object, Object>()
	{
		@Override
		public Object getBy(Object a)
		{
			return cast(a);
		}

	};
	
	public abstract Class[] getAssignTypes();
	
	public abstract boolean canAssignForType(Class<?> cls);
	
	private static final CastTo whitoutObject[] = new CastTo[values().length-1];
	
	static
	{
		int ep = 0;
		for(CastTo t:values())
			if(t != Object)
				whitoutObject[ep++] = t;
	}
	
	protected static Map<Class, CastTo> typeToCaster = new IdentityHashMap<>();
	
	static
	{
		for(CastTo t:whitoutObject)
		{
			for(Class c: t.getAssignTypes())
			{
				typeToCaster.put(c, t);
			}
		}
	}
	
	public static CastTo getCasterRestrictlyForTargetClass(Class<?> cls)
	{
		if(Object.class == cls)
		{
			return Object;
		}
		
		return typeToCaster.get(cls);
	}
	

	public static CastTo getCasterForTargetClass(Class<?> cls)
	{
		CastTo ret = typeToCaster.get(cls);
		if(null != ret)
		{
			return ret;
		}
		return Object;
	}
}