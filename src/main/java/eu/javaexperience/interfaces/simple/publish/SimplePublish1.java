package eu.javaexperience.interfaces.simple.publish;

/**
 * TODO Refactor: drop this class and use java.util.function.Consumer<A> instead
 * */
public interface SimplePublish1<A> //extends java.util.function.Consumer<A>
{
	public void publish(A a);
	
	/*public default void accept(A a)
	{
		this.publish(a);
	}*/
}