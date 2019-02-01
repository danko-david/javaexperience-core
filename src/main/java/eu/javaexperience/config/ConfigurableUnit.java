package eu.javaexperience.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.semantic.designedfor.Immutable;
import eu.javaexperience.semantic.references.MayNull;
import eu.javaexperience.verify.WeightedValidationReportEntry;

/**
 * A unit that can be configured by C kind of object and
 * provides U kind of object 
 * 
 * provides WeightedValidationReportEntry<V> type of validation entries.
 * */
public abstract class ConfigurableUnit<C, U, V>
{
	public enum ConfigurableUnitState
	{
		UNCONFIGURED_NEW_UNIT,
		VALID_CONFIGURATION_SET,
		INITIALIZED,
		
		BEFORE_UPGRADE,//aka need reinitalize
		
		DESTROYED
	}
	
	public ConfigurableUnit(String name)
	{
		this.name = name;
	}
	
	protected ConfigurableUnitState state = ConfigurableUnitState.UNCONFIGURED_NEW_UNIT;
	
	protected String name;
	
	protected C currentConfig;
	protected C desiredConfig;
	
	protected U unit;
	
	public synchronized ConfigurableUnitState getState()
	{
		return state;
	}
	
	public synchronized void setConfig(@Immutable C cfg)
	{
		checkStateAndSetValidatedConfig(cfg);
	}
	
	protected void checkStateAndSetValidatedConfig(C cfg)
	{
		if(state == ConfigurableUnitState.DESTROYED)
		{
			throw new RuntimeException("Can't set new configuration to "+this.toString()+" because it's already destroyed");
		}
		
		List<WeightedValidationReportEntry<V>> v = new ArrayList<>(); 
		if(!validateConfig(cfg, v))
		{
			throw new RuntimeException("New Configuration: `"+cfg+"` for: `"+toString()+"` is invalid. ValidationEntries: "+CollectionTools.toStringMultiline(v));
		}
		
		desiredConfig = cfg;
		if(state == ConfigurableUnitState.UNCONFIGURED_NEW_UNIT)
		{
			state = ConfigurableUnitState.VALID_CONFIGURATION_SET;
		}
		else
		{
			state = ConfigurableUnitState.BEFORE_UPGRADE;
		}
	}
	
	public abstract boolean validateConfig(C config, Collection<WeightedValidationReportEntry<V>> validationRemarks);
	
	public void assertHasConfig()
	{
		if(state != ConfigurableUnitState.VALID_CONFIGURATION_SET && state != ConfigurableUnitState.BEFORE_UPGRADE)
		{
			throw new RuntimeException(toString()+" has no valid configuration, can not be initalized.");
		}
	}
	
	public C getCurrentConfig()
	{
		return currentConfig;
	}
	
	public C getDesiredConfigfuration()
	{
		return desiredConfig;
	}
	
	public synchronized void initialize()
	{
		assertNotDestroyed();
		assertHasConfig();
		unit = internalInitalize(desiredConfig, unit);
		currentConfig = desiredConfig;
		desiredConfig = null;
		state = ConfigurableUnitState.INITIALIZED;
	}
	
	public synchronized void rollback()
	{
		if(ConfigurableUnitState.INITIALIZED == state)
		{
			throw new RuntimeException("Can't rollback unit: already initalized.");
		}
		
		assertNotDestroyed();
		assertHasConfig();
		desiredConfig = null;
		switch(state)
		{
		case UNCONFIGURED_NEW_UNIT:
		case DESTROYED:
		case INITIALIZED:
			throw new RuntimeException("Bad synchronization, ConfigurableUnit is in an illegal state `"+state+"` after state check in a synchronized block");
			
		case BEFORE_UPGRADE:
			state = ConfigurableUnitState.UNCONFIGURED_NEW_UNIT;
			break;
		
		case VALID_CONFIGURATION_SET:
			state = ConfigurableUnitState.INITIALIZED;
			break;
			
		default: throw new UnimplementedCaseException(state);
		}
	}
	
	protected abstract U internalInitalize(C cfg, @MayNull U prevUnit);
	
	public synchronized void destroy()
	{
		assertNotDestroyed();
		state = ConfigurableUnitState.DESTROYED;
		internalDestroy(unit);
		unit = null;
	}
	
	protected abstract void internalDestroy(U unit);
	
	public void assetUnitReady()
	{
		assertNotDestroyed();
		assertUnitInitalized();
	}
	
	public void assertNotDestroyed()
	{
		if(state == ConfigurableUnitState.DESTROYED)
		{
			throw new IllegalStateException(toString()+" already destroyed");
		}
	}
	
	public void assertUnitInitalized()
	{
		if(state == ConfigurableUnitState.VALID_CONFIGURATION_SET || state == ConfigurableUnitState.UNCONFIGURED_NEW_UNIT)
		{
			throw new IllegalStateException(toString()+" not yet initalized");
		}
	}
	
	public synchronized U getUnit()
	{
		assetUnitReady();
		return unit;
	}
	
	@Override
	public String toString()
	{
		return "ConfigurableUnit("+name+")";
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public static String formatName(Class cls, String userDefinedName)
	{
		return cls.getSimpleName()+"["+userDefinedName+"]";
	}
}
