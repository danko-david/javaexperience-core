package eu.javaexperience.classes;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import eu.javaexperience.collection.enumerations.EnumLike;
import eu.javaexperience.collection.enumerations.EnumManager;
import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.interfaces.ObjectWithPropertyStorage;
import eu.javaexperience.text.StringTools;

public class ClassDescriptorTools
{
	public static void collectAllSuperClass(List<ClassDescriptor> sups, ClassDescriptor type)
	{
		sups.add(type);
		for(ClassDescriptor sup:type.getSuperTypes())
		{
			collectAllSuperClass(sups, sup);
		}
	}

	protected static ObjectWithPropertyStorage<ClassDescriptor> CLASS_PROPS = new ObjectWithPropertyStorage<>();
	
	protected static ObjectWithPropertyStorage<ClassFieldDescriptor> FIELD_PROPS = new ObjectWithPropertyStorage<>();
	
	static
	{
		CLASS_PROPS.addExaminer("name", (e)->StringTools.getSubstringAfterLastString(e.getClassName(), "."));
		CLASS_PROPS.addExaminer("fullName", (e)->e.getClassName());
		CLASS_PROPS.addExaminer("superClass", (e)->getSuperClass(e));
		CLASS_PROPS.addExaminer("superInterfaces", (e)->getSuperInterfaces(e));
		CLASS_PROPS.addExaminer("isArray", (e)->e.isArray());
		CLASS_PROPS.addExaminer("isEnum", (e)->e.isEnum());
		CLASS_PROPS.addExaminer("arrayComponent", (e)->e.getComponentType());
		CLASS_PROPS.addExaminer("enums", (e)->wrapEnums(e.getEnumManager()));
		CLASS_PROPS.addExaminer("isInterface", (e)->e.isInterface());
		CLASS_PROPS.addExaminer("isAbstract", (e)->Modifier.isAbstract(e.getModifiers()));
		CLASS_PROPS.addExaminer("modifiers", (e)->e.getModifiers());
		CLASS_PROPS.addExaminer("fields", (e)->e.getAllField());
		//TODO annontations
		
		
		FIELD_PROPS.addExaminer("name", (f)->f.getName());
		FIELD_PROPS.addExaminer("declaringClass", (f)->f.getOwnerModel().getClassName());
		FIELD_PROPS.addExaminer("type", (f)->f.getType().getClassName());
		FIELD_PROPS.addExaminer("modifiers", (f)->f.getModifiers());
		FIELD_PROPS.addExaminer("annotations", (f)->wrapAnnotations(f));
	}
	
	protected static Object wrapAnnotations(ClassFieldDescriptor f)
	{
		/*TODO if(f instanceof JavaClassField)
		{
			JavaClassField cfd = (JavaClassField) f;
			return null;
			//TODO ClassAnnotationDescriptor.wrap(cfd.getJavaField().getAnnotations());
		}
		*/
		return null;
	}
	
	protected static Object wrapEnums(EnumManager enumManager)
	{
		if(null == enumManager)
		{
			return null;
		}
		
		Object[] vs = enumManager.getValues();
		for(int i=0;i<vs.length;++i)
		{
			EnumLike el = (EnumLike) vs[i];
			SmallMap<String, Object> add = new SmallMap<>();
			add.put("oridnal", el.getOrdinal());
			add.put("name", el.getName());
			vs[i] = add;
		}
		
		return vs;
	}

	public static List<ClassDescriptor> getSuperInterfaces(ClassDescriptor cls)
	{
		List<ClassDescriptor> ret = new ArrayList<>();
		
		for(ClassDescriptor t:cls.getSuperTypes())
		{
			if(t.isInterface())
			{
				ret.add(t);
			}
		}
		
		return ret;
	}
	
	public static ClassDescriptor getSuperClass(ClassDescriptor cls)
	{
		for(ClassDescriptor t:cls.getSuperTypes())
		{
			if(!t.isInterface())
			{
				return t;
			}
		}
		return null;
	}

	public static String[] classPropKeys()
	{
		return CLASS_PROPS.keys();
	}

	public static Object classPropGet(ClassDescriptor cd, String key)
	{
		return CLASS_PROPS.get(cd, key);
	}
	
	public static String[] fieldPropKeys()
	{
		return FIELD_PROPS.keys();
	}

	public static Object fieldPropGet(ClassFieldDescriptor cfd, String key)
	{
		return FIELD_PROPS.get(cfd, key);
	}
}
