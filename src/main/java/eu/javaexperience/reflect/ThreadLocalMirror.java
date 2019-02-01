package eu.javaexperience.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ThreadLocalMirror
{
	private static final Method TLTLMgetEntry = Mirror.getClassMethodOrNull("java.lang.ThreadLocal$ThreadLocalMap","getEntry", ThreadLocal.class);
	private static final Field Thread_threadLocals = Mirror.getClassFieldOrNull(Thread.class, "threadLocals"); 
	private static final Field TLTLME = Mirror.getClassFieldOrNull("java.lang.ThreadLocal$ThreadLocalMap$Entry", "value");
	
	public static <T> T getOtherThreadsLocalVariable(ThreadLocal<T> threadLocal,Thread t) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		//System.out.println(TLTLMgetEntry+" "+Thread_threadLocals+" "+TLTLME);
		Object o = Thread_threadLocals.get(t);
		if(o == null)
			return null;
		o = TLTLMgetEntry.invoke(o, threadLocal);
		if(o == null)
			return null;

		return (T) TLTLME.get(o);
	}
	
}
