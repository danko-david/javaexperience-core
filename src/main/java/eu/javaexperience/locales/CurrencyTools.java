package eu.javaexperience.locales;

import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.semantic.references.MayNotNull;

public class CurrencyTools
{
	public static Currency NULL_CURRENCY = new Currency()
	{
		@Override
		public String getIso4217Name()
		{
			return "NULL";
		}
	};
	
	protected static final Map<String, AvailableCurrency> WELL_KNOWN_CURRENCIES = new HashMap<>();
	static
	{
		for(AvailableCurrency ac: AvailableCurrency.values())
		{
			WELL_KNOWN_CURRENCIES.put(ac.getIso4217Name(), ac);
		}
	}
	
	public static @MayNotNull Currency getOrCreateCurrency(String name)
	{
		if(null == name)
		{
			return NULL_CURRENCY;
		}
		
		final String n = name.trim().toUpperCase();
		
		AvailableCurrency ret = WELL_KNOWN_CURRENCIES.get(n);
		if(null != ret)
		{
			return ret;
		}
		
		return new Currency()
		{
			@Override
			public String getIso4217Name()
			{
				return n;
			}
			
			@Override
			public boolean equals(Object obj)
			{
				return equalsByCurrencyIso4217(this, obj);
			}
			
			@Override
			public int hashCode()
			{
				return n.hashCode();
			}
		};
	}
	
	public static boolean equalsByCurrencyIso4217(Object o1, Object o2)
	{
		//shortcut for AvailableCurrency'es
		if(o1 == o2)
		{
			return true;
		}
		
		if(!(o1 instanceof Currency) || !(o2 instanceof Currency))
		{
			return false;
		}
		
		Currency c1 = (Currency) o1;
		Currency c2 = (Currency) o2;
		
		return Mirror.equals(c1.getIso4217Name(), c2.getIso4217Name());
	}
}
