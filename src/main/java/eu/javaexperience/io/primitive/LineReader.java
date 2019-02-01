package eu.javaexperience.io.primitive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


public class LineReader
{
	protected final Reader read;
	
	
	public LineReader(Reader read)
	{
		this.read = read;
	}

	public static enum LineMode
	{
		Unix('\n'),
		Windows('\r'),
		Mac('\0');
		
		private final char le;
		
		private LineMode(char c)
		{
			le = c;
		}		
	}
	
	public static String readLine(Reader reader,LineMode mode) throws IOException
	{
		StringBuilder ret = new StringBuilder();
		char[] buf = new char[1];
		//lehet \r vagy \n vagy \r\n
		ok:{
			switch (mode)
			{
			case Mac:
				while(true)
				{
					int re = reader.read(buf);
					if(re == 1)
					{
						if(buf[0] == '\n')
							break ok;
						else if(buf[0] == '\r')
							continue;
						else
							ret.append(buf[0]);
					}
					else if(re < 1)
					{
						if(re < 0 && ret.length() == 0)
							return null;
						break ok;
					}
				}

			case Unix:
			case Windows:
				while(true)
				{
					int re = reader.read(buf);
					if(re == 1)
					{
						if(buf[0] == mode.le)
							break ok;
						else
							ret.append(buf[0]);
					}
					else if(re < 1)
					{
						if(re < 0 && ret.length() == 0)
							return null;
						break ok;
					}
				}
			default:
				throw new IllegalArgumentException("No line mode");
			}
		}
		return ret.toString();
	}
	
	public static String readLine(InputStream reader,LineMode mode) throws IOException
	{
		StringBuilder ret = new StringBuilder();
		byte[] buf = new byte[1];
		//lehet \r vagy \n vagy \r\n
		ok:{
			switch (mode)
			{
			case Mac:
				while(true)
				{
					int re = reader.read(buf);
					if(re == 1)
					{
						if(buf[0] == '\n')
							break ok;
						else if(buf[0] == '\r')
							continue;
						else
							ret.append((char)buf[0]);
					}
					else if(re < 1)
					{
						if(re < 0 && ret.length() == 0)
							return null;
						break ok;
					}
				}

			case Unix:
			case Windows:
				while(true)
				{
					int re = reader.read(buf);
					if(re == 1)
					{
						if(buf[0] == mode.le)
							break ok;
						else
							ret.append((char)buf[0]);
					}
					else if(re < 1)
					{
						if(re < 0 && ret.length() == 0)
							return null;
						break ok;
					}
				}
			default:
				throw new IllegalArgumentException("No line mode given");
			}
		}
		return ret.toString();
	}
	
	public static byte[] readByteLine(InputStream reader,LineMode mode) throws IOException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buf = new byte[1];
		//lehet \r vagy \n vagy \r\n
		ok:{
			switch (mode)
			{
			case Mac:
				while(true)
				{
					int re = reader.read(buf);
					if(re == 1)
					{
						if(buf[0] == '\n')
							break ok;
						else if(buf[0] == '\r')
							continue;
						else
							out.write(buf[0]);
					}
					else if(re < 1)
					{
						if(re < 0 && out.size() == 0)
							return null;
						break ok;
					}
				}

			case Unix:
			case Windows:
				while(true)
				{
					int re = reader.read(buf);
					if(re == 1)
					{
						if(buf[0] == mode.le)
							break ok;
						else
							out.write(buf[0]);
					}
					else if(re < 1)
					{
						if(re < 0 && out.size() == 0)
							return null;
						break ok;
					}
				}
			default:
				throw new IllegalArgumentException("No line mode given");
			}
		}
		
		return out.toByteArray();
	}
	
	public String readLine() throws IOException
	{
		return readLine(read,LineMode.Unix);
	}
	
	
	
}