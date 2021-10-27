package eu.javaexperience.classes;

import java.util.List;

import eu.javaexperience.collection.enumerations.EnumManager;

public interface ClassDescriptor
{
	public String getClassName();
	public List<? extends ClassFieldDescriptor> getAllField();
	
	public List<ClassDescriptor> getSuperTypes();
	
	public boolean isEnum();
	public boolean isInterface();
	public EnumManager getEnumManager();
	public boolean isArray();
	public ClassDescriptor getComponentType();
	public int getModifiers();
	
	public ClassSpace getClassSpace();
	public void setClassSpace(ClassSpace classSpace);
	public List<ClassAnnotationDescriptor> getAnnotations();
}
