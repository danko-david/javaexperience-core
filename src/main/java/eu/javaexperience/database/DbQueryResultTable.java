package eu.javaexperience.database;

import java.util.ArrayList;

import eu.javaexperience.datatable.DataTableStructure;

public class DbQueryResultTable implements DataTableStructure<Object[]>
{
	public String[] columns;
	public ArrayList<Object[]> rows = new ArrayList<>();
	
	@Override
	public String[] getRowNames()
	{
		return columns;
	}
	@Override
	public Iterable<Object[]> getDataCursor()
	{
		return rows;
	}
}
