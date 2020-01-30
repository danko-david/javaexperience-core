package eu.javaexperience.interfaces.simple;

/**
 * TODO Refactor: remove this class and use Supplier<T> instead
 * */
public interface SimpleGet<T> //extends import java.util.function.Supplier<T>
{
	public T get();
}