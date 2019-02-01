package eu.javaexperience.time;

import eu.javaexperience.semantic.references.ContainsNotNull;

/**
 * Szétdarabolja az adott időelem blokkot, az eredményeit tömbben visszadaja.
 * 
 * A {@link SimpleTimeLine} arra számít hogy a visszadott blokkok benne vannak
 * abban az időtartományban amit átadtunk és azok időben nem ütköznek.
 * 
 * A visszadaott blokkokkal ugyanúgy jár el mint a {@link SimpleTimeLine#add(TimeLineElement)}
 * ha ütközés van: nem adja hozzá, ha más időpontot adunk meg mint a bemenő adat hozzáadja
 * az idővonal abban az időpontban nincs ütközés. 
 * */
public interface TimeLineElementSplitter<T>
{
	public @ContainsNotNull T[] split(T elem);
}