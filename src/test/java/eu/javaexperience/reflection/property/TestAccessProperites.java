package eu.javaexperience.reflection.property;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.interfaces.simple.publish.SimplePublish1;
import eu.javaexperience.reflect.property.PropertyAccessTools;

public class TestAccessProperites
{
	public static interface ServletHandler {};
	
	public static class PathHandler implements ServletHandler
	{
		public String path;
		public SimplePublish1<Object> handler;
		
		public PathHandler(String path, SimplePublish1<Object> handler)
		{
			this.path = path;
			this.handler = handler;
		}
	}
	
	public static interface Service {};
	
	public static class WebServerConfig implements Service
	{
		public String protocol;
		public int port;
		public List<ServletHandler> handlers;
		
		public WebServerConfig(String protocol, int port, ServletHandler... handlers)
		{
			this.protocol = protocol;
			this.port = port;
			this.handlers = CollectionTools.inlineArrayList(handlers);
		}
	}
	
	public static List<Service> createServices()
	{
		List<Service> ret = new ArrayList<>();
		
		ret.add(new WebServerConfig
		(
			"http",
			8080,
			new PathHandler("/", null),
			new PathHandler("/site", null),
			new PathHandler("/about", null),
			new PathHandler("/blog", null)
		));
		
		return ret;
	}
	
	@Test
	public void testAccessConfig()
	{
		List<Service> cfg = createServices();
		Map<String, Object> acc = PropertyAccessTools.dotAccessWrap(PropertyAccessTools.wrap(cfg));
		
		assertEquals(1, acc.get("length"));
		assertEquals("http", acc.get("0.protocol"));
		assertEquals(8080, acc.get("0.port"));
		assertEquals("/", acc.get("0.handlers.0.path"));
		assertEquals("/site", acc.get("0.handlers.1.path"));
		assertEquals("/about", acc.get("0.handlers.2.path"));
		assertEquals("/blog", acc.get("0.handlers.3.path"));
	}
}

