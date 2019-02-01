package eu.javaexperience.csv;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Map;

import eu.javaexperience.collection.map.SmallMap;
import eu.javaexperience.reflect.Mirror;

/**
 *	Ahhol ; a delimiter és nincsenek kvórátva a karakterek
 * 
 * */
public class SillyCSVFormatReader implements AutoCloseable, Closeable
{
	protected BufferedReader br;
	protected InputStream is;
	
	protected String[] rows;
	
	protected final char quote;
	protected final char delimiter;
	protected final String strQuote;
	
	protected final URL url;
	protected final Charset charset;
	
	public SillyCSVFormatReader(File file,char quote,char delimiter, Charset charset, String[] rows) throws MalformedURLException
	{
		this(new URL("file://"+file.toString()), quote, delimiter, charset, rows);
	}
	
	public SillyCSVFormatReader(URL url,char quote,char delimiter, Charset charset, String[] rows)
	{
		this.charset = charset;
		this.url = url;
		this.rows = rows;
		this.quote = quote;
		strQuote = new String(new char[]{quote});
		this.delimiter = delimiter;
	}
	
	public void init() throws IOException
	{
		URLConnection conn = url.openConnection();
		is = conn.getInputStream();
		if(charset == null)
			br = new BufferedReader(new InputStreamReader(is));
		else
			br = new BufferedReader(new InputStreamReader(is,charset));
	}
	
	public Map<String,String> getRow()
	{
		SmallMap<String, String> ret = new SmallMap<>();
		String line = null;
		try
		{
			line = br.readLine();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		
		if(getRow(line, rows, ret, quote, delimiter))
		{
			return ret;
		}
		else
		{
			return null;
		}
	}
	
	public static boolean getRow
	(
		String line,
		String[] rows,
		Map<String,String> ret,
		char quote,
		char delimiter
	)
	{
		if(line == null)
		{
			return false;
		}
		
		/**
		 * Kvóta van ha:
		 * 	- üres a string: ;"";
		 * 	- van venne whitespace: ;"1 dukát 1870 Ferenc József rozettás UV !";
		 * Nincs:
		 * 	- ha nincs benne whitespace karakter: ;Pécs;
		 * 
		 * Megyünk végig:
		 * ha van kvótázó karakter:
		 * 	- és utána delimiter következik akkor új string kezdődik
		 * különben folyatódik a string a kvótával beleértve  
		 * 
		 * 
		 * */
		int unit = 0;
		int poz = 0;
		
		StringBuilder sb = new StringBuilder();
		
		boolean prev_delim = false;
		boolean prev_quote = false;
		
		/**
		 * ha delim_eou = false akkor csak delimiter-t keresünk a végén, ha true akkor quote+delimiter-t
		 */
		boolean delim_eou = false;
		
		//egységek olvasása
		while(unit<rows.length&& poz < line.length())
		{
			char at = line.charAt(poz++);
			
			//akkor kezdünk újat ha ha ;"-vel kezdődött és "; találtunk
			//vagy csak ;-t találtunk és ;-t találunk

			if(at == quote)
			{
				//;" de csak akkor ha nem vagyunk a string közepében akkor "; a lezáró string

				//ha az előző ; volt, most egy " állunk és most kezdődik a string akkor ";-vel fog záródni a cella
				if(prev_quote && sb.length() > 0)
				{
					sb.append(quote);
					prev_delim = false;
					prev_quote = false;
					continue;
				}
				
				if(/*prev_delim &&*/ sb.length() == 0)
				{
					delim_eou = true;
				}
				else if(prev_quote)
				{
					sb.append(quote);
				}
				
				prev_delim = false;
				prev_quote = true;
				
				continue;
			}
			else if(at == delimiter)//;
			{
				//"; volt és delim_eou (avagy ";-val zárul a sor) vagy csak ;-vel kezdődött akkor vége az egységnek
				if((prev_quote && delim_eou) || !delim_eou)
				{
					ret.put(rows[unit++], CSVTools.removeUnnecessaryQuote(sb.toString(), quote));
					sb = new StringBuilder();
					prev_quote = false;
					prev_delim = false;
					delim_eou = false;
				}
				else
				{
					sb.append(delimiter);
				}
				
				prev_quote = false;
				prev_delim = true;
				
				continue;
			}
			else
			{
				//; és most nem " van akkor
				if(prev_delim && sb.length() == 0)
					delim_eou = false;
				
				sb.append(at);
			}
			
			/*if(sb.indexOf("<p>") == 0)
				System.out.println("");
			*/
			
			prev_delim = false;
			prev_quote = false;
		}
		
		//ha van String kimenet akkor a vége nem lett tisztán lezárva vagy tőbb oszlop van ebben a sorban mint a megadott fejléc
		if(unit < rows.length)
		{
			ret.put(rows[unit++], CSVTools.removeUnnecessaryQuote(sb.toString(), quote));
		}
		
		return true;
	}
	
	public void close() throws IOException
	{
		br.close();
		is.close();
	}

	public void setRowNames(String[] ret)
	{
		rows = ret;
	}
	
	public void readHeaders()
	{
		rows = CSVTools.HEADER_READ_COLUMN_NAMES;
		Map<String, String> row = getRow();
		this.rows = row.values().toArray(Mirror.emptyStringArray);
	}
}