package eu.javaexperience.database.failsafe;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

public class FailSafeDatabaseMetadata implements DatabaseMetaData
{
	protected JdbcFailSafeConnection conn;
	protected DatabaseMetaData _;
	
	public FailSafeDatabaseMetadata(JdbcFailSafeConnection jdbcFailSafeConnection) throws SQLException
	{
		this.conn = jdbcFailSafeConnection;
		recreate();
	}

	protected void recreate() throws SQLException
	{
		try
		{
			_ = conn.conn.getMetaData();
		}
		catch(SQLException e)
		{
			conn.reconnect(e);
			for(int i=1;i<conn.att;i++)
			{
				try
				{
					_ = conn.conn.getMetaData();
					return;
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}
	
	@Override
	public boolean allProceduresAreCallable() throws SQLException
	{
		try
		{
			return _.allProceduresAreCallable();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.allProceduresAreCallable();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean allTablesAreSelectable() throws SQLException
	{
		try
		{
			return _.allTablesAreSelectable();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.allTablesAreSelectable();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getUserName() throws SQLException
	{
		try
		{
			return _.getUserName();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getUserName();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean nullsAreSortedHigh() throws SQLException
	{
		try
		{
			return _.nullsAreSortedHigh();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.nullsAreSortedHigh();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean nullsAreSortedLow() throws SQLException
	{
		try
		{
			return _.nullsAreSortedLow();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.nullsAreSortedLow();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean nullsAreSortedAtStart() throws SQLException
	{
		try
		{
			return _.nullsAreSortedAtStart();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.nullsAreSortedAtStart();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean nullsAreSortedAtEnd() throws SQLException
	{
		try
		{
			return _.nullsAreSortedAtEnd();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.nullsAreSortedAtEnd();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getDatabaseProductName() throws SQLException
	{
		try
		{
			return _.getDatabaseProductName();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getDatabaseProductName();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getDatabaseProductVersion() throws SQLException
	{
		try
		{
			return _.getDatabaseProductVersion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getDatabaseProductVersion();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getDriverName() throws SQLException
	{
		try
		{
			return _.getDriverName();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getDriverName();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getDriverVersion() throws SQLException
	{
		try
		{
			return _.getDriverVersion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getDriverVersion();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getDriverMajorVersion()
	{
		return _.getDriverMajorVersion();
	}

	@Override
	public int getDriverMinorVersion()
	{
		return _.getDriverMinorVersion();
	}

	@Override
	public boolean usesLocalFiles() throws SQLException
	{
		try
		{
			return _.usesLocalFiles();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.usesLocalFiles();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean usesLocalFilePerTable() throws SQLException
	{
		try
		{
			return _.usesLocalFilePerTable();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.usesLocalFilePerTable();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsMixedCaseIdentifiers() throws SQLException
	{
		try
		{
			return _.supportsMixedCaseIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsMixedCaseIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean storesUpperCaseIdentifiers() throws SQLException
	{
		try
		{
			return _.storesUpperCaseIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.storesUpperCaseIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean storesLowerCaseIdentifiers() throws SQLException
	{
		try
		{
			return _.storesLowerCaseIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.storesLowerCaseIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean storesMixedCaseIdentifiers() throws SQLException
	{
		try
		{
			return _.storesMixedCaseIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.storesMixedCaseIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException
	{
		try
		{
			return _.supportsMixedCaseQuotedIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsMixedCaseQuotedIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException
	{
		try
		{
			return _.storesUpperCaseQuotedIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.storesUpperCaseQuotedIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException
	{
		try
		{
			return _.storesLowerCaseQuotedIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.storesLowerCaseQuotedIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException
	{
		try
		{
			return _.storesMixedCaseQuotedIdentifiers();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.storesMixedCaseQuotedIdentifiers();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getIdentifierQuoteString() throws SQLException
	{
		try
		{
			return _.getIdentifierQuoteString();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getIdentifierQuoteString();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getSQLKeywords() throws SQLException
	{
		try
		{
			return _.getSQLKeywords();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSQLKeywords();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getNumericFunctions() throws SQLException
	{
		try
		{
			return _.getNumericFunctions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getNumericFunctions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getStringFunctions() throws SQLException
	{
		try
		{
			return _.getStringFunctions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getStringFunctions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getSystemFunctions() throws SQLException
	{
		try
		{
			return _.getSystemFunctions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSystemFunctions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getTimeDateFunctions() throws SQLException
	{
		try
		{
			return _.getTimeDateFunctions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getTimeDateFunctions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getSearchStringEscape() throws SQLException
	{
		try
		{
			return _.getSearchStringEscape();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSearchStringEscape();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getExtraNameCharacters() throws SQLException
	{
		try
		{
			return _.getExtraNameCharacters();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getExtraNameCharacters();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsAlterTableWithAddColumn() throws SQLException
	{
		try
		{
			return _.supportsAlterTableWithAddColumn();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsAlterTableWithAddColumn();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsAlterTableWithDropColumn() throws SQLException
	{
		try
		{
			return _.supportsAlterTableWithDropColumn();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsAlterTableWithDropColumn();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsColumnAliasing() throws SQLException
	{
		try
		{
			return _.supportsColumnAliasing();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsColumnAliasing();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException
	{
		try
		{
			return _.nullPlusNonNullIsNull();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.nullPlusNonNullIsNull();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsConvert() throws SQLException
	{
		try
		{
			return _.supportsConvert();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsConvert();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsConvert(int a, int b) throws SQLException
	{
		try
		{
			return _.supportsConvert(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsConvert(a, b);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsTableCorrelationNames() throws SQLException
	{
		try
		{
			return _.supportsTableCorrelationNames();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsTableCorrelationNames();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsDifferentTableCorrelationNames() throws SQLException
	{
		try
		{
			return _.supportsDifferentTableCorrelationNames();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsDifferentTableCorrelationNames();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsExpressionsInOrderBy() throws SQLException
	{
		try
		{
			return _.supportsExpressionsInOrderBy();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsExpressionsInOrderBy();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsOrderByUnrelated() throws SQLException
	{
		try
		{
			return _.supportsOrderByUnrelated();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsOrderByUnrelated();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsGroupBy() throws SQLException
	{
		try
		{
			return _.supportsGroupBy();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsGroupBy();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsGroupByUnrelated() throws SQLException
	{
		try
		{
			return _.supportsGroupByUnrelated();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsGroupByUnrelated();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsGroupByBeyondSelect() throws SQLException
	{
		try
		{
			return _.supportsGroupByBeyondSelect();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsGroupByBeyondSelect();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsLikeEscapeClause() throws SQLException
	{
		try
		{
			return _.supportsLikeEscapeClause();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsLikeEscapeClause();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsMultipleResultSets() throws SQLException
	{
		try
		{
			return _.supportsMultipleResultSets();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsMultipleResultSets();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsMultipleTransactions() throws SQLException
	{
		try
		{
			return _.supportsMultipleTransactions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsMultipleTransactions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsNonNullableColumns() throws SQLException
	{
		try
		{
			return _.supportsNonNullableColumns();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsNonNullableColumns();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsMinimumSQLGrammar() throws SQLException
	{
		try
		{
			return _.supportsMinimumSQLGrammar();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsMinimumSQLGrammar();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException
	{
		try
		{
			return _.supportsCoreSQLGrammar();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsCoreSQLGrammar();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsExtendedSQLGrammar() throws SQLException
	{
		try
		{
			return _.supportsExtendedSQLGrammar();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsExtendedSQLGrammar();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsANSI92EntryLevelSQL() throws SQLException
	{
		try
		{
			return _.supportsANSI92EntryLevelSQL();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsANSI92EntryLevelSQL();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsANSI92IntermediateSQL() throws SQLException
	{
		try
		{
			return _.supportsANSI92IntermediateSQL();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsANSI92IntermediateSQL();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsANSI92FullSQL() throws SQLException
	{
		try
		{
			return _.supportsANSI92FullSQL();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsANSI92FullSQL();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsIntegrityEnhancementFacility() throws SQLException
	{
		try
		{
			return _.supportsIntegrityEnhancementFacility();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsIntegrityEnhancementFacility();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsOuterJoins() throws SQLException
	{
		try
		{
			return _.supportsOuterJoins();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsOuterJoins();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsFullOuterJoins() throws SQLException
	{
		try
		{
			return _.supportsFullOuterJoins();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsFullOuterJoins();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsLimitedOuterJoins() throws SQLException
	{
		try
		{
			return _.supportsLimitedOuterJoins();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsLimitedOuterJoins();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getSchemaTerm() throws SQLException
	{
		try
		{
			return _.getSchemaTerm();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSchemaTerm();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getProcedureTerm() throws SQLException
	{
		try
		{
			return _.getProcedureTerm();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getProcedureTerm();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getCatalogTerm() throws SQLException
	{
		try
		{
			return _.getCatalogTerm();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getCatalogTerm();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean isCatalogAtStart() throws SQLException
	{
		try
		{
			return _.isCatalogAtStart();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.isCatalogAtStart();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getCatalogSeparator() throws SQLException
	{
		try
		{
			return _.getCatalogSeparator();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getCatalogSeparator();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSchemasInDataManipulation() throws SQLException
	{
		try
		{
			return _.supportsSchemasInDataManipulation();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSchemasInDataManipulation();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSchemasInProcedureCalls() throws SQLException
	{
		try
		{
			return _.supportsSchemasInProcedureCalls();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSchemasInProcedureCalls();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSchemasInTableDefinitions() throws SQLException
	{
		try
		{
			return _.supportsSchemasInTableDefinitions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSchemasInTableDefinitions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSchemasInIndexDefinitions() throws SQLException
	{
		try
		{
			return _.supportsSchemasInIndexDefinitions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSchemasInIndexDefinitions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException
	{
		try
		{
			return _.supportsSchemasInPrivilegeDefinitions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSchemasInPrivilegeDefinitions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsCatalogsInDataManipulation() throws SQLException
	{
		try
		{
			return _.supportsCatalogsInDataManipulation();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsCatalogsInDataManipulation();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsCatalogsInProcedureCalls() throws SQLException
	{
		try
		{
			return _.supportsCatalogsInProcedureCalls();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsCatalogsInProcedureCalls();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsCatalogsInTableDefinitions() throws SQLException
	{
		try
		{
			return _.supportsCatalogsInTableDefinitions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsCatalogsInTableDefinitions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException
	{
		try
		{
			return _.supportsCatalogsInIndexDefinitions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsCatalogsInIndexDefinitions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException
	{
		try
		{
			return _.supportsCatalogsInPrivilegeDefinitions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsCatalogsInPrivilegeDefinitions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsPositionedDelete() throws SQLException
	{
		try
		{
			return _.supportsPositionedDelete();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsPositionedDelete();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsPositionedUpdate() throws SQLException
	{
		try
		{
			return _.supportsPositionedUpdate();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsPositionedUpdate();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSelectForUpdate() throws SQLException
	{
		try
		{
			return _.supportsSelectForUpdate();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSelectForUpdate();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsStoredProcedures() throws SQLException
	{
		try
		{
			return _.supportsStoredProcedures();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsStoredProcedures();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSubqueriesInComparisons() throws SQLException
	{
		try
		{
			return _.supportsSubqueriesInComparisons();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSubqueriesInComparisons();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSubqueriesInExists() throws SQLException
	{
		try
		{
			return _.supportsSubqueriesInExists();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSubqueriesInExists();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSubqueriesInIns() throws SQLException
	{
		try
		{
			return _.supportsSubqueriesInIns();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSubqueriesInIns();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSubqueriesInQuantifieds() throws SQLException
	{
		try
		{
			return _.supportsSubqueriesInQuantifieds();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSubqueriesInQuantifieds();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsCorrelatedSubqueries() throws SQLException
	{
		try
		{
			return _.supportsCorrelatedSubqueries();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsCorrelatedSubqueries();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsUnion() throws SQLException
	{
		try
		{
			return _.supportsUnion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsUnion();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsUnionAll() throws SQLException
	{
		try
		{
			return _.supportsUnionAll();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsUnionAll();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException
	{
		try
		{
			return _.supportsOpenCursorsAcrossCommit();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsOpenCursorsAcrossCommit();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException
	{
		try
		{
			return _.supportsOpenCursorsAcrossRollback();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsOpenCursorsAcrossRollback();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException
	{
		try
		{
			return _.supportsOpenStatementsAcrossCommit();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsOpenStatementsAcrossCommit();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException
	{
		try
		{
			return _.supportsOpenStatementsAcrossRollback();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsOpenStatementsAcrossRollback();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxBinaryLiteralLength() throws SQLException
	{
		try
		{
			return _.getMaxBinaryLiteralLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxBinaryLiteralLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxCharLiteralLength() throws SQLException
	{
		try
		{
			return _.getMaxCharLiteralLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxCharLiteralLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxColumnNameLength() throws SQLException
	{
		try
		{
			return _.getMaxColumnNameLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxColumnNameLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxColumnsInGroupBy() throws SQLException
	{
		try
		{
			return _.getMaxColumnsInGroupBy();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxColumnsInGroupBy();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxColumnsInIndex() throws SQLException
	{
		try
		{
			return _.getMaxColumnsInIndex();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxColumnsInIndex();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxColumnsInOrderBy() throws SQLException
	{
		try
		{
			return _.getMaxColumnsInOrderBy();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxColumnsInOrderBy();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxColumnsInSelect() throws SQLException
	{
		try
		{
			return _.getMaxColumnsInSelect();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxColumnsInSelect();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxColumnsInTable() throws SQLException
	{
		try
		{
			return _.getMaxColumnsInTable();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxColumnsInTable();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxConnections() throws SQLException
	{
		try
		{
			return _.getMaxConnections();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxConnections();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxCursorNameLength() throws SQLException
	{
		try
		{
			return _.getMaxCursorNameLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxCursorNameLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxIndexLength() throws SQLException
	{
		try
		{
			return _.getMaxIndexLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxIndexLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxSchemaNameLength() throws SQLException
	{
		try
		{
			return _.getMaxSchemaNameLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxSchemaNameLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxProcedureNameLength() throws SQLException
	{
		try
		{
			return _.getMaxProcedureNameLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxProcedureNameLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxCatalogNameLength() throws SQLException
	{
		try
		{
			return _.getMaxCatalogNameLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxCatalogNameLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxRowSize() throws SQLException
	{
		try
		{
			return _.getMaxRowSize();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxRowSize();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException
	{
		try
		{
			return _.doesMaxRowSizeIncludeBlobs();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.doesMaxRowSizeIncludeBlobs();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxStatementLength() throws SQLException
	{
		try
		{
			return _.getMaxStatementLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxStatementLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxStatements() throws SQLException
	{
		try
		{
			return _.getMaxStatements();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxStatements();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxTableNameLength() throws SQLException
	{
		try
		{
			return _.getMaxTableNameLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxTableNameLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxTablesInSelect() throws SQLException
	{
		try
		{
			return _.getMaxTablesInSelect();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxTablesInSelect();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getMaxUserNameLength() throws SQLException
	{
		try
		{
			return _.getMaxUserNameLength();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getMaxUserNameLength();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getDefaultTransactionIsolation() throws SQLException
	{
		try
		{
			return _.getDefaultTransactionIsolation();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getDefaultTransactionIsolation();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsTransactions() throws SQLException
	{
		try
		{
			return _.supportsTransactions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsTransactions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsTransactionIsolationLevel(int a) throws SQLException
	{
		try
		{
			return _.supportsTransactionIsolationLevel(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsTransactionIsolationLevel(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException
	{
		try
		{
			return _.supportsDataDefinitionAndDataManipulationTransactions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsDataDefinitionAndDataManipulationTransactions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException
	{
		try
		{
			return _.supportsDataManipulationTransactionsOnly();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsDataManipulationTransactionsOnly();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException
	{
		try
		{
			return _.dataDefinitionCausesTransactionCommit();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.dataDefinitionCausesTransactionCommit();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException
	{
		try
		{
			return _.dataDefinitionIgnoredInTransactions();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.dataDefinitionIgnoredInTransactions();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getProcedures(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getProcedures(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getProcedures(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getProcedureColumns(String a, String b, String c, String d) throws SQLException
	{
		try
		{
			return _.getProcedureColumns(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getProcedureColumns(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getTables(String a, String b, String c, String[] d) throws SQLException
	{
		try
		{
			return _.getTables(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getTables(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getSchemas() throws SQLException
	{
		try
		{
			return _.getSchemas();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSchemas();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getSchemas(String a, String b) throws SQLException
	{
		try
		{
			return _.getSchemas(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSchemas(a, b);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getCatalogs() throws SQLException
	{
		try
		{
			return _.getCatalogs();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getCatalogs();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getTableTypes() throws SQLException
	{
		try
		{
			return _.getTableTypes();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getTableTypes();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getColumns(String a, String b, String c, String d) throws SQLException
	{
		try
		{
			return _.getColumns(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getColumns(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getColumnPrivileges(String a, String b, String c, String d) throws SQLException
	{
		try
		{
			return _.getColumnPrivileges(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getColumnPrivileges(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getTablePrivileges(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getTablePrivileges(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getTablePrivileges(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getBestRowIdentifier(String a, String b, String c, int d, boolean e) throws SQLException
	{
		try
		{
			return _.getBestRowIdentifier(a, b, c, d, e);
		}
		catch(SQLException ex)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getBestRowIdentifier(a, b, c, d, e);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw ex;
		}
	}

	@Override
	public ResultSet getVersionColumns(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getVersionColumns(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getVersionColumns(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getPrimaryKeys(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getPrimaryKeys(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getPrimaryKeys(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getImportedKeys(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getImportedKeys(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getImportedKeys(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getExportedKeys(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getExportedKeys(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getExportedKeys(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getCrossReference(String a, String b, String c, String d, String e, String f) throws SQLException
	{
		try
		{
			return _.getCrossReference(a, b, c, d, e, f);
		}
		catch(SQLException ex)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getCrossReference(a, b, c, d, e, f);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw ex;
		}
	}

	@Override
	public ResultSet getTypeInfo() throws SQLException
	{
		try
		{
			return _.getTypeInfo();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getTypeInfo();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getIndexInfo(String a, String b, String c, boolean d, boolean e) throws SQLException
	{
		try
		{
			return _.getIndexInfo(a, b, c, d, e);
		}
		catch(SQLException ex)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getIndexInfo(a, b, c, d, e);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw ex;
		}
	}

	@Override
	public boolean supportsResultSetType(int a) throws SQLException
	{
		try
		{
			return _.supportsResultSetType(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsResultSetType(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsResultSetConcurrency(int a, int b) throws SQLException
	{
		try
		{
			return _.supportsResultSetConcurrency(a, b);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsResultSetConcurrency(a, b);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean ownUpdatesAreVisible(int a) throws SQLException
	{
		try
		{
			return _.ownUpdatesAreVisible(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.ownUpdatesAreVisible(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean ownDeletesAreVisible(int a) throws SQLException
	{
		try
		{
			return _.ownDeletesAreVisible(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.ownDeletesAreVisible(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean ownInsertsAreVisible(int a) throws SQLException
	{
		try
		{
			return _.ownInsertsAreVisible(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.ownInsertsAreVisible(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean othersUpdatesAreVisible(int a) throws SQLException
	{
		try
		{
			return _.othersUpdatesAreVisible(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.othersUpdatesAreVisible(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean othersDeletesAreVisible(int a) throws SQLException
	{
		try
		{
			return _.othersDeletesAreVisible(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.othersDeletesAreVisible(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean othersInsertsAreVisible(int a) throws SQLException
	{
		try
		{
			return _.othersInsertsAreVisible(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.othersInsertsAreVisible(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean updatesAreDetected(int a) throws SQLException
	{
		try
		{
			return _.updatesAreDetected(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.updatesAreDetected(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean deletesAreDetected(int a) throws SQLException
	{
		try
		{
			return _.deletesAreDetected(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.deletesAreDetected(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean insertsAreDetected(int a) throws SQLException
	{
		try
		{
			return _.insertsAreDetected(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.insertsAreDetected(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsBatchUpdates() throws SQLException
	{
		try
		{
			return _.supportsBatchUpdates();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsBatchUpdates();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getUDTs(String a, String b, String c, int[] d) throws SQLException
	{
		try
		{
			return _.getUDTs(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getUDTs(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsSavepoints() throws SQLException
	{
		try
		{
			return _.supportsSavepoints();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsSavepoints();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsNamedParameters() throws SQLException
	{
		try
		{
			return _.supportsNamedParameters();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsNamedParameters();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsMultipleOpenResults() throws SQLException
	{
		try
		{
			return _.supportsMultipleOpenResults();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsMultipleOpenResults();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsGetGeneratedKeys() throws SQLException
	{
		try
		{
			return _.supportsGetGeneratedKeys();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsGetGeneratedKeys();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getSuperTypes(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getSuperTypes(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSuperTypes(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getSuperTables(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getSuperTables(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSuperTables(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsResultSetHoldability(int a) throws SQLException
	{
		try
		{
			return _.supportsResultSetHoldability(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsResultSetHoldability(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getResultSetHoldability() throws SQLException
	{
		try
		{
			return _.getResultSetHoldability();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getResultSetHoldability();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getDatabaseMajorVersion() throws SQLException
	{
		try
		{
			return _.getDatabaseMajorVersion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getDatabaseMajorVersion();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getDatabaseMinorVersion() throws SQLException
	{
		try
		{
			return _.getDatabaseMinorVersion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getDatabaseMinorVersion();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getJDBCMajorVersion() throws SQLException
	{
		try
		{
			return _.getJDBCMajorVersion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getJDBCMajorVersion();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getJDBCMinorVersion() throws SQLException
	{
		try
		{
			return _.getJDBCMinorVersion();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getJDBCMinorVersion();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public int getSQLStateType() throws SQLException
	{
		try
		{
			return _.getSQLStateType();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getSQLStateType();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean locatorsUpdateCopy() throws SQLException
	{
		try
		{
			return _.locatorsUpdateCopy();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.locatorsUpdateCopy();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsStatementPooling() throws SQLException
	{
		try
		{
			return _.supportsStatementPooling();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsStatementPooling();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public RowIdLifetime getRowIdLifetime() throws SQLException
	{
		try
		{
			return _.getRowIdLifetime();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getRowIdLifetime();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException
	{
		try
		{
			return _.supportsStoredFunctionsUsingCallSyntax();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.supportsStoredFunctionsUsingCallSyntax();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException
	{
		try
		{
			return _.autoCommitFailureClosesAllResultSets();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.autoCommitFailureClosesAllResultSets();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getClientInfoProperties() throws SQLException
	{
		try
		{
			return _.getClientInfoProperties();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getClientInfoProperties();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getFunctions(String a, String b, String c) throws SQLException
	{
		try
		{
			return _.getFunctions(a, b, c);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getFunctions(a, b, c);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getFunctionColumns(String a, String b, String c, String d) throws SQLException
	{
		try
		{
			return _.getFunctionColumns(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getFunctionColumns(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getPseudoColumns(String a, String b, String c, String d) throws SQLException
	{
		try
		{
			return _.getPseudoColumns(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getPseudoColumns(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean generatedKeyAlwaysReturned() throws SQLException
	{
		try
		{
			return _.generatedKeyAlwaysReturned();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.generatedKeyAlwaysReturned();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public String getURL() throws SQLException
	{
		try
		{
			return _.getURL();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getURL();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		return conn;
	}

	@Override
	public boolean isReadOnly() throws SQLException
	{
		try
		{
			return _.isReadOnly();
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.isReadOnly();
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public ResultSet getAttributes(String a, String b, String c, String d) throws SQLException
	{
		try
		{
			return _.getAttributes(a, b, c, d);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.getAttributes(a, b, c, d);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public boolean isWrapperFor(Class a) throws SQLException
	{
		try
		{
			return _.isWrapperFor(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.isWrapperFor(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}

	@Override
	public Object unwrap(Class a) throws SQLException
	{
		try
		{
			return _.unwrap(a);
		}
		catch(SQLException e)
		{
			for(int i=1;i<conn.att;i++)
			{
				recreate();
				try
				{
					return _.unwrap(a);
				}
				catch(SQLException e2)
				{
					continue;
				}
			}
			throw e;
		}
	}
}