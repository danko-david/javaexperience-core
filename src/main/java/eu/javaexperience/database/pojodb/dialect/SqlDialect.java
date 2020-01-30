package eu.javaexperience.database.pojodb.dialect;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import eu.javaexperience.semantic.references.MayNull;

public interface SqlDialect
{
	public boolean probeDialect(Connection conn);
	public String getSqlType(Field fd);
	public String getFieldQuoteString();
	public String getStringQuote();
	public @MayNull String getOtherTableCreateOptions();
	public void getTableFields(Connection connection, Collection<String> dbf, String table) throws SQLException;
	public String escapeString(String value);
	public String toQueryString(Object add);
}