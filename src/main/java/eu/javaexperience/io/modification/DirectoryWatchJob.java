package eu.javaexperience.io.modification;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import eu.javaexperience.exceptions.UnimplementedCaseException;
import eu.javaexperience.interfaces.simple.publish.SimplePublish2;
import eu.javaexperience.multithread.Job;

public class DirectoryWatchJob implements Job<Void>
{
	private final WatchService ws;
	private final Path p;
	private final SimplePublish2<Path, FileEvent> publish;
	
	/**
	 * 
	 * Ha a SimplePublish2 kivételt dob, a feladat csöndben leteszi a lantot...
	 * */
	public DirectoryWatchJob(File dir, SimplePublish2<Path, FileEvent> onFileChange) throws IOException
	{
		ws = FileSystems.getDefault().newWatchService();
		p = dir.toPath();
		this.publish = onFileChange;
	}
	
	public static FileEvent asEvent(Kind<Path> ev)
	{
		if(ev == StandardWatchEventKinds.ENTRY_CREATE)
			return FileEvent.created;
		else if(ev == StandardWatchEventKinds.ENTRY_DELETE)
			return FileEvent.deleted;
		else if(ev == StandardWatchEventKinds.ENTRY_MODIFY)
			return FileEvent.modified;
			
		throw new UnimplementedCaseException("no case defined for "+ev);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void exec(Void param) throws Throwable
	{
		try
		{
			p.register(ws, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
	
			while(true)
			{
				WatchKey cwk = ws.take();
				for (WatchEvent<?> event : cwk.pollEvents())
					publish.publish(((Path) event.context()), asEvent((Kind<Path>) event.kind()));
	
				cwk.reset();
			}
		}
		finally
		{
			ws.close();
		}
	}
	
	/*public static void main(String[] args) throws Throwable
	{
		DirectoryWatchJob j = new DirectoryWatchJob(new File("/tmp/1"), new SimplePublish2<Path,FileEvent>()
		{
			@Override
			public void publish(Path a,FileEvent kind)
			{
				System.out.println(a+" "+kind);
			}
		});
		JobExecutor.putJob(j, null);
	
		while(true)
			Thread.sleep(10000);
	}*/
}