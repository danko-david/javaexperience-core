package eu.javaexperience.reflect;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.collection.map.MapTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.interfaces.simple.getBy.GetBy2;
import eu.javaexperience.io.IOTools;
import eu.javaexperience.log.JavaExperienceLoggingFacility;
import eu.javaexperience.log.LogLevel;
import eu.javaexperience.log.Loggable;
import eu.javaexperience.log.Logger;
import eu.javaexperience.log.LoggingTools;
import eu.javaexperience.semantic.references.MayNotNull;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;

public class Mirror
{
	protected static class MirrorLogger
	{
		//initialize as last, beacuse it can cause initialization loop error
		protected static final Logger LOG = JavaExperienceLoggingFacility.getLogger(new Loggable("Mirror"));
	}
	
	private Mirror(){}
	
	public static final Object undefined = new Object();
	
	public static <T> T tryCastOrNull(Object o, Class<T> cls)
	{
		if(o == null)
			return null;
		if(cls.isAssignableFrom(o.getClass()))
			return cls.cast(o);
		
		return null;
	}
	
	public static Object getObjectFieldOrNull(Object o, String field)
	{
		try
		{
			return getClassFieldOrNull(o.getClass(),field).get(o);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static Object getObjectFieldByClassOrNull(Class cls,Object o, String field)
	{
		try
		{
			return getClassFieldOrNull(cls,field).get(o);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static Object getObjectFieldByClassOrNull(String cls,Object o, String field)
	{
		try
		{
			return getClassFieldOrNull(cls,field).get(o);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Visszatért az osztály elérhetővé tett metódusával 
	 * 
	 * */
	public static Method getClassMethodOrNull(String cls,String method,Class<?>... types)
	{
		try
		{
			Method ret = Class.forName(cls).getDeclaredMethod(method, types);
			setAccessible(ret);
			return ret;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Visszatért az osztály elérhetővé tett mezőjével
	 * 
	 * */
	public static Field getClassFieldOrNull(String cls,String field)
	{
		try
		{
			Field ret = Class.forName(cls).getDeclaredField(field);
			setAccessible(ret);
			return ret;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Visszatért az osztály elérhetővé tett metódusával 
	 * 
	 * */
	public static Method getClassMethodOrNull(Class<?> cls,String method,Class<?>... types)
	{
		try
		{
			Method ret = cls.getDeclaredMethod(method, types);
			setAccessible(ret);
			return ret;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Visszatért az osztály elérhetővé tett metódusával 
	 * 
	 * */
	public static Method getClassUniqueNameMethodOrNull(Class<?> cls,String method)
	{
		try
		{
			Method[] mets = cls.getDeclaredMethods();
			for(Method m:mets)
				if(method.equals(m.getName()))
				{
					setAccessible(m);
					return m;
				}
		}
		catch(Exception e)
		{
		}
		return null;
	}
	
	/**
	 * Visszatért az osztály elérhetővé tett mezőjével
	 * 
	 * */
	public static Field getClassFieldOrNull(Class<?> cls,String field)
	{
		try
		{
			ClassData cd = getClassData(cls);
			for(Field f:cd.getAllFields())
			{
				if(field.equals(f.getName()))
				{
					setAccessible(f);
					return f;
				}
			}
		}
		catch(Exception e)
		{
			LoggingTools.tryLogFormat(MirrorLogger.LOG, LogLevel.DEBUG, "Exception while collecting ClassData about class `%s`", cls);
		}
		return null;
	}
	
	
	/**
	 * A PHP-ban az egyik legcsodásabb dolog amit ismerek a var_dump. Struktúráltan kiírja az Objektum mezőit és annak értéketi.
	 * Mikor még csak ismerkedtem a Java-val azt hittem alap hogy van ilyen a nyelvben, sajnos nem.
	 * De mostmár van! :D
	 * @param ps ahova írjuk az eredményt
	 * @param o objektum amit ki szeretnénk iratni
	 * @param c szint elválasztó
	 * @throws IOException 
	 * */
	public static void var_dump_throw(Appendable ps,Object o,String c) throws IllegalArgumentException, IllegalAccessException, IOException
	{
		var_dump(ps,o,1,c,new ArrayList<Object>());
	}
	
	public static void var_dump(Appendable ps,Object o,String c)
	{
		try
		{
			var_dump_throw(ps, o, c);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	
	public static ClassData getClassData(Class<?> cls)
	{
		if(cls == null)
			return null;
		
		ClassData ret = LazyClassDataInit.data.get(cls);
	
		if(ret == null)
		{
			ret = new ClassData(cls);
			LazyClassDataInit.data.put(cls, ret);
		}
	
		return ret;
	}
	
	public static ClassData getClassData(String scls)
	{
		Class<?> cls = null;
		try
		{
			cls = Class.forName(scls);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		if(cls == null)
			return null;
		
		ClassData ret = LazyClassDataInit.data.get(cls);
	
		if(ret == null)
		{
			ret = new ClassData(cls);
			LazyClassDataInit.data.put(cls, ret);
		}
	
		return ret;
	}
	
	protected static class LazyClassDataInit
	{
		protected static ConcurrentMap<Class<?>, ClassData> data = new ConcurrentHashMap<>();
	}
	public static char[] emptyCharArray = new char[0];
	
	
	
	public static enum Visibility
	{
		All,
		Default,
		Public,
		Protected,
		Private,
	}
	
	public static enum BelongTo
	{
		Any,
		Static,
		Instance,
	}
	
	public static enum Select
	{
		All,
		Is,
		IsNot
	}
	
	////PUBLIC | PROTECTED | PRIVATE | 		ABSTRACT | STATIC | FINAL | SYNCHRONIZED | NATIVE | STRICT;
	public static final class MethodSelector
	{
		final boolean all;			//1
		final Visibility vis;		//3
		final BelongTo belong;		//2
		final Select _abstract;		//2
		final Select _final;		//2
		final Select _synchronized;	//2
		final Select _native;		//2
		final Select _strict;		//2
		
		public MethodSelector(boolean all_not_just_self,Visibility visibility,BelongTo belong,Select _abstract,Select _final,Select _synchronized,Select _native, Select _strict)
		{
			all = all_not_just_self;
			vis = visibility;
			this.belong = belong;
			this._abstract = _abstract;
			this._final = _final;
			this._synchronized = _synchronized;
			this._native = _native;
			this._strict = _strict;
		}
		
		@Override
		public int hashCode()
		{
			int ret = 0;
			ret &= all?1:0;							// 0
			ret &= vis.ordinal() >> 1;				// 1-4
			ret &= belong.ordinal() >> 4;			// 5-6
			ret &= _abstract.ordinal() >> 6;		// 7-8
			ret &= _final.ordinal() >> 8;			// 9-10
			ret &= _synchronized.ordinal() >> 10;	// 11-12
			ret &= _native.ordinal() >> 12;			// 13-14
			ret &= _strict.ordinal() >> 14;			// 15-16
			
			return ret;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(this == o)
				return true;
			
			if(!(o instanceof MethodSelector))
				return false;
			
			MethodSelector dat = (MethodSelector) o;
			
			return
						all == dat.all
					&&
						vis == dat.vis
					&&
						belong == dat.belong
					&&
						_abstract == dat._abstract
					&&
						_final == dat._final
					&&
						_synchronized == dat._synchronized
					&&
						_native == dat._native
					&&
						_strict == dat._strict
			;
		}
	}
	
	//PUBLIC | PROTECTED | PRIVATE | STATIC | FINAL | TRANSIENT | VOLATILE;
	public static final class FieldSelector
	{
		final boolean all;
		final Visibility vis;
		final BelongTo belong;
		final Select _final;
		final Select _transient;
		final Select _volatile;
		
		public FieldSelector(boolean all_not_just_self,Visibility visibility,BelongTo belong,Select _final,Select _transient,Select _volatile)
		{
			all = all_not_just_self;
			vis = visibility;
			this.belong = belong;
			this._final = _final;
			this._transient = _transient;
			this._volatile = _volatile;
		}
		
		@Override
		public int hashCode()
		{
			int ret = 0;
			ret &= all?1:0;							// 0
			ret &= vis.ordinal() >> 1;				// 1-4
			ret &= belong.ordinal() >> 4;			// 5-6
			ret &= _transient.ordinal() >> 6;		// 7-8
			ret &= _final.ordinal() >> 8;			// 9-10
			ret &= _volatile.ordinal() >> 10;	// 11-12
			return ret;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(this == o)
				return true;
			
			if(!(o instanceof FieldSelector))
				return false;
			
			FieldSelector dat = (FieldSelector) o;
			
			return
						all == dat.all
					&&
						vis == dat.vis
					&&
						belong == dat.belong
					&&
						_transient == dat._transient
					&&
						_final == dat._final
					&&
						_volatile == dat._volatile
			;
		}
	}
	
	/**
	 * Osztályok adatinak összegyüjtése
	 * */
	public static class ClassData
	{
		final Class<?> cls;
		final Method[] self_methods;
		final Method[] all_methods;
		
		final Field[] self_fields;
		final Field[] all_fields;
		
		final Class<?>[] direct_interfaces;
		final Class<?>[] all_interfaces;
	
		final Class<?> direct_superclass;
		final Class<?>[] all_superclass;
		
		private ClassData(@MayNotNull Class<?> cls)
		{
			this.cls = cls;
			
			self_methods = cls.getDeclaredMethods();
			self_fields = cls.getDeclaredFields();
			
			direct_interfaces = cls.getInterfaces();
			direct_superclass = cls.getSuperclass();
			
			Class<?>[] interf = direct_interfaces;
			
			if(direct_superclass != null)
			{
				ClassData dat = getClassData(direct_superclass);
				all_methods = ArrayTools.arrayConcat(self_methods, dat.all_methods);
				all_fields = ArrayTools.arrayConcat(self_fields, dat.all_fields);
				for(Class<?> in:direct_interfaces)
					interf = ArrayTools.arrayConcat(interf, getClassData(in).all_interfaces);

				all_superclass = ArrayTools.arrayAppend(direct_superclass, dat.all_superclass);
			}
			else//ez csak az Object-nél lesz igaz
			{
				all_methods = self_methods;
				all_fields = self_fields;
				all_superclass = emptyClassArray;
			}
			
			for(Field f:self_fields)
				setAccessible(f);
			
			for(Method m:self_methods)
				setAccessible(m);
			
			all_interfaces = interf;
		}
		
		public Class<?> getSubjectClass()
		{
			return cls;
		}
		
		public Method[] getSelfMethods()
		{
			return ArrayTools.copy(self_methods);
		}
		
		public Method[] getAllMethods()
		{
			return ArrayTools.copy(all_methods);
		}
		
		public Field[] getSelfFields()
		{
			return ArrayTools.copy(self_fields);
		}
		
		public Field[] getAllFields()
		{
			return ArrayTools.copy(all_fields);
		}
		
		ConcurrentMap<MethodSelector, Method[]> metSel = new ConcurrentHashMap<>();
		ConcurrentMap<FieldSelector, Field[]> fieldSel = new ConcurrentHashMap<>();
		
		//PUBLIC | PROTECTED | PRIVATE | 		ABSTRACT | STATIC | FINAL | SYNCHRONIZED | NATIVE | STRICT;
		public Method[] selectMethods(MethodSelector sel)
		{
			Method[] ret = metSel.get(sel);
			
			if(ret == null)
			{
				ret = select(sel);
				metSel.put(sel, ret);
			}
			
			return ret;
		}
	
		//PUBLIC | PROTECTED | PRIVATE | STATIC | FINAL | TRANSIENT | VOLATILE;
		public Field[] selectFields(FieldSelector sel)
		{
			Field[] ret = fieldSel.get(sel);
			
			if(ret == null)
			{
				ret = select(sel);
				fieldSel.put(sel, ret);
			}
			
			return ret;
		}
	
		
		public Method[] select(MethodSelector sel)
		{
			ArrayList<Method> met = new ArrayList<>();
			
			Method[] ms = sel.all?all_methods:self_methods;

			for(Method m:ms)
			{
				int mod = m.getModifiers();
				switch (sel.vis)
				{
				case Default:
					if(Modifier.isPublic(mod) || Modifier.isProtected(mod) || Modifier.isPrivate(mod))
						continue;

					break;
				case Private:
					if(!Modifier.isPrivate(mod))
						continue;
					
					break;
				case Protected:
					if(!Modifier.isProtected(mod))
						continue;
					
					break;
				case Public:
					if(!Modifier.isPublic(mod))
						continue;
					
					break;
				default:
				case All:
					break;
				}
				

				switch (sel.belong)
				{
				case Instance:
					if(Modifier.isStatic(mod))
						continue;
					
					break;
				case Static:
					if(!Modifier.isStatic(mod))
						continue;
					
					break;
				default:
				case Any:
					break;
				}
					
				if(acceptByMatch(Modifier.ABSTRACT, sel._abstract, mod))
					continue;
				
				if(acceptByMatch(Modifier.FINAL, sel._final, mod))
					continue;

				if(acceptByMatch(Modifier.SYNCHRONIZED, sel._synchronized, mod))
					continue;

				if(acceptByMatch(Modifier.NATIVE, sel._native, mod))
					continue;
				
				if(acceptByMatch(Modifier.STRICT, sel._strict, mod))
					continue;

				met.add(m);
			}
			
			return met.toArray(emptyMethodArray);
		}
		
		public static boolean acceptByMatch(int modifier,Select sel,int value)
		{
			switch (sel)
			{
			case Is:
				return (modifier & value) != modifier;
				
			case IsNot:
				return (modifier & value) != 0;

			default:
			case All:
				return false;
			}
		}
		
		public Field[] select(FieldSelector sel)
		{
			ArrayList<Field> met = new ArrayList<>();
			
			Field[] ms = sel.all?all_fields:self_fields;

			for(Field f:ms)
			{
				int mod = f.getModifiers();
				switch (sel.vis)
				{
				case Default:
					if(Modifier.isPublic(mod) || Modifier.isProtected(mod) || Modifier.isPrivate(mod))
						continue;

					break;
				case Private:
					if(!Modifier.isPrivate(mod))
						continue;
					
					break;
				case Protected:
					if(!Modifier.isProtected(mod))
						continue;
					
					break;
				case Public:
					if(!Modifier.isPublic(mod))
						continue;
					
					break;
				default:
				case All:
					break;
				}
				

				switch (sel.belong)
				{
				case Instance:
					if(Modifier.isStatic(mod))
						continue;
					
					break;
				case Static:
					if(!Modifier.isStatic(mod))
						continue;
					
					break;
				default:
				case Any:
					break;
				}
					
				if(acceptByMatch(Modifier.TRANSIENT, sel._transient, mod))
					continue;
				
				if(acceptByMatch(Modifier.FINAL, sel._final, mod))
					continue;

				if(acceptByMatch(Modifier.VOLATILE, sel._volatile, mod))
					continue;

				met.add(f);
			}
			
			return met.toArray(emptyFieldArray);
		}

		public Field getFieldByName(String k)
		{
			for(Field f:getAllFields())
			{
				if(f.getName().equals(k))
				{
					return f;
				}
			}
			return null;
		}
		
		public void addClassesAndIntefaces(Collection<Class> dst)
		{
			dst.add(cls);
			CollectionTools.inlineAdd(dst, all_superclass);
			CollectionTools.inlineAdd(dst, all_interfaces);
		}

		public Class[] getAllSuperClassAndInterfaces()
		{
			List<Class> ret = new ArrayList<>();
			addClassesAndIntefaces(ret);
			return ret.toArray(emptyClassArray);
		}
	}
	
	public static void fillObjectPublicFieldIntoMap(Object o,Map<String,Object> map) throws IllegalArgumentException, IllegalAccessException
	{
		ClassData dat = getClassData(o.getClass());
		Field[] fs = dat.select(new FieldSelector(true, Visibility.Public, BelongTo.Instance, Select.All, Select.All, Select.All));
		for(Field f:fs)
			map.put(f.getName(), f.get(o));
	}
	
	public static void fillMapIntoObject(Map<String,Object> map, Object o) throws IllegalArgumentException, IllegalAccessException
	{
		ClassData dat = getClassData(o.getClass());
		Field[] fs = dat.select(new FieldSelector(true, Visibility.All, BelongTo.Instance, Select.All, Select.All, Select.All));
		for(Field f:fs)
			if(map.containsKey(f.getName()))
				try
				{
					f.set(o, map.get(f.getName()));
				}
				catch (Exception e)
				{
					if(MirrorLogger.LOG.mayLog(LogLevel.DEBUG))
					{
						LoggingTools.tryLogFormat(MirrorLogger.LOG, LogLevel.DEBUG, "Exception while invoking fillMapIntoObject(`%s`, `%s`)", map, o);
					}
				}
	}
	
	public static void tryFillMapIntoObjectCast(Map<String,Object> map, Object o)
	{
		ClassData dat = getClassData(o.getClass());
		Field[] fs = dat.select(FieldSelectTools.SELECT_ALL_INSTANCE_FIELD);
		for(Field f:fs)
		{
			if(map.containsKey(f.getName()))
			{
				CastTo cast = CastTo.getCasterRestrictlyForTargetClass(f.getType());
				if(null != cast)
				{
					Object set = map.get(f.getName());
					set = cast.cast(set);
					try
					{
						f.set(o, set);
					}
					catch (Exception e)
					{
						if(MirrorLogger.LOG.mayLog(LogLevel.DEBUG))
						{
							LoggingTools.tryLogFormat(MirrorLogger.LOG, LogLevel.DEBUG, "Exception while invoking tryFillMapIntoObjectCast(`%s`, `%s`)", map, o);
						}
					}
				}
			}
		}
	}
	
	public static Number castNumberTo(Number n,Class<? extends Number> cls)
	{
		cls = (Class<Number>) PrimitiveTools.translatePrimitiveToObjectType(cls);
		if(cls == Integer.class)
			return n.intValue();
		else if(cls == Long.class)
			return n.longValue();
		else if(cls == Double.class)
			return n.doubleValue();
		else if(cls == Float.class)
			return n.floatValue();
		else if(cls == Byte.class)
			return n.byteValue();
		else if(cls == Short.class)
			return n.shortValue();
		return n;
	}
	
	private static void var_dump(Appendable ps,Object obj,int lvl,String c,ArrayList<Object> disa) throws IllegalArgumentException, IllegalAccessException, IOException
	{
		if(obj == null)
		{
			ps.append("null,\n");
			return;
		}
		
		if(disa.contains(obj))
		{
			ps.append("@[");
			ps.append(Integer.toHexString(obj.hashCode()));
			ps.append("]\n");
			return;
		}
		
		disa.add(obj);
		
		String ls = StringTools.repeatString(c, lvl-1);
		Class<?> cls = obj.getClass();
		if(cls.isArray())
		{
			ps.append("<");
			printClassType(ps, cls);
			ps.append("> ");
			ps.append(Integer.toHexString(System.identityHashCode(obj)));
			ps.append("\n");
			ps.append(ls);
			ps.append("{\n");
			
			for(int i=0;i<Array.getLength(obj);i++)
			{
				ps.append(ls);
				ps.append(c);
				ps.append("[");
				ps.append(String.valueOf(i));
				ps.append("] : ");
				var_dump(ps, Array.get(obj, i), lvl+1,c,disa);
			}
			
			ps.append(ls);
			ps.append("},\n");
		}
		else if(PrimitiveTools.isPrimitiveTypeObject(cls)) //primitiv vagy String
		{
			ps.append("<");
			ps.append(cls.getName());
			ps.append(">");
			ps.append(" : ");
			ps.append(obj.toString());
			ps.append(",\n");
		}
		else if(cls.equals(String.class))
		{
			ps.append("<");
			ps.append(cls.getName());
			ps.append(">");
			ps.append(" : ");
			ps.append("\"");
			ps.append(obj.toString());
			ps.append("\"");
			ps.append(",\n");
		}
		else //sima objektum
		{
			//collectfileds ne csak ezen osztályét
			Field[] fields = collectClassFields(cls, false);
			
			ps.append("<");
			printClassType(ps,cls);
			ps.append("> ");
			ps.append(Integer.toHexString(System.identityHashCode(obj)));
			ps.append(":\n");
			ps.append(ls);
			ps.append("{\n");
			
			for(Field f:fields)
			{
				if(Modifier.isStatic(f.getModifiers()))
					continue;
				
				ps.append(ls);
				ps.append(c);
				ps.append(f.getName());
				ps.append("(");
				printClassType(ps,f.getType());
				ps.append(")");
				ps.append(" : ");
				
				f.setAccessible(true);
				
				Object val = f.get(obj);
				var_dump(ps, val, lvl+1,c,disa);
			}
			
			ps.append(ls);
			ps.append("},\n");
		}
	}
	
	public static final Class<?>[] emptyClassArray = new Class[0];
	
	public static Field[] collectClassFields(Class<?> cls,boolean publicOnly)
	{
		ArrayList<Field> l = new ArrayList<>();
		collectClassFields(cls,l,publicOnly);
		return l.toArray(emptyFieldArray); 
	}
	
	public static void collectClassFields(Class<?> cls,ArrayList<Field> ret,boolean publicOnly)
	{
		if(cls.isInterface() || cls == Object.class)
		{
			/**
			 * TeaVm: function jl_Class_getDeclaredFields($this)
			 * dies here:
			 * $this.$declaredFields = $rt_createArray(jlr_Field, $jsFields.length);
			 * when we try to discover an interface.
			 */
			return;
		}
		
		for(Field f:publicOnly?cls.getFields():cls.getDeclaredFields())
		{
			ret.add(f);
		}
		
		Class<?> sup = cls.getSuperclass();
		if(sup != null)
		{
			collectClassFields(sup, ret, publicOnly);
		}
		
		/*for(Class<?> c:cls.getInterfaces())
		{
			if(c != null)
			{
				collectClassFields(c, ret, publicOnly);
			}
		}*/
	}
	
	public static Method[] collectClassMethods(Class<?> cls,boolean publicOnly)
	{
		ArrayList<Method> l = new ArrayList<>();
		collectClassMethods(cls,l,publicOnly);
		return l.toArray(emptyMethodArray); 
	}
	
	public static void collectClassMethods(Class<?> cls,ArrayList<Method> ret,boolean publicOnly)
	{
		for(Method f:publicOnly?cls.getMethods():cls.getDeclaredMethods())
			ret.add(f);
		
		Class<?> sup = cls.getSuperclass();
		if(sup != null)
			collectClassMethods(sup, ret, publicOnly);
		
		for(Class<?> c:cls.getInterfaces())
			if(c != null)
				collectClassMethods(c, ret, publicOnly);
	}
	
	public static Class<?> getFinalComponentClass(Class<?> cls)
	{
		if(cls.isArray())
			return getFinalComponentClass(cls.getComponentType());
		return cls;
	}
	
	public static int determineArrayDeep(Class<?> c)
	{
		return determineArrayDeep(c, 0);
	}
	
	private static int determineArrayDeep(Class<?> c,int lvl)
	{
		if(!c.isArray())
			return lvl;
		
		return determineArrayDeep(c.getComponentType(),lvl+1);
	}
	
	
	public static void printClassType(Appendable ps,Class<?> cls) throws IOException
	{
		int deep = determineArrayDeep(cls);
		Class<?> comp = getFinalComponentClass(cls);
		ps.append(comp.getName());
		for(int i=0;i<deep;i++)
			ps.append("[]");
	}
	
	public static void appendClassType(StringBuilder ps,Class<?> cls)
	{
		int deep = determineArrayDeep(cls);
		Class<?> comp = getFinalComponentClass(cls);
		ps.append(comp.getCanonicalName());
		for(int i=0;i<deep;i++)
			ps.append("[]");
	}
	
	public static String getClassTypeString(Class<?> cls)
	{
		StringBuilder sb = new StringBuilder();
		appendClassType(sb, cls);
		return sb.toString();
	}
	
	public static Field setAccessible(Field f)
	{
		f.setAccessible(true);
		return f;
	}
	
	public static Method setAccessible(Method f)
	{
		f.setAccessible(true);
		return f;
	}

	public static Object newInstanceOrNull(Class<?> cls)
	{
		try
		{
			return cls.newInstance();
		}
		catch (Exception e)
		{}
		
		return null;
	}

	public static Object invokeClassMethod(Class<?> cls, String method, Object This)
	{
		Method m = getClassMethodOrNull(cls, method);
		if(m == null)
			return null;
		try
		{
			return m.invoke(This);
		}
		catch(Exception e)
		{}
		return null;
	}
	
	public static Object invokeClassMethod(Object This, String method)
	{
		Method m = getClassMethodOrNull(This.getClass(), method);
		if(m == null)
			return null;
		try
		{
			return m.invoke(This);
		}
		catch(Exception e)
		{}
		return null;
	}
	
	public static Object invokeClassMethod(String scls, Object This, String method)
	{
		try
		{
			Class<?> cls = getClassData(scls).cls;
			
			Method m = getClassMethodOrNull(cls, method);
			
			if(m == null)
				return null;

			return m.invoke(This);
		}
		catch(Exception e)
		{e.printStackTrace();}
		return null;
	}
	
	
	/**
	 * Perfect method to determine full qualified class name from byte code.
	 * 
	 * http://stackoverflow.com/questions/1649674/resolve-class-name-from-bytecode
	 * */
	public static String getClassName(InputStream is) throws Exception
	{
		DataInputStream dis = new DataInputStream(is);
		dis.readLong(); // skip header and class version
		int cpcnt = (dis.readShort()&0xffff)-1;
		int[] classes = new int[cpcnt];
		String[] strings = new String[cpcnt];
		for(int i=0; i<cpcnt; i++) {
			int t = dis.read();
			if(t==7) classes[i] = dis.readShort()&0xffff;
			else if(t==1) strings[i] = dis.readUTF();
			else if(t==5 || t==6) { dis.readLong(); i++; }
			else if(t==8) dis.readShort();
			else dis.readInt();
		}
		dis.readShort(); // skip access flags
	    return strings[classes[(dis.readShort()&0xffff)-1]-1].replace('/', '.');
	}
	
	/**
	 * if @param o is an array first element will returned
	 * otherwise the original value
	 * */
	public static Object tryUnpack(Object o)
	{
		if(null == o)//note obj instanceof Object[] is false e.g. for int[]
			return null;
		
		if(o.getClass().isArray())
			if(Array.getLength(o) > 0)
				return Array.get(o, 0);
		
		return o;
	}
	
	public static boolean in(int val, int... vals)
	{
		for(int i=0;i<vals.length;++i)
		{
			if(val == vals[i])
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean in(@MayNotNull Object val, @MayNotNull Object... vals)
	{
		for(int i=0;i<vals.length;++i)
		{
			if(val.equals(vals[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	@Deprecated
	public static void throwSoftOrHardButAnyway(Throwable t)
	{
		propagateAnyway(t);
	}
	
	public static void propagateAnyway(Throwable t)
	{
		if(t instanceof InvocationTargetException)
		{
			Throwable tmp = ((InvocationTargetException)t).getCause();
			if(null != tmp)
			{
				t = tmp;
			}
		}
		
		if(t instanceof RuntimeException)
		{
			throw (RuntimeException) t;
		}
		else if(t instanceof Error)
		{
			throw (Error) t;
		}
		
		throw new RuntimeException(t);
	}
	
	public static <T> T valueOrDefault(T value, T _default)
	{
		if(null != value)
		{
			return value;
		}
		
		return _default;
	}
	
	public static boolean equals(Object o1, Object o2)
	{
		if(o1 == o2)
		{
			return true;
		}
		
		if(null != o1)
		{
			return o1.equals(o2);
		}
		
		return false;
	}
	
	public static int compare(int a, int b)
	{
		if(a > b)
		{
			return 1;
		}
		else if(b > a)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
	
	public static <T> T shallowClone(T origin, Field[] toClone) throws IllegalArgumentException, IllegalAccessException, InstantiationException
	{
		T ret = (T) origin.getClass().newInstance();
		shallowClone(origin, ret, toClone);
		return ret;
	}
	
	public static <T> void shallowClone(T origin, T newObject, Field[] toClone) throws IllegalArgumentException, IllegalAccessException
	{
		for(Field f:toClone)
		{
			Object src = f.get(origin);
			f.set(newObject, src);
		}
	}
	
	public static final int[] emptyIntArray = new int[0];
	public static final int[][] emptyIntIntArray = new int[0][0];
	public static final byte[] emptyByteArray = new byte[0];
	public static final long[] emptyNLongArray = new long[0];
	public static final Integer[] emptyIntegerArray = new Integer[0];
	public static final Method[] emptyMethodArray = new Method[0];
	
	public static final Field[] emptyFieldArray = new Field[0];
	
	public static final String[] emptyStringArray = new String[0];
	
	public static final String[][] emptyStringArrayArray = new String[0][0];
	
	public static final Object[] emptyObjectArray = new Object[0];
	
	public static final Boolean[] emptyBooleanArray = new Boolean[0];
	
	public static final Long[] emptyLongArray = new Long[0];
	
	protected static InputStream getResource(String relative)
	{
		HashSet<ClassLoader> cls = new HashSet<>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		do
		{
			InputStream is = cl.getResourceAsStream(relative);
			if(null != is)
			{
				return is;
			}
			
			//Cyclic reference in ClassLoaders
			if(cls.contains(cl))
			{
				return null;
			}
			
			cls.add(cl);
			
			cl = cl.getParent();
			
			if(null == cl)
			{
				return null;
			}
		}
		while(true);
	}
	
	public static byte[] getJavaResource(String relativePath) throws IOException
	{
		try(InputStream is = getResource(relativePath))
		{
			if(null == is)
			{
				return Mirror.emptyByteArray;
			}
			return IOTools.loadAllFromInputStream(is);
		}
	}
	
	public static byte[] getPackageResource(Class<?> cls, String string) throws IOException
	{
		String pack = StringTools.getSubstringBeforeLastString(StringTools.replaceAllStrings(cls.getCanonicalName(), ".", "/"), "/");
		
		return getJavaResource(pack+"/"+string);
	}
	
	public static enum CloningMethod implements GetBy2<Object, Field, Object>
	{
		SKIP
		{
			@Override
			public Object getBy(Field a, Object b)
			{
				return null;
			}
		},
		REFERENCE_ASSIGN
		{
			@Override
			public Object getBy(Field a, Object b)
			{
				return b;
			}
		},
		DEEP_COPY
		{
			@Override
			public Object getBy(Field a, Object b)
			{
				try
				{
					return Mirror.clone(b, (GetBy1) SIMPLE_CREATOR, DEEP_COPY);
				}
				catch (Exception e)
				{
					throw new RuntimeException(e);
				}
			}
		},
		//CUSTOM;
	}
	
	public static final GetBy1<Object, Class<?>> SIMPLE_CREATOR = new GetBy1<Object, Class<?>>()
	{
		@Override
		public Object getBy(Class<?> a)
		{
			try
			{
				return a.newInstance();
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	};
	
	public static boolean isStatic(Member f)
	{
		int mod = f.getModifiers();
		return Modifier.isStatic(mod);
	}
	
	public static boolean isPublic(Member f)
	{
		int mod = f.getModifiers();
		return Modifier.isPublic(mod);
	}
	
	public static Object clone(Object obj, GetBy1<Object, Class<?>> simpleCreator, GetBy2<Object, Field, Object> cloner) throws IllegalArgumentException, IllegalAccessException
	{
		Object ret = simpleCreator.getBy(obj.getClass());
		ClassData cd = getClassData(obj.getClass());
		for(Field f:cd.all_fields)
		{
			if(!isStatic(f))
			{
				f.set(ret, cloner.getBy(f, f.get(obj)));
			}
		}
		
		return ret;
	}
	
	public static Object simpleShallowClone(Object obj) throws IllegalArgumentException, IllegalAccessException
	{
		return clone(obj, SIMPLE_CREATOR, CloningMethod.REFERENCE_ASSIGN);
	}

	public static void extractFieldsToMap(Object in, Map<String, Object> values, FieldSelector select) throws IllegalArgumentException, IllegalAccessException
	{
		ClassData dat = getClassData(in.getClass());
		Field[] fs = dat.select(select);
		for(Field f:fs)
		{
			values.put(f.getName(), f.get(in));
		}
	}

	public static Object tryGetFieldValue(Object o, String key)
	{
		if(null == o)
		{
			return null;
		}
		
		try
		{
			Class c = o.getClass();
			Field f = c.getDeclaredField(key);
			f.setAccessible(true);
			return f.get(o);
		}
		catch(Exception e)
		{
			
		}
		return null;
	}
	
	public static Class<?> extracClass(Type t)
	{
		if(t instanceof Class)
		{
			return (Class<?>) t;
		}
		else if(t instanceof GenericArrayType)
		{
			return Array.newInstance(extracClass(((GenericArrayType)t).getGenericComponentType()),0).getClass();
		}
		else if(t instanceof ParameterizedType)
		{
			return extracClass(((ParameterizedType)t).getRawType());
		}
		
		return Object.class;
	}
	
	public static boolean isVoid(Class cls)
	{
		if(null != cls)
		{
			return void.class == cls || Void.class == cls; 
		}
		
		return false;
	}

	protected static final int SHALLOW_EXCLUDE_MODIFIER = 0
		| Modifier.STATIC 
		| Modifier.TRANSIENT
		//| Modifier.
		;
	
	public static boolean shallowEquals(Object me, Object obj)
	{
		if(null == me || null == obj)
		{
			return false;
		}
		
		if(me == obj)
		{
			return true;
		}
		
		if(!me.getClass().isAssignableFrom(obj.getClass()))
		{
			return false;
		}
		
		Field[] fs = getClassData(me.getClass()).self_fields;
		try
		{
			for(Field f:fs)
			{
				if(0 == (f.getModifiers() & SHALLOW_EXCLUDE_MODIFIER))
				{
					if(!Mirror.equals(f.get(me), f.get(obj)))
					{
						return false;
					}
				}
			}
		}
		catch(Exception e)
		{
			propagateAnyway(e);
		}
		
		return true;
	}
	
	public static int shallowHashCode(Object me)
	{
		if(null == me)
		{
			return 0;
		}
		
		int ret = 27;
		
		Field[] fs = getClassData(me.getClass()).self_fields;
		try
		{
			for(Field f:fs)
			{
				if(0 == (f.getModifiers() & SHALLOW_EXCLUDE_MODIFIER))
				{
					int ph = 0;
					Object o = f.get(me);
					if(null != o)
					{
						ph = o.hashCode();
					}
					
					ret = 31* ret + ph;
				}
			}
		}
		catch(Exception e)
		{
			propagateAnyway(e);
		}
		
		return ret;
	}

	public static URL getClassUrl(Class<?> cls)
	{
		String file = StringTools.replaceAllStrings(cls.getName(), ".", "/")+".class";
		return Thread.currentThread().getContextClassLoader().getResource(file);
	}
	
	public static byte[] getClassBytecode(Class<?> cls) throws IOException
	{
		String file = StringTools.replaceAllStrings(cls.getName(), ".", "/")+".class";
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
		if(null != is)
		{
			try
			{
				return IOTools.loadAllFromInputStream(is);
			}
			finally
			{
				is.close();
			}
		}
		
		return null;
	}

	public static void setProperty(Object subject, String field, Object value) throws IllegalArgumentException, IllegalAccessException
	{
		ClassData cd = getClassData(subject.getClass());
		for(Field f:cd.getAllFields())
		{
			if(f.getName().equals(field))
			{
				f.set(subject, value);
			}
		}
	}

	public static <T> T passOnlyInstanceOf
	(
		Class<T> cls,
		Object object
	)
	{
		if(null != object)
		{
			if(cls.isAssignableFrom(object.getClass()))
			{
				return (T) object;
			}
		}
		
		return null;
	}
	
	public static boolean isRestAppicableForType
	(
		Class forClass,
		int startIndexInclusive,
		boolean mayNull,
		Class... inputClasses
	)
	{
		for(int i=startIndexInclusive;i<inputClasses.length;++i)
		{
			Class c = inputClasses[i];
			if(null == c)
			{
				if(!mayNull)
				{
					return false;
				}
				else
				{
					continue;
				}
			}
			
			if(!forClass.isAssignableFrom(c))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static <T> Object[] varargize(Class<T> reqClass, int startIndexInclusiveDst, Object... params)
	{
		T[] varargs = (T[]) Array.newInstance(reqClass, params.length-startIndexInclusiveDst);
		Object[] ret = new Object[startIndexInclusiveDst+1]; 
		for(int i=0;i<=startIndexInclusiveDst;++i)
		{
			ret[i] = params[i];
		}
		
		ret[startIndexInclusiveDst] = varargs;
		
		for(int i=0;i<varargs.length;++i)
		{
			varargs[i] = (T) params[startIndexInclusiveDst+i];
		}
		
		return ret;
	}
	
	public static <T> Class[] getClasses(T[] arr)
	{
		Class[] ret = new Class[arr.length];
		for(int i = 0;i<arr.length;++i)
		{
			Object o = arr[i];
			ret[i] = null == o?null:o.getClass();
		}
		return ret;
	}
	
	public static Object callMethod
	(
		Object subject,
		String method,
		Object... params
	)
		throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Class[] obj = new Class[params.length];
		ClassData cd = getClassData(subject.getClass());
		Method func = null;
		
		Class[] pclasses = getClasses(params);
		for(Method m:cd.all_methods)
		{
			if(isStatic(m))
			{
				continue;
			}
			
			if(method.equals(m.getName()))
			{
				Class[] ps = m.getParameterTypes();
				if(ps.length < params.length)
				{
					throw new RuntimeException("Parameter count mismatch: req_params:"+Arrays.toString(ps)+", given: "+pclasses);
				}
				//varargs 
				else if(ps.length > params.length)
				{
					if(!isRestAppicableForType(ps[ps.length-1], ps.length-1, true, pclasses))
					{
						throw new RuntimeException("Can't varargize parameters: req_params:"+Arrays.toString(ps)+", given: "+pclasses);
					}
					
					params = varargize(ps[ps.length-1], ps.length-1, params);
				}
				
				for(int i=0;i<ps.length;++i)
				{
					Class fc = ps[i];
					Class pc = pclasses[i];
					if(null != pc && !fc.isAssignableFrom(pc))
					{
						throw new RuntimeException("Invalid argument type: req: "+fc+" given: "+pc);
					}
				}
				
				func = m;
				break;
			}
		}
		
		if(null == func)
		{
			throw new RuntimeException("Method (\""+method+"\") not found in :"+subject.getClass());
		}
		
		return func.invoke(subject, params);
	}

	public static <T> boolean trySetObjectField
	(
		Class<T> cls,
		T instance,
		String field,
		Object value
	)
	{
		try
		{
			Field ret = cls.getDeclaredField(field);
			setAccessible(ret);
			ret.set(instance, value);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public static String usualToString(Object obj)
	{
		ClassData cd = Mirror.getClassData(obj.getClass());
		Field[] fs = cd.selectFields(FieldSelectTools.SELECT_ALL_INSTANCE_FIELD);
		StringBuilder sb = new StringBuilder();
		sb.append(obj.getClass().getSimpleName());
		sb.append(": {");
		
		boolean hasOut = false;
		for(Field f:fs)
		{
			Class ft = f.getType();
			if(hasOut)
			{
				sb.append(", ");
			}
			sb.append(f.getName());
			sb.append(": ");
			try
			{
				Object o = f.get(obj);
				if(null == o)
				{
					sb.append("null");
				}
				else if(Collection.class.isAssignableFrom(o.getClass()))
				{
					sb.append(CollectionTools.toString((Collection<?>) o));
				}
				else if(Map.class.isAssignableFrom(o.getClass()))
				{
					sb.append(MapTools.toString((Map<?, ?>) o));
				}
				else if(Date.class.isAssignableFrom(o.getClass()))
				{
					sb.append(Format.UTC_SQL_TIMESTAMP_MS.format((Date)o));
				}
				else
				{
					sb.append(o);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			hasOut = true;
		}
		
		sb.append("}");
		
		return sb.toString();
	}

	public static <T> Constructor<T> getDeclaredConstructor(Class<T> class1, Class... params)
	{
		try
		{
			Constructor<T> ret = class1.getDeclaredConstructor(params);
			ret.setAccessible(true);
			return ret;
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}

	public static Method getDeclaredMethod(Class<Socket> class1, String name, Class... params)
	{
		try
		{
			Method ret = class1.getDeclaredMethod(name, params);
			ret.setAccessible(true);
			return ret;
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	public static Object tryCallBeanMethod(Method m, Object subject)
	{
		try
		{
			return m.invoke(subject);
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	public static Object tryCallMethod(Method m, Object subject, Object... params)
	{
		try
		{
			return m.invoke(subject, params);
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
}