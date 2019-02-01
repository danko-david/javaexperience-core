package eu.javaexperience.url;

import java.net.URL;

import eu.javaexperience.arrays.ArrayTools;

public enum UrlPart implements UrlPartGetter
{
	PROTOCOL()
	{
		@Override
		public String getBy(URL url)
		{
			return url.getProtocol();
		}
	},
	
	USERNAME
	{
		@Override
		public String getBy(URL url)
		{
			String info = url.getUserInfo();
			if(null == info)
			{
				return null;
			}
			return ArrayTools.extractFirst(info.split(":"));
		}
	},
	
	PASSWORD
	{
		@Override
		public String getBy(URL url)
		{
			String info = url.getUserInfo();
			if(null == info)
			{
				return null;
			}
			return ArrayTools.accessIndexSafe(info.split(":"), 1);
		}
	},
	
	HOST
	{
		@Override
		public String getBy(URL url)
		{
			return url.getHost();
		}
	},
	
	CURRENT_PORT
	{
		@Override
		public String getBy(URL url)
		{
			int crnt = url.getPort();
			if(crnt < 0)
			{
				crnt = url.getDefaultPort();
			}
			
			if(crnt < 0)
			{
				return null;
			}
			
			return String.valueOf(crnt);
		}
	},
	
	PORT
	{
		@Override
		public String getBy(URL url)
		{
			return String.valueOf(url.getPort());
		}
	},
	
	DEFAULT_PORT
	{
		@Override
		public String getBy(URL url)
		{
			return String.valueOf(url.getDefaultPort());
		}
	},
	
	PATH
	{
		@Override
		public String getBy(URL url)
		{
			return url.getPath();
		}
	},
	
	QUERY
	{
		@Override
		public String getBy(URL url)
		{
			return url.getQuery();
		}
	},
	
	REFERENCE
	{
		@Override
		public String getBy(URL url)
		{
			return url.getRef();
		}
	}	
	;
}