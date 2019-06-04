package eu.javaexperience.classes;

import java.util.List;

public interface ClassSpace
{
	public List<ClassDescriptor> getLoadedClasses();
	public ClassDescriptor getClassByName(String name);
	public boolean registerClass(String name, ClassDescriptor classDescriptor);
	public boolean removeClass(String name);
}
