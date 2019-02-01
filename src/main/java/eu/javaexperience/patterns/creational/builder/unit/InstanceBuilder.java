package eu.javaexperience.patterns.creational.builder.unit;

import java.util.EnumMap;
import java.util.Map.Entry;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class InstanceBuilder<F extends Enum<F> & BuildFields, I>
{
	protected final Class<F> fieldEnumClass;
	protected final EnumMap<F, Object> fieldData;
	protected final GetBy1<I, InstanceBuilder<F, I>> creator;
	
	public InstanceBuilder
	(
		Class<F> cls,
		GetBy1<I, InstanceBuilder<F, I>> creator
	)
	{
		fieldEnumClass = cls;
		fieldData = new EnumMap<>(cls);
		this.creator = creator;
	}
	
	public Class<F> getFieldEnumClass()
	{
		return fieldEnumClass;
	}
	
	public EnumMap<F, Object> getBuilderMap()
	{
		return fieldData;
	}
	
	public I build()
	{
		return creator.getBy(this);
	}
	
	public Object get(F field)
	{
		return fieldData.get(field);
	}
	
	public GetBy1<I, InstanceBuilder<F, I>> getCreator()
	{
		return creator;
	}
	
	public InstanceBuilder<F, I> clone()
	{
		InstanceBuilder<F, I> ret = new InstanceBuilder<F, I>(fieldEnumClass, creator);
		for(Entry<F, Object> kv:fieldData.entrySet())
		{
			ret.fieldData.put(kv.getKey(), kv.getValue());
		}
		
		return ret;
	}
	
	public InstanceBuilderFactory<F, I> createFactory()
	{
		return new InstanceBuilderFactory<>(clone());
	}	
	
	public InstanceBuilder<F, I> setField(F field, Object o)
	{
		fieldData.put(field, o);
		return this;
	}
	
	public InstanceBuilder<F, I> unsetField(F field)
	{
		fieldData.remove(field);
		return this;
	}
	
	public Object getField(F field)
	{
		return fieldData.get(field);
	}
}
