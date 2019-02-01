package eu.javaexperience.query;

import java.util.HashSet;
import java.util.Map;

import eu.javaexperience.reflect.CastTo;

public class AtomicCondition// implements ObjectLike
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final F operator;
	private final boolean negate;
	private final String field;
	private final Object value;
	
	public AtomicCondition(F operator,boolean negate,String field,Object val)
	{
		this.operator = operator;
		this.negate = negate;
		this.field = field;
		this.value = val;
	}

	public F getOperator()
	{
		return operator;
	}

	public boolean isNegated()
	{
		return negate;
	}

	public String getFieldName()
	{
		return field;
	}

	public Object getValue()
	{
		return value;
	}
	
	@Override
	public String toString()
	{
		return field+" "+(negate?"!":"")+operator.name()+" "+value;
	}
	
	protected static final HashSet<String> fields = new HashSet<>();
	static
	{
		fields.add("o");
		fields.add("n");
		fields.add("f");
		fields.add("v");
	}
	
	public static AtomicCondition parse(Map<String, Object> ac)
	{
		F op = F.valueOf((String)ac.get("o"));
		boolean neg = (boolean) CastTo.Boolean.cast(ac.get("n"));
		String f = (String) ac.get("f");
		Object val = ac.get("v");
		return new AtomicCondition(op, neg, f, val);
	}
	
	public void write(Map<String, Object> to)
	{
		to.put("o", operator.name());
		to.put("n", negate);
		to.put("f", field);
		to.put("v", value);
	}

	
	/*public static AtomicCondition parse(DataObject ac)
	{
		F op = F.valueOf(ac.getString("o"));
		boolean neg = ac.getBoolean("n");
		String f = ac.getString("f");
		Object val = ac.get("v");
		if(val instanceof DataArray)
		{
			val = ((DataArray)val).asJavaArray();
		}
		
		return new AtomicCondition(op, neg, f, val);
	}
	
	@Override
	public Object get(String key)
	{
		if(null == key)
		{
			return null;
		}
		
		switch (key)
		{
			case "o":	return operator.name();
			case "n":	return negate;
			case "f":	return field;
			case "v":	return value;
			default: return null;
		}
	}

	@Override
	public DataReprezType getDataReprezType()
	{
		return DataReprezType.OBJECT;
	}

	@Override
	public boolean has(String key)
	{
		return fields.contains(key);
	}

	@Override
	public String[] keys()
	{
		return fields.toArray(Mirror.emptyStringArray);
	}

	@Override
	public int size()
	{
		return fields.size();
	}*/
}