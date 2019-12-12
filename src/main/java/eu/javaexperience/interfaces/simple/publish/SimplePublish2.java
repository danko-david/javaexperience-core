package eu.javaexperience.interfaces.simple.publish;

/**
 * TODO Refactor: drop this class and use java.util.function.BiConsumer<A, B> instead
 * */
public interface SimplePublish2<A,B>
{
	public void publish(A a, B b);
}