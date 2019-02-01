package eu.javaexperience.datatable;

public interface DataTableStructure<S>
{
	public String[] getRowNames();
	public Iterable<S> getDataCursor();
}
