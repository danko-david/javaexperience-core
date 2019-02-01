package eu.javaexperience.exceptions;

public class OperationSuccessfullyEnded extends Error //ha RuntimeException lenne akkor a catch(Exception e)  meg tudná fogni...
{
	private static final long serialVersionUID = 1L;
	
	public static OperationSuccessfullyEnded instance = new OperationSuccessfullyEnded(); 
}