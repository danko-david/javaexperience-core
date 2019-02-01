package eu.javaexperience.dispatch;

public interface Dispatcher<CTX>
{
	/**
	 * If you notice in the method that you can dispatch this request,
	 * set the pathPointer to the next element before pass another dispatcher  
	 * */
	public boolean dispatch(CTX ctx);
}
