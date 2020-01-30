package eu.javaexperience.database.pojodb;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import eu.javaexperience.database.JDBC;
import eu.javaexperience.database.pojodb.dialect.SqlDialect;
import eu.javaexperience.query.AtomicCondition;
import eu.javaexperience.query.LogicalGroup;
import eu.javaexperience.query.LogicalRelation;

public class SqlTools
{
	public static void buildQuery(StringBuilder sb, LogicalGroup lg, SqlDialect dialect)
	{
		final String quote = dialect.getFieldQuoteString();
		final String strQuote = dialect.getStringQuote();
		switch (lg.getLogicalRelation())
		{
		case and:
		case or:
			boolean nfirst = false;
			for(LogicalGroup g:lg.getLogicalGroups())
			{
				if(nfirst)
					sb.append(lg.getLogicalRelation() == LogicalRelation.and?" AND ":" OR ");
				sb.append("(");
				buildQuery(sb, g, dialect);
				sb.append(")");
				nfirst = true;
			}
			break;
			
		case unit:
			AtomicCondition c = lg.getAtomicCondition();
			sb.append(quote);
			sb.append(c.getFieldName());
			sb.append(quote);
			
			switch (c.getOperator())
			{
			case contains:
				if(c.isNegated())
					sb.append(" NOT");
					
				sb.append(" LIKE ");
				sb.append(strQuote);
				sb.append("%");
				
				//mysql fix... see "Note" https://dev.mysql.com/doc/refman/8.0/en/string-comparison-functions.html "To search for \, specify it as \\\\; "
				String add = (String) c.getValue();
				if(null == add)
				{
					add = "null";
				}
				sb.append(dialect.escapeString(add));
				sb.append("%");
				sb.append(strQuote);
				break;
				
			case eq:
				if(c.getValue() == null)
				{
					if(c.isNegated())
						sb.append(" IS NOT NULL");
					else
						sb.append(" IS NULL");
				}
				else
				{
					sb.append(" ");
					if(c.isNegated())
						sb.append("!");
					
					sb.append("= ");
					if(c.getValue() instanceof String)
					{
						sb.append(strQuote);
						sb.append(dialect.toQueryString(c.getValue()));
						sb.append(strQuote);
					}
					else
						sb.append(dialect.toQueryString(c.getValue()));
				}
				break;
				
			case match:
				if(c.isNegated())
					sb.append(" NOT");
					
				sb.append(" REGEX ");
				sb.append(dialect.toQueryString(c.getValue()));
				break;
				
			case gt:
				if(c.isNegated())
					sb.append(" < ");
				else
					sb.append(" >= ");
				
				sb.append(dialect.toQueryString(c.getValue()));
				break;
				
			case gte:
				if(c.isNegated())
					sb.append(" <= ");
				else
					sb.append(" > ");
				
				sb.append(dialect.toQueryString(c.getValue()));
				break;
				
			case lt:
				if(c.isNegated())
					sb.append(" > ");
				else
					sb.append(" <= ");
				
				sb.append(dialect.toQueryString(c.getValue()));
				break;
				
			case lte:
				if(c.isNegated())
					sb.append(" >= ");
				else
					sb.append(" < ");
				
				sb.append(dialect.toQueryString(c.getValue()));
				break;
			
			case in:
				try
				{
					Object val = c.getValue();
					Iterable it = null;
					
					int length = -1;
					if(val instanceof Collection)
					{
						it = (Iterable) val;
						length = ((Collection) val).size();
					}
					else if(val.getClass().isArray())
					{
						ArrayList ar = new ArrayList<>();
						length = Array.getLength(val);
						for(int i=0;i<length;++i)
						{
							ar.add(Array.get(val, i));
						}
						
						it = ar;
					}
					
					if(0 == length)
					{
						if(c.isNegated())
							sb.append(" IS NOT NULL OR TRUE ");
						else
							sb.append(" IS NULL AND FALSE ");
					}
					else
					{
						if(c.isNegated())
							sb.append(" NOT IN ");
						else
							sb.append(" IN ");
						
						JDBC.listing(sb, it, (v)->dialect.toQueryString(v));
					}
					
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			}
			break;
		}
	}
	
	public static void alterTableAddFields(Connection conn, Model model, SqlDialect dialect) throws SQLException
	{
		final String table = model.getTable();
		if(!JDBC.isTableExists(conn, table))
		{
			createTable(conn, model, dialect);
		}
		else
		{
			//check for alter
			//delete and type alteration must happen manually.
			ArrayList<Field> toAdd = new ArrayList<>();
			ArrayList<String> dbf = new ArrayList<>();
			dialect.getTableFields(conn, dbf, table);
			
			out:for(Field fd:model.getFields())
			{
				for(String f:dbf)
				{
					if(fd.getName().equals(f))
					{
						continue out;
					}
				}
				toAdd.add(fd);
			}
			
			if(toAdd.size() > 0)
			{
				String quote = dialect.getFieldQuoteString();
				
				StringBuilder sb = new StringBuilder();
				sb.append("ALTER TABLE ");
				sb.append(quote);
				sb.append(table);
				sb.append(quote);
				sb.append(" ADD ");
				
				int n = 0;
				
				for(Field f:toAdd)
				{
					if(n++ > 0)
					{
						sb.append(", ");
					}
					sb.append(quote);
					sb.append(f.getName());
					sb.append(quote);
					sb.append(" ");
					sb.append(dialect.getSqlType(f));
				}
				
				try(Statement st = conn.createStatement())
				{
					st.execute(sb.toString());
				}
			}
		}
	}

	public static boolean createTable(Connection conn, Model m, SqlDialect dialect) throws SQLException
	{
		final String quote = dialect.getFieldQuoteString();
		
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(quote);
		sb.append(m.getTable());
		sb.append(quote);
		sb.append(" (");
		
		int i = 0;
		for(Field fd:m.getFields())
		{
			String type = dialect.getSqlType(fd);
			if(null != type)
			{
				if(i++ > 0)
				{
					sb.append(", ");
				}
				
				sb.append(quote);
				sb.append(fd.getName());
				sb.append(quote);
				sb.append(" ");
				sb.append(type);
			}
		}
		
		sb.append(")");
		sb.append(dialect.getOtherTableCreateOptions());
		
		try
		(
				Statement st = conn.createStatement();
		)
		{
			return st.execute(sb.toString());
		}
	}
}
