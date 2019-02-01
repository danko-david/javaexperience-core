package eu.javaexperience.multithread;

public interface BlockingJob<T> extends Job<T>
{
	public T acceptJob() throws Throwable;
}
