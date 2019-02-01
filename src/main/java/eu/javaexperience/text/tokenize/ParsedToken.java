package eu.javaexperience.text.tokenize;

import eu.javaexperience.semantic.references.MayNull;

public class ParsedToken<T>
{
	protected @MayNull TokenGroup<T> group;
	protected String content;
	protected int position;
	
	public ParsedToken(TokenGroup<T> grp, String text, int position)
	{
		this.group = grp;
		this.content = text;
		this.position = position;
	}
	
	@Override
	public String toString()
	{
		return "ParsedToken: group:"+group+", content: "+content;
	}
	
	public TokenGroup<T> getGroup()
	{
		return group;
	}
	
	public String getContent()
	{
		return content;
	}
}
