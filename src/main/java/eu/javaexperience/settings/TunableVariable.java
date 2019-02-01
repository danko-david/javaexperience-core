package eu.javaexperience.settings;

import java.util.LinkedList;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;

public class TunableVariable<T>
{
	protected T innerValue;
	
	protected final GetBy1<T, Object> caster;
	protected final GetBy1<Boolean, T> valiadtor;
	
	private String description;
	protected String symbol;
	protected final T initialValue;

	public TunableVariable(GetBy1<T, Object> caster, GetBy1<Boolean, T> validator, T initialValue)
	{
		AssertArgument.assertNotNull(this.caster = caster, "caster");
		AssertArgument.assertNotNull(this.valiadtor = validator, "validator");
		this.initialValue = initialValue;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setValue(Object o)
	{
		//TODO later
		
		
	}
	
	public T getValue()
	{
		return innerValue;
	}

	public String getSymbol()
	{
		return symbol;
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}
	
	protected LinkedList<SimplePublish2<T, T>> modifyListeners =
			new LinkedList<>();
	
	
	public boolean addModifyListener(SimplePublish2<T, T> listener)
	{
		return modifyListeners.add(listener);
	}
	
	public boolean removeModifyListener(SimplePublish2<T, T> listener)
	{
		return modifyListeners.remove(listener);
	}
	
	public boolean isModifyListenerRegistered(SimplePublish2<T, T> listener)
	{
		return modifyListeners.contains(listener);
	}
	
}