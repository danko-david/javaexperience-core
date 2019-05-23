package eu.javaexperience.reflect;

public class PrimitiveTools
{
	public static String objectifyPrimitiveName(String name, String def)
	{
		switch(name)
		{
			case "void": return "Void";
			case "boolean": return "Boolean";
			case "byte": return "Byte";
			case "char": return "Character";
			case "short": return "Short";
			case "int": return "Integer";
			case "long": return "Long";
			case "float": return "Float";
			case "double": return "Double";
			
			default: return def;
		}
	}

	public static Class toObjectClassType(Class cls, Class def)
	{
		if(cls == boolean.class)
		{
			return Boolean.class;
		}
		else if(cls == byte.class)
		{
			return Byte.class;
		}
		else if(cls == char.class)
		{
			return Character.class;
		}
		else if(cls == short.class)
		{
			return Short.class;
		}
		else if(cls == int.class)
		{
			return Integer.class;
		}
		else if(cls == long.class)
		{
			return Long.class;
		}
		else if(cls == float.class)
		{
			return Float.class;
		}
		else if(cls == double.class)
		{
			return Double.class;
		}
		
		return def;
	}
	
	public static Class<?> translatePrimitiveToObjectType(Class<?> prim)
	{
		if(prim == int.class)
			return Integer.class;
		else if(prim == long.class)
			return Long.class;
		else if(prim == double.class)
			return Double.class;
		else if(prim == float.class)
			return Float.class;
		else if(prim == byte.class)
			return Byte.class;
		else if(prim == short.class)
			return Short.class;
		else if(prim == boolean.class)
			return Boolean.class;
		else if(prim == char.class)
			return Character.class;
		
		return prim;
	}

	public static boolean isPrimitiveTypeObject(Class<?> cls)
	{
		return 
				cls.equals(Boolean.class)||
				cls.equals(Character.class)||
				cls.equals(Byte.class)||
				cls.equals(Short.class)||
				cls.equals(Integer.class)||
				cls.equals(Float.class)||
				cls.equals(Long.class)||
				cls.equals(Double.class)
		;
	}
	
	
	public static boolean isPrimitiveClass(Class<? extends Object> cls)
	{
		return null != toObjectClassType(cls, null);
	}
	
	public static final Integer INT_ZERO = 0;
	public static final Integer INT_ONE = 1;
	public static final Integer INT_TWO = 2;
	public static final Integer INT_TEN = 3;
	
}
