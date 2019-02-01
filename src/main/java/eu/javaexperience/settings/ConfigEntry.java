package eu.javaexperience.settings;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.io.FileConfigMapper;
import eu.javaexperience.parse.ParsePrimitive;

public abstract class ConfigEntry<T>
{
	FileConfigMapper source;
	
	protected final String key;
	
	protected T defaultValue;
	
	public ConfigEntry(FileConfigMapper owner, String key, T default_value)
	{
		this.source = owner;
		this.key = key;
		this.defaultValue = default_value;
	}
	
	protected ArrayList<ConfigValueChangeListener<T>> listeners = new ArrayList<>();
	
	protected abstract String format(T value);
	protected abstract T parse(String value);
	
	public T getValue()
	{
		String v = source.get(key);
		if(null == v)
		{
			return defaultValue;
		}
		
		T ret = parse(v);
		if(null == ret)
		{
			return defaultValue;
		}
		
		return ret;
	}
	
	public void addListener(ConfigValueChangeListener<T> lst)
	{
		if(null != lst)
		{
			CollectionTools.addLikeSet(listeners, lst);
		}
	}
	
	public boolean isListenerRegistered(ConfigValueChangeListener<T> lst)
	{
		return listeners.contains(lst);
	}
	
	public void removeListener(ConfigValueChangeListener<T> lst)
	{
		listeners.remove(lst);
	}
	
	public void setValue(T obj)
	{
		if(null == obj)
		{
			return;
		}
		
		String save = format(obj);
		if(null == save)
		{
			return;
		}
		
		source.put(key, save);
		
		for(ConfigValueChangeListener<T> c:listeners)
		{
			try
			{
				c.configValueChanged(this, obj);
			}
			catch(Throwable t)
			{
				if(null != t)
				{
					t.printStackTrace();
				}
			}
		}
	}
	
	public static class WaitForReloadListener<T> implements ConfigValueChangeListener<T>
	{
		protected final ConfigEntry<T> entry;
		protected final AtomicReference<Semaphore> waiter  = new AtomicReference<>(new Semaphore(0));
		public WaitForReloadListener(ConfigEntry<T> entry)
		{
			this.entry = entry;
			entry.addListener(this);
		}
		
		@Override
		public void configValueChanged(ConfigEntry<T> cfg, T new_value)
		{
			Semaphore s = waiter.get();
			s.release(Integer.MAX_VALUE/2);
			waiter.set(new Semaphore(0));
		}
		
		public void waitForReload() throws InterruptedException
		{
			Semaphore s = waiter.get();
			s.acquire();
			s.release();
		}
	}
	
	public static class StringConfigEntry extends ConfigEntry<String>
	{
		public StringConfigEntry(FileConfigMapper owner, String key, String default_value)
		{
			super(owner, key, default_value);
		}

		@Override
		protected String format(String value)
		{
			return value;
		}
		
		@Override
		protected String parse(String value)
		{
			return value;
		}
		
	}
	
	public static class TrimmedStringConfigEntry extends ConfigEntry<String>
	{
		public TrimmedStringConfigEntry(FileConfigMapper owner, String key, String default_value)
		{
			super(owner, key, default_value);
		}

		@Override
		protected String format(String value)
		{
			return value.trim();
		}
		
		@Override
		protected String parse(String value)
		{
			return value.trim();
		}
	}
	
	public static class IntegerConfigEntry extends ConfigEntry<Integer>
	{
		public IntegerConfigEntry(FileConfigMapper owner, String key, Integer default_value)
		{
			super(owner, key, default_value);
		}

		@Override
		protected String format(Integer value)
		{
			return value.toString();
		}

		@Override
		protected Integer parse(String value)
		{
			return ParsePrimitive.tryParseInt(value);
		}
	}
}