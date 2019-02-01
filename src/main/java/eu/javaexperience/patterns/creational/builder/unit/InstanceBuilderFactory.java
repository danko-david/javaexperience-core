package eu.javaexperience.patterns.creational.builder.unit;

public class InstanceBuilderFactory<F extends Enum<F> & BuildFields, I>
{
	protected InstanceBuilder<F, I> goldenBuilder;
	
	public InstanceBuilderFactory(InstanceBuilder<F, I> clone)
	{
		this.goldenBuilder = clone;
	}
	
	public InstanceBuilder<F, I> newBuilder()
	{
		return goldenBuilder.clone();
	}
}
