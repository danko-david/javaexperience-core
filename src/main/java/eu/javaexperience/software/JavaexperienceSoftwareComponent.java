package eu.javaexperience.software;

public class JavaexperienceSoftwareComponent
{
	protected static SoftwareComponent ROOT = new SoftwareComponent();
	
	static
	{
		SoftwareComponent.getRoot().registerComponent("Javaexperience", ROOT);
	}
}
