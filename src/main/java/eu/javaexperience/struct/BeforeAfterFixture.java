package eu.javaexperience.struct;

public class BeforeAfterFixture<T>
{
	public final T before;
	public final T after;
	
	public BeforeAfterFixture(T before, T after)
	{
		this.before = before;
		this.after = after;
	}
}
