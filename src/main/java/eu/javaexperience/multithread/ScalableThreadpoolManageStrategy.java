package eu.javaexperience.multithread;

public interface ScalableThreadpoolManageStrategy
{
	public void initialize(ScalableThreadpool<?> pool);
	public void manageLoad(ScalableThreadpool<?> pool);
}
