package eu.javaexperience.io;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

public class IpAddressTools
{
	/**
	 * returns with the first parsed IP address as integer
	 * the method assume that there's only one ip address in the string
	 * if there's no IP the address the method returns -1 
	 * 
	 * */
	public static int parseIPv4AsInt(String line)
	{
		/**
		 * n.n.n.n
		 * nnn.nnn.nnn.nnn
		 * */
		
		int d1 = -1;
		int d2 = -1;
		int d3 = -1;
		
		d1 = line.indexOf('.', 1);
		if(-1 == d1)
		{
			return -1;
		}
		
		//least one number
		d2 = line.indexOf('.', d1+2);
		
		if(-1 == d2)
		{
			return -1;
		}
		
		d3 = line.indexOf('.', d2+2);
		
		if(-1 == d3)
		{
			return -1;
		}
		
		int start = -1;

		int tmp = 0;
		for(int i=d1-1;i>=0 && i >= d1-3;--i)
		{
			tmp = line.charAt(i) - '0';
			if(tmp >= 0 && tmp < 10)
			{
				start = i;
			}
			else
			{
				break;
			}
		}
		
		if(-1 == start)
		{
			return -1;
		}
		
		int ip = 0;
		
		tmp = parseIpv4Int(line, start);
		if(-1 == tmp)
		{
			return -1;
		}
		
		ip |= tmp << 24;
		
		tmp = parseIpv4Int(line, d1+1);
		
		if(-1 == tmp)
		{
			return -1;
		}
		
		ip |= tmp << 16; 
		
		tmp = parseIpv4Int(line, d2+1);
		
		if(-1 == tmp)
		{
			return -1;
		}
		
		ip |= tmp << 8;
		
		tmp = parseIpv4Int(line, d3+1);
		
		if(-1 == tmp)
		{
			return -1;
		}
		
		ip |= tmp;
		return ip;
	}
	
	protected static int parseIpv4Int(String str, int index)
	{
		int ret = 0;
		int tmp = 0;
		int end = index+3;
		if(str.length() < end)
		{
			end = str.length();
		}
		for(int i=index;i<end;++i)
		{
			tmp = str.charAt(i) - '0';
			if(tmp < 0 || tmp > 9)
			{
				if(index == i)
				{
					return -1;
				}
				else
				{
					return ret;
				}
			}
			
			ret *= 10;
			ret += tmp;
		}
		return ret;
	}
	
	public static void fillIpv4(int ip, short[] address)
	{
		address[0] = (short) ((ip >> 24) & 0xff);
		address[1] = (short) ((ip >> 16) & 0xff);
		address[2] = (short) ((ip >> 8) & 0xff);
		address[3] = (short) (ip & 0xff);
	}
	
	public static String getIpAddress(int ip)
	{
		short[] addr = new short[4];
		fillIpv4(ip, addr);
		return addr[0]+"."+addr[1]+"."+addr[2]+"."+addr[3];
	}
	
	protected static void test(String line)
	{
		int ip = parseIPv4AsInt(line);
		short[] nums = new short[4];
		fillIpv4(ip, nums);
		System.out.println("IPv4: "+nums[0]+"."+nums[1]+"."+nums[2]+"."+nums[3]);
	}
	
	public static List<InetAddress> getHostIpAddresses() throws SocketException
	{
		ArrayList<InetAddress> addrs = new ArrayList<InetAddress>();
		Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
		while(e.hasMoreElements())
		{
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration<InetAddress> ee = n.getInetAddresses();
			while (ee.hasMoreElements())
			{
				addrs.add(ee.nextElement());
			}
		}
		
		return addrs;
	}
	
	protected static final Pattern NAIVE_IP_MATCH = Pattern.compile("^(\\d{1,3}\\.){3,3}\\d{1,3}$");
	
	public static boolean isIpv4(String str)
	{
		return NAIVE_IP_MATCH.matcher(str).find();
	}
	
	//https://stackoverflow.com/questions/2939218/getting-the-external-ip-address-in-java
	protected static final String[] RAW_IP_PROBES_URLS = new String[]
	{
		"http://checkip.amazonaws.com/",
		"http://myexternalip.com/raw",
		"http://icanhazip.com/",
		"http://www.trackip.net/ip",
		"http://ipecho.net/plain",
		"http://bot.whatismyipaddress.com/",
	};
	
	public static String getPublicIp()
	{
		for(String s:RAW_IP_PROBES_URLS)
		{
			try
			{
				URL u = new URL(s);
				URLConnection conn = u.openConnection();
				String str = new String(IOTools.loadAllFromInputStream(conn.getInputStream()));
				str = str.trim();
				if(isIpv4(str))
				{
					return str;
				}
			}
			catch(Exception e)
			{}
		}
		
		return "127.0.0.1";
	}
	
	public static void main(String[] args) throws Throwable
	{
		/*test("0.0.0.0");
		test("255.255.0.255");
		test("[ip address: 0.0.0.0");*/
		//System.out.println(getPublicIp());
		for(InetAddress addr:getHostIpAddresses())
		{
			if(addr instanceof Inet4Address)
			{
				System.out.println(addr.getHostAddress());
			}
		}
	}
	
	public static boolean isReachable(InetAddress addr, int timeout) throws IOException
	{
		return addr.isReachable(timeout);
	}
	
	public static boolean isReachable(String addr, int timeout) throws IOException
	{
		return InetAddress.getByName(addr).isReachable(timeout);
	}
}
