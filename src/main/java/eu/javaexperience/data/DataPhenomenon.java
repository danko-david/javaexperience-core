package eu.javaexperience.data;

import eu.javaexperience.interfaces.ExternalDataAttached;
import eu.javaexperience.verify.EnvironmentDependValidator;

/**
 * Data Phenomenons are definied by programmer in the scope of the current
 * 	project. The goal of this abstraction is to provide highly flexible and
 * 	reusable data management across the font and backend.
 * 
 * 	Goals:
 * 		- Store information about data representation (String length: 50 character/50 byte)
 * 		- validation:
 * 				- data storage level and
 * 				- other user defined EnvironmentDependValidator<E, T, R>
 * 				- validation method: until first error, vaildate all
 * 				- post validation entry selector 
 * 		
 * 		- manage compaund types (types that represented as one type but stored in two field)
 * 			- manage mapping between DataPhenomenon and data storage layer (name mangling) 
 * 			- manage Phenomenon variants: username is an email.
 * 
 * 		- manage restrictions:
 * 			- if the phenomenon should be a selected element of:
 * 				-  a well known set, store the set itself
 * 				- if an element should be retrived, an examiner function should be attached
 * 					that can examine the entries from the PhenomenonEnvironment
 * 		
 * 		- simplified UI type: text, content (html), chooser, radio, check, other
 * 
 * 		//- some special data management mode: is the result is multiple selection of a set (checkbox set)? 
 * 		
 * 		
 * 		- model class attachments (Exact class, creator function: GetBy1<Model, DataPhenomenon>)
 * 		
 * 		
 * 		- Store other user data (Map<String,Object>)
 * 
 * 
 * 	Some example:
 * 
 * 		- Identifier, variants: String, int
 * 		- Reference, variants: fixed_type, type_set
 * 		- email: email_address
 * 		- username, variants: email_address, String(60 byte), String matching regex.
 * 		- image: 
 * 		- icon, variants: tbs-glyphicon, image 
 * 
 * examples:
 * 		- username input (validator: username already exists?)
 * 		- email input 
 * 		- multi checkbox selection (checkbox set represented as a long)
 * 		- image selection
 * 		
 * scenario:
 * 		- Add a username phenomenon on a page. If user types the username
 * 			we check that username is valid (not start or trail with whitespace, constains least 5 character)
 * 			and not exists on server side,
 * 
 * 		- password: meets the strong password criteria, not any of the previous
 *  		passwords.
 * 
 *  	- password again: equals the "password" field in this scope.
 *  
 *  	- GPS location: 48.0892483,20.60571,12z
 *  	- TextFreeLocation: 1128 El Camino Real, Burlingame, Kalifornia, Egyesült Államok
 * 
 * 
 * */
public interface DataPhenomenon extends ExternalDataAttached
{
	//public String getFieldName();
	public int getFieldDataLength();
	public DataPhenomenonClass getDataClass();
	
	public EnvironmentDependValidator<DataPhenomenonEnvironment, String, String> getValidator();
	
}
