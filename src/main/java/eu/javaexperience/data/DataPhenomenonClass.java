package eu.javaexperience.data;

public enum DataPhenomenonClass
{
	/**
	 * true/false
	 * */
	BOOLEAN,
	
	/**
	 * not the int32 but the non fraction number
	 * */
	INTEGER_NUMBER,
	
	/**
	 * not the float64 but any fraction number
	 * */
	DECIMAL_NUMBER,
	
	/**
	 * from one char, trough small labels to logn textes
	 * */
	TEXT,
	
	/**
	 * Binary data byte[]
	 * */
	BLOB,
	
	/**
	 * 
	 * */
	ENUMERATION,
	FLAG_SET,
	ARRAY,
	OBJECT
}
