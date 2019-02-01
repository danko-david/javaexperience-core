package eu.javaexperience.reflect;

public class RuntimeClass extends ClassLoader
{
	public Class<?> loadClass(byte[] bytecode)
	{
		return defineClass(bytecode, 0, bytecode.length);
	}
}
