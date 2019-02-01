package eu.javaexperience.reflect;

public interface ReflectInterface
{
	/**
	 * Megadott osztályú Objecktumot képes létrehozni a konstruktor hívása nélkül.
	 * Akár Enum-ot is tud példányosítani.
	 * */
	public <T> T allocObject(Class<T> cls) throws InstantiationException;
	
	/**
	 * Megpróbálja zárolni az Objektumhoz tartozó zárat.
	 * true ha sikerrel járt, false ha épp zárolva van. 
	 * */
	public boolean tryMonitorEnter(Object o);
	
	/**
	 * Az Objektum zárolását kezdeményezi, csak akkor tér vissza ha sikerült a
	 * zárat megszerezni.
	 * */
	public void monitorExit(Object o);
	
	/**
	 * Feloldja az Objektum zárját.
	 * */
	public void monitorEnter(Object obj);
}