package eu.javaexperience.config;

/***
 * product.category.0.name = 
 * product.category.0.db_id = 10
 * product.category.0.preprocess = toLowerCase; deAccent, passJustNumbers  
 * 
 * 
 * system.dispatcher.0.type = attach_directory
 * system.dispatcher.0.path = /assets/
 * system.dispatcher.0.dir = ./assets
 * system.dispatcher.0.list_dir = false
 * 
 * system.dispatcher.1.type = java_class
 * system.dispatcher.1.class = ajax
 * system.dispatcher.1.path = /ajax/
 * 
 * system.dispatcher.2.path = /files/
 * system.dispatcher.2.dir = ./files/
 * system.dispatcher.2.list_dir = false
 * 
 * 
 * getConfigsFrom("system.dispatcher") => TreeConfigStorage[]
 * 
 * 
 * 
 * */
public class TreeConfig
{
	
}