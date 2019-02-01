package eu.javaexperience.io.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.reflect.Mirror;

public class LineFilter
{
	protected final File file;
	protected final GetBy1<Boolean, String> filter;
	
	public LineFilter(File file, GetBy1<Boolean, String> filter)
	{
		this.file = file;
		this.filter = filter;
	}
	
	public static LineFilter withRegex(File file, String regex)
	{
		final Pattern p = Pattern.compile(regex);
		return new LineFilter(file, new GetBy1<Boolean, String>()
		{
			@Override
			public Boolean getBy(String a)
			{
				return p.matcher(a).matches();
			}
		});
	}
	
	public void filterLines(Collection<String> dst) throws IOException
	{
		try
		(
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis))
		)
		{
			String line = null;
			while(null != (line = br.readLine()))
			{
				if(Boolean.TRUE == filter.getBy(line))
				{
					dst.add(line);
				}
			}
		}
	}
	
	public String[] filterLines() throws IOException
	{
		ArrayList<String> ret = new ArrayList<>();
		filterLines(ret);
		return ret.toArray(Mirror.emptyStringArray);
	}
}
