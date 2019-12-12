package eu.javaexperience.interfaces.simple.getBy;

/**
 * TODO Refactor: drop this class and use java.util.function.BiFunction<A, B, R> instead
 * */
public interface GetBy2<R,A,B> //extends java.util.function.BiFunction<A, B, R>
{
	public R getBy(A a,B b);
	
	/*public default R apply(A a, B b)
	{
		return this.getBy(a, b);
	}*/
}
