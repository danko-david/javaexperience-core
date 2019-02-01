package eu.javaexperience.collection.enumerations;

import eu.javaexperience.asserts.AssertArgument;

public class SimpleEnumLike<E extends EnumLike<E>> implements EnumLike<E>
{
	protected int ordinal = -1;
	protected String name;
	protected EnumManager<E> mngr;
	
	public SimpleEnumLike(String name)
	{
		AssertArgument.assertNotNull(this.name = name, "enum value name");
	}
	
	@Override
	public boolean isRegistered()
	{
		return ordinal >= 0;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public int getOrdinal()
	{
		return ordinal;
	}

	@Override
	public EnumManager<E> getEnumManager()
	{
		return mngr;
	}

	public void assertNotRegistered()
	{
		AssertArgument.assertTrue(null == this.mngr, "Enum's manager may be set only once. "+name);
	}
	
	@Override
	public void setEnumManager(EnumManager<E> mngr)
	{
		assertNotRegistered();
		AssertArgument.assertNotNull(mngr, "enum manager");
		this.mngr = mngr;
	}

	@Override
	public void setOrdinal(int oridinal)
	{
		AssertArgument.assertTrue(this.ordinal == -1, "Ordinal may be set only once.");
		AssertArgument.assertTrue(oridinal >= 0, "Ordinal must be greater than or equals 0");
		this.ordinal = oridinal;
	}
}
