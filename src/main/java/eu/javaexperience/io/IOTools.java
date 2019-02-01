package eu.javaexperience.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;

import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.interfaces.simple.SimpleCall;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.semantic.references.MayNotModified;
import eu.javaexperience.text.StringTools;

public class IOTools
{
	static final @MayNotModified byte[] unixLineFeed = new byte[]{'\n'};
	static final @MayNotModified byte[] windowsLineFeed = new byte[]{'\r'};
	static final @MayNotModified byte[] macLineFeed = new byte[]{'\r','\n'};
	
	public static byte[] loadFileContent(String file) throws IOException
	{
		try(FileInputStream fis = new FileInputStream(file);)
		{
			byte[] ret = loadAllAvailableFromInputStream(fis);
			return ret;
		}
	}
	
	public static byte[] loadFileContent(AbstractFile file) throws IOException
	{
		try(InputStream fis = file.openRead())
		{
			return loadAllFromInputStream(fis);
		}
	}
	
	public static final OutputStream nullOutputStream = new OutputStream()
	{
		@Override public void write(int arg0) throws IOException{}
		@Override public void write(byte[] b) throws IOException{}
		@Override public void write(byte[] b,int i,int a) throws IOException{}
	};
	
	public static Writer nullWriter = new Writer()
	{
		@Override public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)throws IOException{}
		@Override public void flush() throws IOException{}
		@Override public void close() throws IOException{}
	};
	
	public static PrintWriter nullPrintWriter = new PrintWriter(nullOutputStream);
	
	public static final InputStream nullInputStream = new InputStream()
	{
		@Override public int read() throws IOException{return -1;}
	};

	public static byte[] loadFileProcContent(String file) throws IOException
	{
		try(FileInputStream fis = new FileInputStream(file);)
		{
			byte[] ret = loadAllFromInputStream(fis);
			return ret;
		}
	}
	
	public static byte[] getBytesOfFile(String file, int start, int end) throws FileNotFoundException, IOException
	{
		if(end<= start)
			return new byte[0];
		
		try(FileInputStream fis = new FileInputStream(file))
		{
			if(end > fis.available())
				end = fis.available();
			
			if(end <= start)
				return new byte[0];
			
			byte[] ret = new byte[end-start];
			
			fis.skip(start);
			int ep = 0;
			int read = 0;
			while((read = fis.read(ret, ep, ret.length-ep))>0)
				ep+= read;
			
			return ret;
		}
	}
	
	public static byte[] loadFromStream(InputStream is,int len) throws FileNotFoundException, IOException
	{
		if(len <= 0)
			return new byte[0];
		
		byte[] ret = new byte[len];
		
		int ep = 0;
		int read = 0;
		while((read = is.read(ret, ep, ret.length-ep))>0)
			ep+= read;
		
		return ret;
	}
	
	public static void putFileContent(String file, byte[] data) throws IOException
	{
		try(FileOutputStream fos = new FileOutputStream(file))
		{
			fos.write(data);
			fos.flush();
		}
	}
	
	
	public static void putFileContent(String file, boolean append, byte[] data) throws IOException
	{
		try(FileOutputStream fos = new FileOutputStream(file, append))
		{
			fos.write(data);
			fos.flush();
		}
	}
	
	public static void putFileContents(String file, boolean append, byte[]... datas) throws FileNotFoundException, IOException
	{
		File f = new File(file);
		if(!f.exists())
			f.createNewFile();
		try(FileOutputStream fos = new FileOutputStream(file,append))
		{
			for(byte[] data:datas)
				fos.write(data);
			
			fos.flush();
		}
	}
	
	/**
	 * Beolvas mindent a bejövő adatfolyamról az {@link InputStream#available()} metódus segítségével.
	 * Hatékonyabb mint a {@link #loadAllFromInputStream(InputStream)},
	 * lehet hogy 1 olvasással végez, viszton az {@link InputStream#available()} nem mindig elérhtő.
	 * Nem zárja be az adatfolyamot!
	 * */
	public static byte[] loadAllAvailableFromInputStream(InputStream is) throws IOException
	{
		int ep = 0;
		byte[] ret = new byte[is.available()];
		
		while(is.available() > 0)
		{
			if(ep == ret.length)
				ret = Arrays.copyOf(ret, ret.length*2);
			
			ep += is.read(ret, 0, ret.length-ep);
		}

		return Arrays.copyOf(ret, ep);
	}

	/**
	 * Beolvas mindent a bejövő adatfolyamról, az available használata nélkül.
	 * Lassabb de múködése biztosabb, van hogy az {@link InputStream#available()} kivételt dob.
	 * Nem zárja be az adatfolyamot!
	 * */
	public static byte[] loadAllFromInputStream(InputStream is) throws IOException
	{
		int ep = 0;
		int read = 0;
		byte[] ret = new byte[4096];
		
		while((read = is.read(ret, ep, ret.length-ep))>0)
		{
			if(ep + read == ret.length)
				ret = Arrays.copyOf(ret, ret.length*2);
			
			ep+= read;
		}

		return Arrays.copyOf(ret, ep);
	}
	
	public static byte[] loadAllFromInputStream(InputStream is, int startBuffer) throws IOException
	{
		int ep = 0;
		int read = 0;
		byte[] ret = new byte[startBuffer];
		
		while((read = is.read(ret, ep, ret.length-ep))>0)
		{
			if(ep + read == ret.length)
				ret = Arrays.copyOf(ret, ret.length*2);
			
			ep+= read;
		}

		return Arrays.copyOf(ret, ep);
	}
	
	public static int copyStream(InputStream is,OutputStream os) throws IOException
	{
		int w = 0;
		byte[] buff = new byte[4096];
		int cr = 0;
		while((cr = is.read(buff))> -1)
		{
			os.write(buff, 0, cr);
			w += cr;
		}
		
		return w;
	}
	
	public static int copyStream(InputStream is,OutputStream os, byte[] buff) throws IOException
	{
		int w = 0;
		int cr = 0;
		while((cr = is.read(buff))> -1)
		{
			os.write(buff, 0, cr);
			w += cr;
		}
		
		return w;
	}
	
	/**
	 * returns the count of copied bytes, returns -1* copied byte if limit reached
	 * */
	public static int copyStream(InputStream is,OutputStream os, byte[] buff, int limit) throws IOException
	{
		int w = 0;
		int cr = 0;
		while((cr = is.read(buff))> -1)
		{
			os.write(buff, 0, cr);
			w += cr;
			
			if(w >= limit)
				return -w;
		}
		
		return w;
	}

	public static void silentClose(OutputStream outputStream)
	{
		try
		{
			outputStream.close();
		}
		catch(Exception e)
		{}
	}
	
	public static void silentClose(InputStream is)
	{
		try
		{
			is.close();
		}
		catch(Exception e)
		{}
	}
	
	public static void silentClose(Closeable is)
	{
		try
		{
			is.close();
		}
		catch(Exception e)
		{}
	}
	
	public static int writeInputStreamToFile(InputStream is,String file) throws IOException
	{
		try(FileOutputStream fos = new FileOutputStream(file))
		{
			int n = copyStream(is, fos);
			fos.flush();
			return n;
		}
	}
	
	public static int writeStringToFile(String str,String file) throws IOException
	{
		byte[] d = str.getBytes();
		try(FileOutputStream fos = new FileOutputStream(file))
		{
			fos.write(d);
			fos.flush();
		}
		return d.length;
	}
	
	public static byte[] getFilePart(String file,long off,int len) throws IOException
	{
		byte[] ret = null;
		
		try(FileInputStream fis =  new FileInputStream(file))
		{
			long skip = fis.skip(off);
			while(skip != off)
			{
				long buf = fis.skip(skip-off);
				if(buf < 1)
				{
					fis.close();
					return new byte[0];
				}
				skip += buf;
			}
	
			int s = 0;
			ret = new byte[len];
			while(s != len)
			{
				int read = fis.read(ret, s, len-s);
				if(read == -1)
				{
					ret =  Arrays.copyOf(ret, s);
					break;
				}
	
				s+=read;
			}
		}catch(IOException e)
		{
			throw e;
		}
			
		return ret;
	}
	
	public static byte[] md5(String content)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes());
			return md.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			Mirror.propagateAnyway(e);
			return null;
		}
	}
	
	protected static byte[] digestWith(MessageDigest md, String file) throws IOException
	{
		try(InputStream fis = new FileInputStream(file))
		{
			byte[] data = new byte[4096];
			int read = 0;
			while(-1 < (read = fis.read(data)))
			{
				if(0 == read)
				{
					continue;
				}
				else if(data.length == read)
				{
					md.update(data);
				}
				else
				{
					md.update(Arrays.copyOf(data, read));
				}
			}
		}
		return md.digest();
	}
	
	public static byte[] fileChksumMd5(String file) throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		return digestWith(md, file);
	}
	
	public static byte[] fileChksumCrc32(String file) throws NoSuchAlgorithmException, IOException
	{
		CRC32 crc32 = new CRC32();
		
		try(InputStream fis = new FileInputStream(file))
		{
			byte[] data = new byte[4096];
			int read = 0;
			while(-1 < (read = fis.read(data)))
			{
				if(0 == read)
				{
					continue;
				}
				else if(data.length == read)
				{
					crc32.update(data);
				}
				else
				{
					crc32.update(Arrays.copyOf(data, read));
				}
			}
		}
		long val = crc32.getValue();
		return new byte[]
		{
			(byte)((val >> 24)&0xff),
			(byte)((val >> 16)&0xff),
			(byte)((val >> 8)&0xff),
			(byte)((val)&0xff),
		};
	}
	
	public static byte[] fileChksumSha256(String file) throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		return digestWith(md, file);
	}
	
	
	public static void loadFillAllLine(String file, Collection<String> lines) throws FileNotFoundException, IOException
	{
		try
		(
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		)
		{
			String line = null;
			while((line = br.readLine()) != null)
			{
				lines.add(line);
			}
		}
	}

	public static String[] readAllLine(File in) throws FileNotFoundException, IOException
	{
		try
		(
			FileInputStream fis = new FileInputStream(in);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		)
		{
			ArrayList<String> re = new ArrayList<>();
			
			String line = null;
			while((line = br.readLine()) != null)
			{
				re.add(line);
			}
		
			return re.toArray(Mirror.emptyStringArray);
		}
	}
	
	public static int linesInFile(File f)
	{
		try
		(
				FileInputStream fis = new FileInputStream(f);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		)
		{
			int count = 0;
			while(br.readLine() != null)
			{
				++count;
			}
			
			return count;
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	public static void silentFlush(Flushable f)
	{
		try
		{
			f.flush();
		}
		catch(Throwable t)
		{}
	}

	/**
	 * won't close inputStream
	 * @throws IOException 
	 * */
	public static void processStreamLines(InputStream is, SimplePublish1<String> onReadLine) throws IOException
	{
		try
		(
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
		)
		{
			String line = null;
			while((line = br.readLine()) != null)
			{
				onReadLine.publish(line);
			}
		}
	}
	
	public static void processFileLines(String file, SimplePublish1<String> onReadLine) throws FileNotFoundException, IOException
	{
		try(FileInputStream fis = new FileInputStream(file))
		{
			processStreamLines(fis, onReadLine);
		}
	}

	public static String getFileContents(String string) throws FileNotFoundException, IOException
	{
		try(FileInputStream fis = new FileInputStream(string))
		{
			return new String(loadAllFromInputStream(fis));
		}
	}
	
	public static String getFileContents(File string) throws FileNotFoundException, IOException
	{
		try(FileInputStream fis = new FileInputStream(string))
		{
			return new String(loadAllFromInputStream(fis));
		}
	}
	
	public static String getFileContents(AbstractFile file) throws FileNotFoundException, IOException
	{
		try(InputStream fis = file.openRead())
		{
			return new String(loadAllFromInputStream(fis));
		}
	}

	public static byte[] getURL(URL url) throws IOException
	{
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		try
		{
			return IOTools.loadAllFromInputStream(conn.getInputStream());
		}
		finally
		{
			IOTools.silentClose(is);
		}
	}
	
	public static void feedAllLine(String file, SimplePublish1<String> feed) throws IOException
	{
		try
		(
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis);
				BufferedReader br = new BufferedReader(isr);
		) 
		{
			String line = null;
			while(null != (line = br.readLine()))
			{
				feed.publish(line);
			}
		}
	}
	
	public static SimpleCall readAllStdInLine(final SimplePublish1<String> feed)
	{
		return new SimpleCall()
		{
			@Override
			public void call()
			{
				try 
				{
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					String line = null;
					while(null != (line = br.readLine()))
					{
						feed.publish(line);
					}
				}
				catch(Exception e)
				{
					Mirror.propagateAnyway(e);
				}
			}
		};
	}

	public static String[] readAllLine(String string) throws FileNotFoundException, IOException
	{
		return readAllLine(new File(string));
	}

	public static void createPathBeforeFile(File file)
	{
		createPathBeforeFile(file.toString());
	}
	
	public static void createPathBeforeFile(String file)
	{
		if(file.endsWith("/"))
		{
			new File(file).mkdirs();
		}
		else
		{
			String path = StringTools.getSubstringBeforeLastString(file, "/", null);
			if(null != path)
			{
				new File(path).mkdirs();
			}
		}
	}
	
	public static String readLine_rn(InputStream is, byte[] buffer, int max) throws IOException
	{
		if(is.markSupported())
		{
			return readLine_rn_withMarkSupport(is, buffer, max);
		}
		
		int ep = 0;
		for(int i=0;i<max;++i)
		{
			int read = is.read();
			if(0 > read)
			{
				//no more line
				if(0 == i)
				{
					return null;
				}
				else
				{
					//returns with the line
					break;
				}
			}
			
			if('\r' == read)
			{
				//ignore
				continue;
			}
			//line feed after CR, line is ended
			else if('\n' == read)
			{
				//returns with the Line
				break;
			}
			else
			{
				buffer[ep++] = (byte) read;
			}
		}
		
		return new String(buffer, 0, ep);
	}
	
	protected static String readLine_rn_withMarkSupport(InputStream is, byte[] buffer, int max) throws IOException
	{
		is.mark(max);
		int size = is.read(buffer);
		int index = -1;
		for(int i=0;i<size;++i)
		{
			if('\n' == buffer[i])
			{
				index = i;
				break;
			}
		}
		
		if(-1 == index)
		{
			is.reset();
			return null;
		}
		
		if(0 == index)
		{
			return "";
		}
		
		is.reset();
		is.skip(index+1);
		
		return new String(buffer, 0, index-1);
	}
	
	public static void appendData(String file, byte[] data) throws IOException
	{
		try(OutputStream os = new FileOutputStream(file, true))
		{
			os.write(data);
			os.flush();
		}
	}

	public static void copyFileContentToStream(File f, OutputStream os) throws FileNotFoundException, IOException
	{
		try(InputStream is = new FileInputStream(f))
		{
			IOTools.copyStream(is, os);
		}
	}
	
	/**
	 * Origin from: java.lang.String
	 *@param   source       the characters being searched.
	 * @param   sourceOffset offset of the source string.
	 * @param   sourceCount  count of the source string.
	 * @param   target       the characters being searched for.
	 * @param   targetOffset offset of the target string.
	 * @param   targetCount  count of the target string.
	 * @param   fromIndex    the index to begin searching from.
	 */
	public static int indexOfBytes(byte[] source, int sourceOffset, int sourceCount, byte[] target, int targetOffset, int targetCount, int fromIndex)
	{
        if (fromIndex >= sourceCount)
        {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        byte first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[i] != first) {
                while (++i <= max && source[i] != first);
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                        == target[k]; j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }
	
	public static int indexOfBytes(byte[] source, byte[] target, int fromIndex, int endindex)
	{
		return indexOfBytes(source, 0, endindex, target, 0, target.length, fromIndex); 
	}

	public static int readFull(InputStream is, byte[] data) throws IOException
	{
		int off = 0;
		int read = 0;
		while(off < data.length && (read = is.read(data, off, data.length-off)) > -1)
		{
			off += read;
		}
		
		return off;
	}

	public static String tryGetContent(AbstractFile file, String _default)
	{
		try
		{
			return getFileContents(file);
		}
		catch(Exception e)
		{
			return _default;
		}
	}

	public static void ungzip(File from, File to) throws FileNotFoundException, IOException
	{
		try
		(
			InputStream is = new FileInputStream(from);
			GZIPInputStream gin = new GZIPInputStream(is);
			OutputStream os = new FileOutputStream(to);
		)
		{
			IOTools.copyStream(gin, os);
			os.flush();
		}
	}
}
