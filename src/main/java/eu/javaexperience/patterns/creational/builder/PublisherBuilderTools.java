package eu.javaexperience.patterns.creational.builder;

public class PublisherBuilderTools
{
	public static <R, P, I> R buildAll
	(
		PublisherBuilder<P, R, I> builder,
		I init,
		Iterable<P> params
	)
	{
		builder.initialize(init);
		for(P p:params)
		{
			builder.publish(p);
		}
		
		return builder.getResult();
	}
	
	public static <R, P, I> R buildAll
	(
		PublisherBuilder<P, R, I> builder,
		I init,
		P... params
	)
	{
		builder.initialize(init);
		for(P p:params)
		{
			builder.publish(p);
		}
		
		return builder.getResult();
	}
}
