package eu.javaexperience.time;

public interface TimeLineElementMerger<T extends TimeLineElement<T>>
{
	/**
	 * null ha nem lehet összeilleszteni a blokkokat, különben
	 * a kettő helyett beillesztendő blokkokkal tér vissza.
	 * */
	public T merge(T elem, T next);
}
