package eu.javaexperience.interfaces.simple;

public interface WrapUnwrap<W,B>
{
	public W wrap(B b);
	public B unwrap(W w);
}