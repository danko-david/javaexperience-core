package eu.javaexperience.io;

import java.io.Flushable;

public interface AutoFlushListener<T extends Flushable>
{
	public void beforeUserFlush(T f);
	public void afterUserFlush(T f);

	
	public void beforeBufferFlush(T f);
	public void afterBufferFlush(T f);

}
