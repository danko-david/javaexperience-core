package eu.javaexperience.collection;

import java.util.Collection;

public interface ImprovedCollection<E> extends Collection<E>
{
	/**
	 * A cél gyüjteményhez hozzáadja a gyüjteményben található elemeket
	 * és visszatér a darabszámmal (amennyit valóban hozzá tudott adni)
	 * (add true visszatérési értékek száma) 
	 * */
	public int copyAll(Collection<? super E> dst);
}
