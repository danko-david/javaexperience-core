package eu.javaexperience.collection.enumerations;

public interface EnumLike<E extends EnumLike<E>>
{
	public boolean isRegistered();
	public String getName();
	public int getOrdinal();
	public EnumManager<E> getEnumManager();
	
	public void setEnumManager(EnumManager<E> mngr);
	public void setOrdinal(int oridinal);
}
