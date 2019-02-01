package eu.javaexperience.url;

import java.net.URLDecoder;

public class UrlTools
{
	public static void modifyUrlDecode(String[] arr)
	{
		for(int i= 0;i<arr.length;i++)
			arr[i] = URLDecoder.decode(arr[i]);
	}
}
