package eu.javaexperience.collection.map;

import java.util.Map;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class PrettyPrintMap<K, V> extends MapWrapper<K, V>
{
	protected GetBy1<String, Map> printer;

	public PrettyPrintMap(Map<K, V> map)
	{
		this(map, PRINT_CONTENT_MULTILINE);
	}
	
	public PrettyPrintMap(Map<K, V> map, GetBy1<String, Map> printer)
	{
		super(map);
		this.printer = printer;
	}
	
	@Override
	public String toString()
	{
		return printer.getBy(this);
	}
	
	public static final GetBy1<String, Map> PRINT_CONTENT_MULTILINE = new GetBy1<String, Map>()
	{
		@Override
		public String getBy(Map a)
		{
			return MapTools.toStringMultiline(a);
		}
	};
}
