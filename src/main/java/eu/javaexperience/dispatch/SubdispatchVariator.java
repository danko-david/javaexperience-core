package eu.javaexperience.dispatch;

public interface SubdispatchVariator<CTX>
{
	public boolean addDispatcher(Dispatcher<CTX> disp);
}
