package eu.javaexperience.multithread;

public class TaskDescriptor<T>
{
	public Job<T> exec;
	public T param;
	
	public TaskDescriptor()
	{}
	
	public TaskDescriptor(Job<T> job, T param)
	{
		this.exec = job;
		this.param = param;
	}
}
