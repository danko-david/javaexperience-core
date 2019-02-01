package eu.javaexperience.reflect;

public interface BidirectionalCaster<F,T>
{
	public T cast(F from);
	public F castReverse(T from);
}
