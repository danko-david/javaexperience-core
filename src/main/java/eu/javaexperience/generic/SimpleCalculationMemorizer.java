package eu.javaexperience.generic;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.semantic.references.ContainsNotNull;

public class SimpleCalculationMemorizer<R,A>
{
	protected final GetBy1<R, A> calculator;
	
	public SimpleCalculationMemorizer(@ContainsNotNull GetBy1<R, A> calculator)
	{
		AssertArgument.assertNotNull(calculator, "calculator");
		this.calculator = calculator;
	}

	protected final ConcurrentHashMap<A, R> results = new ConcurrentHashMap<>();
	protected final Map<A, R> ro =  Collections.unmodifiableMap(results);
	
	public R calcuate(A arg)
	{
		AssertArgument.assertNotNull(arg, "calcualtion argument");
		R ret = results.get(arg);
		if(null == ret)
		{
			results.putIfAbsent(arg, ret = calculator.getBy(arg));
		}
		return ret;
	}
	
	public void reset()
	{
		results.clear();
	}
	
	/**
	 * Returns the calculated values as a bound unmodifyable map.
	 * */
	public Map<A, R> getCalculatedValues()
	{
		return ro;
	}
}
