package eu.javaexperience.database.impl;

import java.util.HashMap;

import eu.javaexperience.collection.map.RWLockMap;
import eu.javaexperience.database.DatabaseFieldManager;
import eu.javaexperience.database.pojodb.Model;

public class JavaDatabaseFieldMapperFacility
{
	protected static final RWLockMap<Class<? extends Model>, DatabaseFieldManager> MNGSR = 
		new RWLockMap<Class<? extends Model>, DatabaseFieldManager>(new HashMap<Class<? extends Model>, DatabaseFieldManager>());
	
	
	
	
}
