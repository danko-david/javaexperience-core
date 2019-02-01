package eu.javaexperience.interfaces.simple.getBy;

public interface GetBy1<R, A>// extends java.util.function.Function<A, R>
{
	public R getBy(A a);
	
	/*public default R apply(A t)
	{
		return this.getBy(t);
	}*/
}
