package eu.javaexperience.data;

import eu.javaexperience.interfaces.ExternalDataAttached;
import eu.javaexperience.verify.EnvironmentDependValidator;

public interface DataPhenomenonInstance extends ExternalDataAttached
{
	public DataPhenomenon getPhenomenon();
	
	public String getFieldName();
	
	public boolean isRequired();
	
	public EnvironmentDependValidator<DataPhenomenonEnvironment, DataPhenomenonUnit, String> getValidator();

/*protected boolean required = false;
	
	//name in the form
	protected String name;
	
	//it may be a translation id
	protected String label;
	
	//it may be a translation id
	protected String placeholder;
	
	protected String helpText;
	
	protected List<String> classes;
	
	protected String value;
	
	protected List<Entry<String,String>> entries;
	*/
}
