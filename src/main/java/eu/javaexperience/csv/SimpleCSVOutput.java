package eu.javaexperience.csv;

import java.io.IOException;

public class SimpleCSVOutput
{
	protected final Appendable sb;
	
	protected char separator = ',';
	
	protected boolean escape_linebreaks = true;
	
	public boolean isEscapeLinebreaks()
	{
		return escape_linebreaks;
	}

	public void setEscapeLinebreaks(boolean escape_linebreaks)
	{
		this.escape_linebreaks = escape_linebreaks;
	}
	
	public SimpleCSVOutput(Appendable out)
	{
		this.sb = out;
	}
	
	protected int ep = 0;
	
	public void putRow(String... arguments) throws IOException
	{
		if(ep++ > 0)
			sb.append("\r\n");
		
		for(int i=0;i<arguments.length;i++)
		{
			if(i != 0)
				sb.append(separator);
			
			escapeCsv(arguments[i]);
		}
	}
	
	/**
	 * http://grepcode.com/file/repo1.maven.org/maven2/commons-lang/commons-lang/2.5/org/apache/commons/lang/StringEscapeUtils.java#StringEscapeUtils.escapeCsv%28java.io.Writer%2Cjava.lang.String%29
	 * tudod ki fogja az egész libet behúzni...
	 * @param str
	 * @throws IOException
	 */
	protected void escapeCsv(String str) throws IOException
	{
		if(null == str)
		{
			str = "";
		}
		
		sb.append("\"");
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			if(escape_linebreaks)
			{
				if(c == '\n')
				{
					sb.append("\\n");
					continue;
				}
				
				else if(c == '\r')
				{
					sb.append("\\r");
					continue;
				}
			}
			
			if (c == '"')
			{
				sb.append("\"\"");
				continue;
			}
			
			sb.append(c);
		}
		sb.append("\"");
	}
	
	@Override
	public String toString()
	{
		return "SimpleCsvOutput: "+sb.toString();
	}
	
	public Appendable getBackendAppendable()
	{
		return sb;
	}

	public char getSeparator()
	{
		return separator;
	}

	public void setSeparator(char separator)
	{
		this.separator = separator;
	}
}