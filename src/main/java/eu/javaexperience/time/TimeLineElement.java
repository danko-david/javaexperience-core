package eu.javaexperience.time;

import java.util.Date;

import eu.javaexperience.semantic.designedfor.Immutable;
import eu.javaexperience.semantic.references.MayNotModified;
import eu.javaexperience.text.Format;

@Immutable
public class TimeLineElement<T>
{
	transient T prev;
	transient T next;
	transient Date dFrom;
	transient Date dTo;
	
	@MayNotModified
	protected long from;
	
	@MayNotModified
	protected long to;
	
	public TimeLineElement(long from,long to)
	{
		if(from > to)
			throw new IllegalArgumentException("The end date must be after the start date. from: "+
		Format.sqlTimestamp(new Date(from))+", to: "+Format.sqlTimestamp(new Date(to)));
		
		this.from = from;
		this.to = to;
	}
	
	public TimeLineElement(Date from,Date to)
	{
		this(from.getTime(),to.getTime());
		
		this.dFrom = from;
		this.dTo = to;
	}
	
	
	public T getPrev()
	{
		return prev;
	}
	
	public T getNext()
	{
		return next;
	}
	
	public final long duration()
	{
		return to-from;
	}
	
	public final boolean isIn(Date d)
	{
		return isIn(d.getTime());
	}
	
	public final boolean isIn(long d)
	{
		return from < d && d < to;
	}

	public final boolean isBTW(long from,long to)
	{
		return from <= this.from && this.to <= to;
	}
	
	public final boolean isBTW(Date from,Date to)
	{
		return isBTW(from.getTime(), to.getTime());
	}
	
	public final boolean hasCommonSectionWith(TimeLineElement<?> e)
	{
		return e.from < this.to && e.to > this.from;
	}
	
	public final boolean isAfter(long time)
	{
		return from >= time;
	}

	public final boolean isBefore(long time)
	{
		return to <= time;
	}

	public final boolean isAfter(TimeLineElement<?> time)
	{
		return isAfter(time.to);
	}

	public final boolean isBefore(TimeLineElement<?> time)
	{
		return isBefore(time.from);
	}
	
	public long getFrom()
	{
		return from;
	}
	
	public long getTo()
	{
		return to;
	}
	
	public Date getDateFrom()
	{
		if(dFrom == null)
		{
			dFrom = new Date(from);
		}
		return dFrom;
	}
	
	public Date getDateTo()
	{
		if(dTo == null)
		{
			dTo = new Date(to);
		}
		return dTo;
	}
	
	public String toString()
	{
		return "from: "+Format.sqlTimestamp(getDateFrom())+", to: "+Format.sqlTimestamp(getDateTo())+", dur: "+TimeCalc.durationToHourMin(duration());
	}

	public boolean isIntersects(long from, long to)
	{
		return from < this.to && to > this.from;
	}
	
	public boolean isIntersects(Date from, Date to)
	{
		return isIntersects(from.getTime(), to.getTime());
	}
}