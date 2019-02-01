package eu.javaexperience.generic;

import java.util.concurrent.ConcurrentHashMap;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.semantic.references.ContainsNotNull;

public class SimpleCalculationMemorizer<R,A>
{
	private final GetBy1<R, A> calcualtor;
	
	public SimpleCalculationMemorizer(@ContainsNotNull GetBy1<R, A> calculator)
	{
		AssertArgument.assertNotNull(calculator, "calculator");
		this.calcualtor = calculator;
	}

	//majd ha ráérek megcsinálom hogy egyszerre csak egy argumentum végrehajtása follyon, másik osztályban
//	private final Object waitFor = new Object(); 
	
	private final ConcurrentHashMap<A, R> results = new ConcurrentHashMap<>();
	
	public R calcuate(A arg)
	{
		R ret = results.get(arg);
		if(ret == null)
			results.putIfAbsent(arg, ret = calcualtor.getBy(arg));
		
		return ret;
	}
}
