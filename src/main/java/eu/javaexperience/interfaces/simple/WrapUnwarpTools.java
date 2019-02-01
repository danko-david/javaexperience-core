package eu.javaexperience.interfaces.simple;

import eu.javaexperience.text.StringTools;

public class WrapUnwarpTools
{
	public static WrapUnwrap<String, String> noWrap = new WrapUnwrap<String, String>()
	{
		public String wrap(String b)
		{
			return b;
		}
		
		@Override
		public String unwrap(String w)
		{
			return w;
		}
	};

	public static WrapUnwrap<String, String> withPrefix(final String prefix)
	{
		return new WrapUnwrap<String, String>()
		{
			@Override
			public String wrap(String b)
			{
				return prefix+b;
			}
			
			@Override
			public String unwrap(String w)
			{
				if(!w.startsWith(prefix))
				{
					return null;
				}
				
				return StringTools.getSubstringAfterFirstString(w, prefix, null);
			}
		};
	}
	
	public static <F, T> WrapUnwrap<T, F> inverse(WrapUnwrap<F, T> wu)
	{
		return new WrapUnwrap<T, F>()
		{
			@Override
			public T wrap(F b)
			{
				return wu.unwrap(b);
			}

			@Override
			public F unwrap(T w)
			{
				return wu.wrap(w);
			}
		};
	}

	public static <F, I ,T> WrapUnwrap<T, F> chain
	(
		WrapUnwrap<I, F> first,
		WrapUnwrap<T, I> last
	)
	{
		return new WrapUnwrap<T, F>()
		{
			@Override
			public T wrap(F b)
			{
				return last.wrap(first.wrap(b));
			}

			@Override
			public F unwrap(T w)
			{
				return first.unwrap(last.unwrap(w));
			}
		};
	}
	
	public static void main(String[] args)
	{
		String url = "https://site.tld/dir/file.ext";
		String ret = "/home/user/download/dir/file.ext";
		
		WrapUnwrap<String, String> c = chain
		(
			inverse(withPrefix("https://site.tld")),
			withPrefix("/home/user/download")
		);
		
		System.out.println(c.wrap(url));
	}
}
