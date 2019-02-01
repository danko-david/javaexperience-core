package eu.javaexperience.datastorage;

import java.io.Closeable;
import java.util.Map;

public interface DataTransaction extends Map<String, Object>, Closeable
{
	public void commit() throws TransactionException;
	public void rollback();
}
