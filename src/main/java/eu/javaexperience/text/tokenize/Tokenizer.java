package eu.javaexperience.text.tokenize;

import java.util.ArrayList;
import java.util.Collection;

import eu.javaexperience.collection.CollectionTools;
import eu.javaexperience.text.StringTools;

public class Tokenizer<T>
{
	protected ArrayList<TokenGroup<T>> tokenizers = new ArrayList<>();
	
	public Tokenizer(Collection<TokenGroup<T>> grps)
	{
		CollectionTools.copyInto(grps, tokenizers);
	}
	
	public void tokenize(Collection<ParsedToken<T>> dst, String source)
	{
		int from = 0;
		
		final int len = source.length();
		out:while(from < len)
		{
			for(TokenGroup<T> t:tokenizers)
			{
				ParsedToken<T> ret = t.tryMatch(from, source);
				if(null != ret)
				{
					dst.add(ret);
					from += ret.content.length();
					continue out;
				}
			}
			
			throw new RuntimeException("Unrecognisable token at positon: "+from+", content: \n"+source+"\n"+StringTools.repeatChar(' ', from)+"^\n");
		}
	}
	
	@Override
	public String toString()
	{
		return super.toString();
	}
}