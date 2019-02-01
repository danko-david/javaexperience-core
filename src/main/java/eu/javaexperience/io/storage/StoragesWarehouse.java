package eu.javaexperience.io.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import eu.javaexperience.io.FileContentMapper;
import eu.javaexperience.text.StringTools;

public class StoragesWarehouse<S extends Serializable>
{
	protected String rootDir;
	
	public StoragesWarehouse(String dir)
	{
		this.rootDir = dir;
		new File(dir).mkdirs();
	}
	
	protected Map<String, FileContentMapper<S>> pageStorages = new HashMap<>();
	
	public synchronized FileContentMapper<S> getStorage(String name) throws FileNotFoundException
	{
		FileContentMapper<S> ret = pageStorages.get(name);
		if(null == ret)
		{
			File dir = new File(rootDir+"/"+name);
			dir.mkdirs();
			ret = new FileContentMapper<>(dir);
			pageStorages.put(name, ret);
		}
		return ret;
	}
	
	public static void main(String[] args) throws Throwable
	{
		StoragesWarehouse env = new StoragesWarehouse("/tmp/spider_test");
		
		{
			FileContentMapper<byte[]> store = env.getStorage("teszt_bin");
			store.put("0", "Árvíztűrő tükörfúrógép".getBytes());
			store.put("1", StringTools.repeatString("Árvíztűrő tükörfúrógép", 100).getBytes());
		}
		
		{
			FileContentMapper<String> store = (FileContentMapper<String>)(Object) env.getStorage("teszt_txt");
			
			store.put("0", "Árvíztűrő tükörfúrógép");
			store.put("1", StringTools.repeatString("Árvíztűrő tükörfúrógép", 100));
		}
	}
	
}
