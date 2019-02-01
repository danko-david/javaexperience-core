package eu.javaexperience.settings;

public interface ConfigValueChangeListener<T>
{
	public void configValueChanged(ConfigEntry<T> cfg, T new_value);
}