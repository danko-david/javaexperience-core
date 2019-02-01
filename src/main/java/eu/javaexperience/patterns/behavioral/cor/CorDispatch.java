package eu.javaexperience.patterns.behavioral.cor;

public class CorDispatch<CTX>
{
	protected CTX subject;
	protected int ttl = 128;
	
	
	public CTX getSubject()
	{
		return subject;
	}
	
	public int getTtl()
	{
		return ttl;
	}
	
	public int decrementTtl()
	{
		return --ttl;
	}
}