package eu.javaexperience.time;

public interface TimeLineElementUnifier<T>
{
	public boolean mayUnify(T elem,T next);
}