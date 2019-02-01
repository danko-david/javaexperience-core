package eu.javaexperience.datatable;

import eu.javaexperience.collection.iterator.FilteredIterator;
import eu.javaexperience.collection.iterator.IteratorTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class DataTableTools
{
	public static <T> DataTableStructure<T> filteredDataTable
	(
		final DataTableStructure<T> src,
		final GetBy1<Boolean, T> filter
	)
	{
		return new DataTableStructure<T>()
		{
			@Override
			public String[] getRowNames()
			{
				return src.getRowNames();
			}

			@Override
			public Iterable<T> getDataCursor()
			{
				return IteratorTools.wrapIterator(new FilteredIterator<T>(src.getDataCursor().iterator(), filter));
			}
		};
	}
}
