package eu.javaexperience.measurement;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import eu.javaexperience.measurement.MeasurementSerie.MeasurementData;;

public class MeasurementSerie implements Iterable<MeasurementData>
{
	public static class MeasurementData implements Comparable<MeasurementData>, Cloneable, Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private double value;
		private double delta;
		
		public MeasurementData(double value)
		{
			this.value = value;
		}
		
		public double getValue()
		{
			return value;
		}
		
		public double getAbsoluteDeviance()
		{
			return delta;
		}
		
		@Override
		public Object clone()
		{
			return new MeasurementData(value);
		}
		
		@Override
		public int compareTo(MeasurementData arg0)
		{
			return Double.compare(this.value, arg0.value);
		}
	}
	
	private boolean calcualted = false;
	
	
	ArrayList<MeasurementData> x = new ArrayList<>();
	
	/**
	 * Average
	 * */
	protected double _x;
	
	/**
	 * Deviance
	 * */
	protected double s;
	
	/**
	 * Average absolute deviance
	 * */
	protected double E;
	
	protected MeasurementData max;
	
	protected MeasurementData min;
	
	/**
	 * átlag _x
	 * Abszolút Eltérés: delta = x_n - _x
	 * szórás s = sqrt( 1/(n-1) *sum(delta^2))
	 * Átlagos abszolut eltérés: E = 1/n * sum(abs(delta))
	 * 
	 * gauss-e: s^2 / E^2 = pi / 2    +- 15%
	 * */
	
	public void add(double num)
	{
		x.add(new MeasurementData(num));
		calcualted = false;
	}
	
	public void add(double... num)
	{
		for(double d:num)
		{
			add(d);
		}
	}
	
	public static final double PI = Math.PI;
	
	public static final double PI_per_2 = PI / 2;
	
	public static final double PI_per_2_delta_15_percent = PI_per_2 * 0.15;
	
	public static final double Gauss_min = PI_per_2 - PI_per_2_delta_15_percent;
	
	public static final double Gauss_max = PI_per_2 + PI_per_2_delta_15_percent;
	
	/**
	 * gauss eloszlású ha s^2 / E^2 = Math.PI / 2 +- 15%
	 * */
	public boolean isGaussDeviance()
	{
		double approx = getAbsoluteGauss();
		return approx > Gauss_min && approx < Gauss_max;
	}

	/**
	 * s^2 / E^2 értékét adja vissza
	 * */
	public double getAbsoluteGauss()
	{
		assureCalcualted();
		if(0 == s)
		{
			return 0;
		}
		else
		{
			return (s*s) / (E*E);
		}
	}
	
	public double getRelativeGaussApproximation()
	{
		return 100.0*(getAbsoluteGauss() - PI_per_2) / PI_per_2;
	}
	
	public double getAverage()
	{
		assureCalcualted();
		return _x;
	}
	
	public double getStandardDeviance()
	{
		assureCalcualted();
		return s;
	}
	
	private void assureCalcualted()
	{
		if(!calcualted)
		{
			if(!calculate())
			{
				throw new IllegalStateException("A mérési sorozatnak legalább egy eredményt kell tartalmaznia");
			}
		}
	}
	
	public MeasurementData kérMaximum()
	{
		return max;
	}
	
	public MeasurementData kérMinimum()
	{
		return min;
	}
	
	public double getAverageAbsoluteDeviance()
	{
		assureCalcualted();
		return E;
	}
	
	public void dropUpperAndLowerPercent(double d)
	{
		Collections.sort(x);
		int num = (int) (x.size()*d);
		
		for(int i=0;i<num;i++)
		{
			x.remove(0);
			x.remove(x.size()-1);
		}
		
		calcualted = false;
	}
	
	public void dropUpperAndLower5Percent()
	{
		dropUpperAndLowerPercent(0.05);
	}
	
	public boolean calculate()
	{
		double sum = 0;
		
		if(x.size() == 0)
		{
			return false;
		}
		
		for(MeasurementData ad:x)
		{
			sum += ad.value;
		}
		
		double darab = x.size();
		_x = sum / darab;
		
		double sumDeltaSquare = 0;
		double absDeltaSum = 0; 

		min = max = x.get(0);
		
		for(MeasurementData adat:x)
		{
			if(min.value > adat.value)
			{
				min = adat;
			}
			
			if(max.value < adat.value)
			{
				max = adat;
			}
			
			adat.delta = adat.value - _x;
			absDeltaSum += Math.abs(adat.delta);
			sumDeltaSquare += Math.pow(adat.delta, 2);
		}
		
		if(x.size() > 1)
		{
			s = Math.sqrt(sumDeltaSquare * (1.0/ (x.size() - 1.0)));
		}
		else
		{
			s = 0;
		}
		E = absDeltaSum / darab;
		calcualted = true;
		
		return true;
	}
	
	public double getPositiveAbsoluteDeviance()
	{
		return max.value - _x;
	}
	
	public double getNegativeAbsoluteDeviance()
	{
		return min.value - _x;
	}
	
	public void printTable(PrintStream ps)
	{
		assureCalcualted();
		ps.println("|n\t|value\t|delta\t|delta^2|");
		int i=0;
		for(MeasurementData a:this)
		{
			ps.printf
			(
				"|%d\t|%s\t|%s\t|%s\t|\n",
				++i,
				formatNumber(a.value),
				formatNumber(a.delta),
				formatNumber(Math.pow(a.delta, 2))
			);
		}
	}
	
	public String getPrintedSummarise()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		try
		{
			printSummarize(ps);
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
		
		return new String(baos.toByteArray());
	}
	
	public AvgStdDev getAvgAndStdDev()
	{
		return new AvgStdDev(getAverage(), getStandardDeviance());
	}
	
	public void printSummarize(PrintStream ps)
	{
		assureCalcualted();
		ps.println("=== Mérési eredmény összesítés ===\n");
		
		ps.print("Elemszám [n]: ");
		ps.println(x.size());
		
		ps.print("Legnagyobb elem: ");
		ps.println(formatNumber(kérMaximum().value));

		ps.print("Legkisebb elem: ");
		ps.println(formatNumber(kérMinimum().value));
		
		ps.print("Pozitív Abszolút terjedelem: ");
		ps.println(formatNumber(getPositiveAbsoluteDeviance()));
		
		ps.print("Negatív Abszolút terjedelem: ");
		ps.println(formatNumber(getNegativeAbsoluteDeviance()));
		
		ps.print("Átlag [_x]: ");
		ps.println(formatNumber(getAverage()));
		
		ps.print("Pozitív Relatív terjedelem: ");
		ps.println(formatNumber(getPositiveAbsoluteDeviance()));
		
		ps.print("Negatív Relatív terjedelem: ");
		ps.println(formatNumber(getNegativeAbsoluteDeviance()));

		ps.print("Szórás [s]:");
		ps.println(formatNumber(getStandardDeviance()));
		
		ps.print("Áltagos abszolút eltérés [E]: ");
		ps.println(formatNumber(getAverageAbsoluteDeviance()));
		
		ps.print("Abszolút Gauss közelítési érték [s^2/E^2]: ");
		ps.println(formatNumber(getAbsoluteGauss()));
		
		ps.print("Relatív Gauss közelítési érték:");
		ps.println(formatNumber(getRelativeGaussApproximation()));
		
		ps.print("Gauss eloszlású [s^2/E^2 ~ pi/2 +- 15%]: ");
		ps.println(isGaussDeviance()?"Igen":"Nem");
	}
	
	public String formatNumber(double num)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(num);
		int i = sb.indexOf(".");
		if(i > 0 && i+3 < sb.length())
			sb.delete(i+4, sb.length());

		return sb.toString();
	}
	
	public String getShortStat()
	{
		calculate();
		StringBuilder sb = new StringBuilder();
		sb.append("Measurement report from ");
		sb.append(x.size());
		sb.append(" samples: avg:");
		sb.append(formatNumber(getAverage()));
		sb.append(", dev: ±");
		sb.append(formatNumber(getStandardDeviance()));
		sb.append(", g: ");
		sb.append(formatNumber(getAbsoluteGauss()));
		sb.append(" (");
		sb.append(formatNumber(getRelativeGaussApproximation()));
		sb.append(" %)");
		return sb.toString();
	}
	
	public static void main(String[] args) throws Throwable
	{
		MeasurementSerie sor = new MeasurementSerie();
		sor.add(100.2,100,100.4,100.15,100.25,100.17,100.22,100.2);
		
		sor.calculate();
		
		sor.printSummarize(System.out);
		sor.printTable(System.out);
		
		/*
		sor.kiírSzámtábla(System.out);
		sor.kiírÖsszesítés(System.out);
		
		System.out.println("5% eldobással");
		
		sor.eldobAlsóFelső5Százalék();
		
		sor.kiírÖsszesítés(System.out);
		*/
		
	/*	MérésSorozat eredeti = new MérésSorozat();
		MérésSorozat sor = new MérésSorozat();

		//sor.adEredmények(100.2,100,100.1,100.15,100.2,100.17,100.22,100.2,99.9,99.8);
		
	  	String cím = "http://ddsi.hu";
	  //	Random véletlen = new Random(System.currentTimeMillis());
	  	
	  	
	  	for(int i=0;i<10||!sor.eGaussEloszlású();i++)
	  	{
	  		long t = //random.read();
	  				cucc(cím);
	  				//véletlen.nextInt();
	  		//double t = Math.pow(véletlen.nextFloat(),véletlen.nextLong());
	  				//ping(cím);
	  		System.out.println((i+1)+" "+t);
	  		eredeti.adEredmény(t);
	  		sor = eredeti.klónozd();
	  		sor.eldobAlsóFelső5Százalék();
	  	}
	  
	  //	sor.eldobAlsóFelső5Százalék();
	  	

	  	sor.kiírÖsszesítés(System.out);
	  	
	*/
	
	}
	
	@Override
	public MeasurementSerie clone()
	{
		MeasurementSerie sor = new MeasurementSerie();
		for(MeasurementData ad:this)
		{
			sor.add(ad.value);
		}
		
		return sor;
	}
	
	@Override
	public Iterator<MeasurementData> iterator()
	{
		return x.iterator(); 
	}

	public double getMaximumValue()
	{
		assureCalcualted();
		return max.value;
	}

	public double getMinimumValue()
	{
		assureCalcualted();
		return min.value;
	}

	public int elementsCount()
	{
		return x.size();
	}
}
