package eu.javaexperience.csv;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import eu.javaexperience.datatable.DataTableStructure;
import eu.javaexperience.interfaces.simple.getBy.GetBy3;
import eu.javaexperience.text.StringTools;

public class CSVTools
{
	/**
	 * Visszatér az oszlop sorszámával.
	 * 
	 * A	=> 1
	 * B	=> 2
	 * C	=> 3
	 * Z	=> 25
	 * AA	=> 26
	 * AB	=> 27
	 * AZ	=> 51
	 * BA	=> 52
	 * */
	public static int getIndexByExcelColumn(String column_name)
	{
		if(null == column_name)
		{
			return -1;
		}
		
		column_name = column_name.toUpperCase();
		
		int ret = 0;
		int w = 0;
		for(int i=column_name.length()-1;i > -1;--i,++w)
		{
			char c = column_name.charAt(i);
			if('A' <= c && c <= 'Z')
			{
				ret += (int) (Math.pow(26, w)*(c - 'A' +1));
			}
			else
			{
				return -1;
			}
		}
		
		return ret-1;
	}
	
	private static final char[] increment(char[] in, int index)
	{
		if(in.length == 0)
		{
			return new char[]{'A'};
		}
		
		if(in[index] == 'Z')
		{
			in[index] = 'A';
			
			if(index == 0)
			{
				char[] ret = new char[in.length+1];
				ret[0] = 'A';
				for(int i=0;i<in.length;++i)
				{
					ret[i+1] = in[i];
				}
				return ret;
			}
			else
			{
				return increment(in, index-1);
			}
		}
		else
		{
			++in[index];
		}
		
		return in;
	}
	
	public static String[] generateColHeader(int n)
	{
		String[] ret = new String[n];
		
		char[] val = new char[0];
		for(int i=0;i<n;++i)
		{
			val = increment(val, val.length-1);
			ret[i] = new String(val);
		}
		
		return ret;
	}
	
	public static String removeUnnecessaryQuote(String str, char quote)
	{
		if(str == null)
			return null;
		
		if (str.startsWith("\uFEFF"))
			str = str.substring(1);

		if(str.indexOf(quote) == 0)
			str = str.substring(1);

		if(str.lastIndexOf(quote) == str.length()-1)
		{
			if(str.length() < 2)
				return "";
			else
				str = str.substring(0,str.length()-1);
		}
		
		return str;
	}
	
	public static void examinePrefixColumns(Collection<String> dst, Map<String, String> row, String prefix)
	{
		for(Entry<String, String> kv:row.entrySet())
		{
			if(kv.getKey().startsWith(prefix))
			{
				String val = kv.getValue();
				if(!StringTools.isNullOrTrimEmpty(val))
				{
					dst.add(val);
				}
			}
		}
	}
	
	public static final GetBy3<String, Object[], Integer, String> DATATABLE_ARRAY_EXAMINER = new GetBy3<String, Object[], Integer, String>()
	{
		@Override
		public String getBy(Object[] a, Integer b, String c)
		{
			return StringTools.toStringOrNull(a[b]);
		}
	};
	
	public static final String[] HEADER_READ_COLUMN_NAMES = generateColHeader(50);
	
	public static void writeDatatableToFile
	(
		String dstFile,
		DataTableStructure<Object[]> src
	)
		throws IOException
	{
		writeDatatableToFile
		(
			dstFile,
			src,
			DATATABLE_ARRAY_EXAMINER
		);
	}
	
	public static <T> void writeDatatableToFile
	(
		String dstFile,
		DataTableStructure<T> src,
		GetBy3<String, T, Integer, String> tableEntryExamine
	)
		throws IOException
	{
		try(PrintWriter pw = new PrintWriter(dstFile))
		{
			SimpleCSVOutput out = new SimpleCSVOutput(pw);
			String[] rowNames = src.getRowNames();
			
			out.putRow(rowNames);
			
			String[] drow = new String[rowNames.length];
			for(T t:src.getDataCursor())
			{
				for(int i=0;i<rowNames.length;++i)
				{
					drow[i] = tableEntryExamine.getBy(t, i, rowNames[i]);
				}
				
				out.putRow(drow);
			}
			
			pw.flush();
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println(getIndexByExcelColumn("A"));
		System.out.println(getIndexByExcelColumn("B"));
		System.out.println(getIndexByExcelColumn("C"));
		System.out.println(getIndexByExcelColumn("Z"));
		System.out.println(getIndexByExcelColumn("AA"));
		System.out.println(getIndexByExcelColumn("AZ"));
		System.out.println(getIndexByExcelColumn("BA"));
		
		String[] cols = generateColHeader(1000);
		for(String s:cols)
		{
			System.out.println(s);
		}
	}
}