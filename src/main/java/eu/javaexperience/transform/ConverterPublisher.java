package eu.javaexperience.transform;

import java.util.ArrayList;
import java.util.List;

import eu.javaexperience.collection.PublisherCollection;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public abstract class ConverterPublisher<From, To> extends PublisherCollection<From>
{
	protected ArrayList<To> dst = new ArrayList<>();
	
	public List<To> getDestination()
	{
		return dst;
	}
	
	protected boolean preFilter(From f)
	{
		return true;
	}
	
	protected boolean postFilter(From f, To t)
	{
		return true;
	}
	
	protected abstract To transform(From t);
	
	@Override
	public boolean add(From obj)
	{
		if(preFilter(obj))
		{
			To t = transform(obj);
			if(postFilter(obj, t))
			{
				dst.add(t);
				return true;
			}
		}
		
		return false;
	}
	
	
	public static <From, To> ConverterPublisher<From, To> createFromGetBy(final GetBy1<To, From> converter)
	{
		return new ConverterPublisher<From, To>()
		{
			@Override
			protected To transform(From t)
			{
				return converter.getBy(t);
			}
		};
	}
	
	public static <From, To> ConverterPublisher<From, To> createFromGetByWithPreFilter(final GetBy1<To, From> converter, final GetBy1<Boolean, From> preFilter)
	{
		return new ConverterPublisher<From, To>()
		{
			@Override
			protected boolean preFilter(From f)
			{
				return Boolean.TRUE == preFilter.getBy(f);
			}
			
			@Override
			protected To transform(From t)
			{
				return converter.getBy(t);
			}
		};
	}
}
