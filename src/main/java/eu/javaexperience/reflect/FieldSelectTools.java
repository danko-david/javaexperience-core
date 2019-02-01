package eu.javaexperience.reflect;

import eu.javaexperience.reflect.Mirror.BelongTo;
import eu.javaexperience.reflect.Mirror.FieldSelector;
import eu.javaexperience.reflect.Mirror.Select;
import eu.javaexperience.reflect.Mirror.Visibility;

public class FieldSelectTools
{
	public static final FieldSelector SELECT_ALL_INSTANCE_FIELD = new FieldSelector(true, Visibility.All, BelongTo.Instance, Select.All, Select.All, Select.All);
	public static final FieldSelector SELECT_ALL_PUBLIC_INSTANCE_FIELD = new FieldSelector(true, Visibility.Public, BelongTo.Instance, Select.All, Select.All, Select.All);
}
