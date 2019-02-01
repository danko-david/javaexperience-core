package eu.javaexperience.measurement;


public class Measure
{
	private static class MutaLong
	{
		public long num = System.currentTimeMillis();
	}

	private static ThreadLocal<MutaLong> num = new ThreadLocal<MutaLong>()
	{
		@Override
		protected MutaLong initialValue()
		{
			return new MutaLong();
		}
	};

	private static ThreadLocal<MutaLong> nano = new ThreadLocal<MutaLong>()
	{
		@Override
		protected MutaLong initialValue()
		{
			return new MutaLong();
		}
	};
	
	public static void startNanoCheckpoint()
	{
		nano.get().num = System.nanoTime();
	}

	public static String nanoCheckpoint(String text)
	{
		long tnow = System.nanoTime();
		long tprew = nano.get().num;
		nano.get().num = tnow;
		return text+" "+((System.nanoTime()-tprew)/1000000.0)+" ms";
	}



	public static void startCheckpoint()
	{
		num.get().num = System.currentTimeMillis();
	}

	public static String checkpoint(String text)
	{
		long tnow = System.currentTimeMillis();
		long tprew = num.get().num;
		num.get().num = tnow;
		return text+" "+(System.currentTimeMillis()-tprew)+" ms";
	}
}
