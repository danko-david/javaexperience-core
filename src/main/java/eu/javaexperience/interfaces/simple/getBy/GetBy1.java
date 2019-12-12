package eu.javaexperience.interfaces.simple.getBy;

/**
 * TODO Refactor: drop this class and use java.util.function.Function<A, R> instead
 * */
public interface GetBy1<R, A>// extends java.util.function.Function<A, R>
{
	public R getBy(A a);
	
	/*public default R apply(A t)
	{
		return this.getBy(t);
	}*/
}
