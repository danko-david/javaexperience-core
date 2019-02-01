package eu.javaexperience.multithread;

/**
 * Interface used for executing cyclic the same Job whit different argument.
 * 
 * If you implement this interface be sure it is thread safe.
 * It is easy to achieve if you don't create variables in the implementation
 * i.e. you create a {@link Stateless} object.
 * 
 * The goal is to T provides all the required data to complete the requested Task.
 * */
public interface Job<T>
{
	public void exec(T param) throws Throwable;
}