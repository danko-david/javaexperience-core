package eu.javaexperience.eval;

import java.io.IOException;

import eu.javaexperience.reflect.Mirror;
import eu.javaexperience.reflect.RuntimeClass;
import eu.javaexperience.text.Format;
import eu.javaexperience.text.StringTools;

public class InjectCode
{
	public static String getClassBytecodeBase64(Class cls) throws IOException
	{
		byte[] data = Mirror.getClassBytecode(cls);
		return Format.base64Encode(data);
	}
	
	public static <T> Class<T> getCodeInstance(String prg)
	{
		RuntimeClass dl = new RuntimeClass();
		String re = prg.trim();
		int padding = re.length() % 4; 
		if(padding > 0)
		{
			re += StringTools.repeatChar('=', padding);
		}
		return (Class<T>) dl.loadClass(Format.base64Decode(re));
	}
}