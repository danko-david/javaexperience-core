package eu.javaexperience.functional.processing;

import java.util.ArrayList;
import java.util.Map;

import eu.javaexperience.interfaces.simple.publish.SimplePublish1;

public class ProcessStage
{
	protected ArrayList<SimplePublish1<Map<String, Object>>> ops = new ArrayList<>();
	
	public void addStage(SimplePublish1<Map<String, Object>> stage)
	{
		ops.add(stage);
	}
	
	
	public void execute(Map<String, Object> env)
	{
		for(SimplePublish1<Map<String, Object>> pub:ops)
		{
			pub.publish(env);
		}
	}
	
	@SafeVarargs
	public static ProcessStage wrapAll(SimplePublish1<Map<String, Object>>... ops)
	{
		ProcessStage ret = new ProcessStage();
		for(SimplePublish1<Map<String, Object>> op:ops)
		{
			ret.addStage(op);
		}
		
		return ret;
	}
}
