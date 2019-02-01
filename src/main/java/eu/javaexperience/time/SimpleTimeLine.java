package eu.javaexperience.time;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class SimpleTimeLine<T extends TimeLineElement<T>> implements Iterable<T>
{
	private T begin;
	private T end;

	/**
	 * Hogy olyan STL-t adsz-e amin tényleg szerepe-e az az elem, az csak a te becsületeden múlik.
	 * Érdemes, különben az eredmény lappangó hibát okozhat.
	 * */
	public static <T extends TimeLineElement<T>> boolean unifyWithNextWithNextOverride(SimpleTimeLine<T> stl,T elem)
	{
		T next = elem.next;
		if(next != null)
		{
			stl.delete(next);
			elem.to = next.to;
			elem.dTo = next.dTo;
			return true;
		}
		
		return false;
	}
	
	public int unifyElements(TimeLineElementUnifier<T> uni)
	{
		if(begin == null)
			return 0;
		
		int num = 0;
		T el = begin;
		while(el != null)
		{
			if(el.next != null)
				if(uni.mayUnify(el, el.next))
				{
					el.to = el.next.to;
					el.dTo = el.next.dTo;
					internal_delete(el.next, true);
					++num;
					continue;
				}
			
			el = el.next;
		}
		return num;
	}
	
	public int mergeElements(TimeLineElementMerger<T> merger)
	{
		if(begin == null)
			return 0;
		
		int num = 0;
		T el = begin;
		while(el != null)
		{
			if(el.next != null)
			{
				T merge = merger.merge(el, el.next);
				if(merge != null)
				{
					/*
					 * ha nincs előző elem az azt jelenti hogy ez ez idővonal első eleme
					 * az összeolvaszott elem az első elem lesz
					 */
					if(el.prev == null)
					{
						//nincs előző elem
						merge.prev = null;
						//idővonal kezdő eleme ez
						begin = merge;
					}
					else
					{
						//különben az előző elem a felülírandó előző eleme
						merge.prev = el.prev;
					}
					
					/*
					 * ha az összeolvaszott után nincs következő elem akkor akkor ez az
					 * utolsó elem az idővonalon.
					 */
					if(el.next.next == null)
					{
						end = merge;
						merge.next = null;
					}
					else
					{
						merge.next = el.next.next;
					}
					
					el = merge.next;
					
					++num;
					//lehet hogy ezt a blokkot az ez után következővel még öszze lehet vonni
					continue;
				}
			}
			el = el.next;
		}

		return num;
	}
	
	
	public int splitElement(TimeLineElementSplitter<T> splitter,T elem)
	{
		if(!contains(elem))
			return 0;
		
		int nums = 0;
		
		T[] elems = splitter.split(elem);
		internal_delete(elem, true);
		for(T e:elems)
		{
			add(e);
			nums++;
		}
		return nums;
	}
	
	public int splitAllElement(TimeLineElementSplitter<T> splitter)
	{
		int num = 0;
		for(T e:this)
		{
			T[] elems = splitter.split(e);
			if(elems.length == 1)
				if(elems[0] == e)
					continue;
			
			T next = e.next;
			T prev = e.prev;
			internal_delete(e, true);
			for(T i:elems)
			{
				fastAddBTW(prev, i, next);
				prev = i;
				//add(i);
				num++;
			}
		}

		return num;
	}
	
	public boolean delete(T elem)
	{
		return internal_delete(elem, false);
	}
	
	private boolean internal_delete(T elem,boolean sureContains)
	{
		//elsőt, utolsót, közbenlévőt
		if(begin != null)//ha begin null akkor az end is null!
		{
			if(elem == begin && begin == end)//csak ez az elem van benn.
			{
				unlinkElement(elem);
				begin = end = null;
				ep = 0;
				return true;
			}
			
			if(begin == elem)//kezdő elem törtlése
			{
				//kell lennie köv elemnek
				if(begin.next.next != null)
					linkElement(null, begin.next, begin.next.next);//van még 2 elem
				else
					linkElement(null, begin.next, null);//törlöm és már csak 1 vam
				unlinkElement(elem);
				ep--;
				return true;
			}

			if(elem == end)// záró elem törlése
			{
				//kell lennie előző elemnek
				if(end.prev.prev != null)
					linkElement(end.prev.prev, end.prev, null);//van még 2 elem
				else
					linkElement(null, end.prev, null);//törlöm és már csak 1 vam
				unlinkElement(elem);
				ep--;
				return true;
			}
		}
		
		//köztes elem törlése
		//csak az a kérdés hogy benne-van-e
		if(sureContains || contains(elem))
		{
			//kereszthivatkozás
			elem.prev.next = elem.next;
			elem.next.prev = elem.prev;
			unlinkElement(elem);
			ep--;
			return true;
		}
		
		return false;
	}
	
	public String getInconsistenty()
	{
		if(begin == end)
			return "minden oké";
		
		StringBuilder sb = new StringBuilder();
		
		ArrayList<T> elems = new ArrayList<>();
		
		for(T i = begin;i != end;i=i.getNext())
		{
			if(i!= null)
			{
				if(elems.contains(i))
					sb.append("Dupla elem: "+i+"\n");
				else
					elems.add(i);
			}	
			if(i.getNext() == null)
				sb.append("null menet közben\n");
		}
		return sb.toString();
	}
	
	public boolean contains(T elem)
	{
		if(begin == null)
			return false;
		
		for(T e = begin;e.next != null;e = e.next)
			if(e == elem)
				return true;
		
		return false;
	}

	public int cutAllBeforeElement(T elem)
	{
		if(begin == null)
			return 0;

		int num = 1;
		for(T e = begin;e.next != null;e = e.next, ++num)
			if(e == elem)
			{
				linkElement(null, e, e.next);
				break;
			}
		
		return num;
	}
	
	public int cutAllAfterElement(T elem)
	{
		if(end == null)
			return 0;

		int num = 1;
		for(T e = end;e.prev != null;e = e.prev, ++num)
			if(e == elem)
			{
				linkElement(e.prev, e, null);
				break;
			}
		
		return num;
	}

	private boolean fastAddBTW(T prev,T disz,T next)
	{
		//ha az after this null akkor a végére a sima add-al vagy az afterThis.next null
		if(prev == null || next == null)
			return add(disz);
		
		
		
		if(disz.isBTW(prev.to, next.from))//ha befér linkeljük
		{
			linkElement(prev, disz, next);
			++ep;
			return true;
		}

		return false;
	}
	
	public boolean add(T elem)
	{
//		if(elem == null) //felesleges
//			throw new IllegalArgumentException("Az elem nem lehet null!");
		
		
//try{
		if(begin == null) //első elem hozzáadása
		{
			linkElement(null, elem, null);
			return true;
		}
		
		if(elem.isBefore(begin))
		{
			linkElement(null, elem, begin);
			return true;
		}
		
		if(elem.isAfter(end))
		{
			linkElement(end, elem, null);
			return true;
		}

		if(elem.hasCommonSectionWith(begin))
			return false;
		
		for(T el = begin;el != null;el = el.next)
		{
			if(elem.hasCommonSectionWith(el))
				return false;
			
			if(el.next != null)
			{
				if(elem.isBTW(el.to, el.next.from))
				{
					linkElement(el, elem,  el.next);
					return true;
				}
			}
			else
				return false;//az előbb már vizsgáltuk (nem az utolsó után van)
		}
//}finally{if(this.size()>400_000){try {DDSI.writeStringToFile(toString(),"/tmp/stl");} catch (IOException e) {e.printStackTrace();}System.exit(0);}}
		
		System.out.println(toString());
		throw new IllegalStateException("Rossz implementáció! na ekkor mi van?");
	}
	
	private int ep = 0;
	
	private void unlinkElement(T elem)
	{
		elem.next = null;
		elem.prev = null;
	}
	
	/**
	 * Megadott elemek közé beszúrja az adott időelem blokkot.
	 * 
	 * (null,elem,null)	: első beszúrás, kezdő és vég elem elem lesz.
	 * (null,elem,first): az idővonal elejére teszi.
	 * (last,elem,null) : idővopnal végére teszi
	 * (t0,elem,t1)		: t0 és t1 közé teszi, előzőleg vizsgálni kell hogy meg lehet-e tenni, ezért privát ez a metódus
	 * */
	private void linkElement(T prev,T midd,T next)
	{
		if(prev == null && next == null)// a legeslegelső elem
		{
			midd.prev = null;
			midd.next = null;
			begin = midd;
			end = midd;
		}
		else if(prev == null)//első elemként linkelés
		{
		//	begin.prev = null;
		//	begin.next = null;
			midd.next = next;
			next.prev = midd;
			midd.prev = null;
			begin = midd;
		}
		else if(next == null)//utolsó elemként linkelés
		{
			/*if(next == end)
			{
				end.next = null;
				end.prev = null;
			}*/
			
			prev.next = midd;
			midd.prev = prev;
			midd.next = null;
			end = midd;
		}
		else
		{
			prev.next = midd;
			midd.next = next;

			next.prev = midd;
			midd.prev = prev;
		}
		ep++;
	}
	
	public T getFirst()
	{
		return begin;
	}
	
	public T getLast()
	{
		return end;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SimpleTimeLine\n{\n\t");
		TimeLineElement<T> el = begin;
		if(el != null)
		{
			sb.append(el.toString());
			
			while((el = el.next) != null)
			{
				sb.append("\n\t");
				sb.append(el.toString());
			}
		}

		sb.append("\n}\n");
		return sb.toString();
	}
	
	public Date getFullRangeStart()
	{
		if(begin == null)
			return null;
		return begin.getDateFrom();
	}
	
	public Date getFullRangeEnd()
	{
		if(end == null)
			return null;
		return end.getDateTo();
	}

	public T getElementAt(Date t)
	{
		T elem = begin;
		long et = t.getTime();
		while(elem != null)
		{
			if(elem.isIn(et))
				return elem;
			
			elem =  elem.next;
		}

		return null;
	}
	
	public T getFirstElementAfter(Date t)
	{
		T elem = begin;
		long et = t.getTime();
		while(elem != null)
		{
			if(elem.isAfter(et))
				return elem;
			
			elem =  elem.next;
		}

		return null;
	}
	
	public T getFirstElementBetween(Date from, Date to)
	{
		T ret = getFirstElementAfter(from);
		if(null == ret)
		{
			return null;
		}
		
		if(ret.isBTW(from, to))
		{
			return ret;
		}
		
		return null;
	}
	
	public void getElementsDurationIs(long inMilisec,Collection<T> store)
	{
		T elem = begin;
		while(elem != null)
		{
			if(elem.duration() == inMilisec)
				store.add(elem);
			
			elem = elem.next;
		}
	}
	
	public void getElementsDurationIsNot(long inMilisec,Collection<T> store)
	{
		T elem = begin;
		while(elem != null)
		{
			if(elem.duration() != inMilisec)
				store.add(elem);
			
			elem = elem.next;
		}
	}
	
	public void getElementsDurationGT(long inMilisec,Collection<T> store)
	{
		T elem = begin;
		while(elem != null)
		{
			if(elem.duration() > inMilisec)
				store.add(elem);
			
			elem = elem.next;
		}
	}
	
	public void getElementsDurationGTE(long inMilisec,Collection<T> store)
	{
		T elem = begin;
		while(elem != null)
		{
			if(elem.duration() >= inMilisec)
				store.add(elem);
			
			elem = elem.next;
		}
	}
	
	public void getElementsDurationLT(long inMilisec,Collection<T> store)
	{
		T elem = begin;
		while(elem != null)
		{
			if(elem.duration() < inMilisec)
				store.add(elem);
			
			elem = elem.next;
		}
	}
	
	public void getElementsDurationLTE(long inMilisec,Collection<T> store)
	{
		T elem = begin;
		while(elem != null)
		{
			if(elem.duration() <= inMilisec)
				store.add(elem);
			
			elem = elem.next;
		}
	}
	
	public void getElementsAfter(Date date,Collection<T> store)
	{
		T elem = begin;
		long t0 = date.getTime();
		while(elem != null)
		{
			if(elem.isAfter(t0))
				while(elem != null)
				{
					store.add(elem);
					elem = elem.next;
				}
			else
				elem = elem.next;
		}
	}
	
	public int size()
	{	
		return ep;
	}
	
	public T get(int n)
	{
		T ret = getFirst();
		while(null != ret && --n >= 0)
		{
			ret = ret.next;
		}
		return ret;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>()
		{
			private T elem = begin;
			@Override
			public boolean hasNext()
			{
				return elem != null;
			}

			@Override
			public T next()
			{
				T ret = elem;
				if(elem != null)
					elem = elem.next;
				else
					elem = null;
				return ret;
			}

			@Override
			public void remove() {
				throw new IllegalStateException("nincs megvalósítva");
			}
		};
	}
}