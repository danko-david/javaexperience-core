package eu.javaexperience.classes;

import java.util.List;

public interface ClassFieldDescriptor
{
	public ClassDescriptor getOwnerModel();
	
	//it's like annotatons but in ordered form.
	//public FieldExtraAttributes getExtraAttributes();
	
	public boolean isUserAccessible();
	
	public String getTranslationSymbol();

	public String getName();
	
	public ClassDescriptor getType();

	public int getModifiers();

	public List<ClassAnnotationDescriptor> getAnnotations();
}
