package eu.javaexperience.collection.search;

import java.util.ArrayList;
import java.util.Collection;

public class MatrixMap<E>
{
	protected final MatrixMapDistanceCalcualtor<E> calc;
	protected ArrayList<E> values = new ArrayList<>();
	
	protected final float[] matrix;
	
	public static MatrixMapDistanceCalcualtor findCalculator(Collection<?> coll)
	{
		for(Object c:coll)
		{
			if(c instanceof MatrixMapDistanceCalcualtor)
			{
				return (MatrixMapDistanceCalcualtor) c;
			}
		}
		return null;
	}
	
	public MatrixMap(Collection<E> coll)
	{
		values.addAll(coll);
		calc = findCalculator(values);
		if(null == calc)
		{
			throw new RuntimeException("No element in the collection implements MatrixMapDistanceCalcualtor.");
		}
		int len = values.size();
		matrix = new float[len*len];
	}
	
	public MatrixMap(Collection<E> coll, MatrixMapDistanceCalcualtor<E> calc)
	{
		values.addAll(coll);
		this.calc = calc;
		if(null == calc)
		{
			throw new RuntimeException("Calculator may not null");
		}
		int len = values.size();
		matrix = new float[len*len];
	}
	
	
	public void calculate()
	{
		int len = values.size();
		for(int i=0;i < len;++i)
		{
			for(int j=0;j<len;++j)
			{
				matrix[i*len+j] = calc.calculate(values.get(i), values.get(j));  
			}
		}
	}

	private String outOfBoundsMsg(int index)
	{
		return "Index: "+index+", Size: "+values.size();
	}
	
	protected int access(E elem)
	{
		int index = values.indexOf(elem);
		
		if(index < 0)
		{
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
		
		return index;
	}
	
	public float getAtoB(E a, E b)
	{
		int ia = access(a);
		int ib = access(b);
		
		return matrix[ia*values.size() + ib];
	}
}