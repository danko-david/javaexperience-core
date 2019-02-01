package eu.javaexperience.dispatch;

import eu.javaexperience.asserts.AssertArgument;
import eu.javaexperience.collection.list.RWArrayList;

public class RWListSubdispatchVariator<CTX> implements SubdispatchVariator<CTX>
{
	protected final RWArrayList<Dispatcher<CTX>> subject;
	
	public RWListSubdispatchVariator()
	{
		this.subject = new RWArrayList<>();
	}
	
	public RWListSubdispatchVariator(RWArrayList<Dispatcher<CTX>> subject)
	{
		AssertArgument.assertNotNull(this.subject = subject, "subject");
	}

	@Override
	public boolean addDispatcher(Dispatcher<CTX> disp)
	{
		return subject.add(disp);
	}
}
