package eu.javaexperience.algorithm.search.graph;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.javaexperience.collection.map.MultiCollectionMap;
import eu.javaexperience.graph.GraphVertexTransition;
import eu.javaexperience.interfaces.simple.SimpleGetFactory;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.text.StringTools;

public class SimpleGraphStructure<V, E>
{
	protected MultiCollectionMap<V, GraphVertexTransition<SimpleGraphStructure<V, E>, V, E>> graph = new MultiCollectionMap
	(
		new HashMap<>(),
		//SimpleGetFactory.getHashSetFactory()
		SimpleGetFactory.getArrayListFactory()
	);
	
	public void addNode(V vertex)
	{
		graph.preserv(vertex);
	}
	
	public void addEdge(V from, E edge, V to)
	{
		addNode(from);
		addNode(to);
		GraphVertexTransition<SimpleGraphStructure<V,E>, V, E> trans = new GraphVertexTransition<>(this, from, edge, to);
		graph.put(from, trans);
		//graph.put(to, trans);
	}
	
	public Set<V> getVertexes()
	{
		return graph.keySet();
	}
	
	public Set<E> getEdges()
	{
		HashSet<E> ret = new HashSet<>();
		for(Entry<V, Collection<GraphVertexTransition<SimpleGraphStructure<V, E>, V, E>>> kv:graph.multiEntrySet())
		{
			//V key = kv.getKey();
			Collection<GraphVertexTransition<SimpleGraphStructure<V, E>, V, E>> val = kv.getValue();
			for(GraphVertexTransition<SimpleGraphStructure<V, E>, V, E> v:val)
			{
				//if(v.getFrom() == key)
				{
					ret.add(v.getEdge());
				}
			}
		}
		return ret;
	}
	
	public Set<GraphVertexTransition<SimpleGraphStructure<V, E>, V, E>> getVertextTransitions()
	{
		Set<GraphVertexTransition<SimpleGraphStructure<V, E>, V, E>> ret = new HashSet<>();
		
		for(Entry<V, Collection<GraphVertexTransition<SimpleGraphStructure<V, E>, V, E>>> kv:graph.multiEntrySet())
		{
			V key = kv.getKey();
			Collection<GraphVertexTransition<SimpleGraphStructure<V, E>, V, E>> val = kv.getValue();
			for(GraphVertexTransition<SimpleGraphStructure<V, E>, V, E> v:val)
			{
				if(v.getFrom().equals(key))
				{
					ret.add(v);
				}
			}
		}
		
		return ret;
	}
	
	public class GraphvizDotRenderer
	{
		public String defaultNodeType = "node";
		
		public GetBy1<String, V> nodeText;
		public GetBy1<String, E> edgeText;
		
		public void render(Appendable sb) throws IOException
		{
			sb.append("digraph structs {\n");
			//sb.append("	rankdir=LR\n");
			sb.append("	");
			sb.append(defaultNodeType);
			sb.append(" [shape=record];\n");
			
			Set<V> nodes = getVertexes();
			
			HashMap<V, Integer> ids = new HashMap<>();
			
			int id = 0;
			
			for(V n:nodes)
			{
				ids.put(n, id);
				
				sb.append("\t");
				sb.append("\"");
				sb.append("id_");
				sb.append(String.valueOf(id));
				sb.append("\"");
				if(null != nodeText)
				{
					String add = nodeText.getBy(n);
					if(null != add)
					{
						sb.append(renderNode(add));
					}
				}
				sb.append("\n");
				
				++id;
			}
			
			for(GraphVertexTransition<SimpleGraphStructure<V, E>, V, E> v:getVertextTransitions())
			{
				sb.append("\t\"");
				sb.append("id_");
				sb.append(String.valueOf(ids.get(v.getFrom())));
				sb.append("\" -> \"");
				sb.append("id_");
				sb.append(String.valueOf(ids.get(v.getTo())));
				sb.append("\"");
				if(null != edgeText)
				{
					sb.append("[label=\"");
					sb.append(escapeString("\"", edgeText.getBy(v.getEdge())));
					sb.append("\"];");
				}
				
				sb.append("\n");
			}
			
			sb.append("}\n");
		}
		
		public String renderNode(String str)
		{
			return " [label=\""+escape(str)+"\"]";
		}
		
		public String escape(String str)
		{
			return escapeString("\"", str).replace("\n", "\\n").replace("{", "\\{").replace("}", "\\}");
		}

		
	}
	
	private static String escapeString(String toEscape, String subject)
	{
		return subject.replace("(?!\\)"+toEscape, "\\"+toEscape);
	}
	
	public GraphvizDotRenderer toGraphvizRenderer(GetBy1<String, V> nodeText, GetBy1<String, E> edgeText)
	{
		SimpleGraphStructure<V, E>.GraphvizDotRenderer ret = this.new GraphvizDotRenderer();
		ret.nodeText = nodeText;
		ret.edgeText = edgeText;
		return ret;
	}
	
	public void toGraphviz
	(
		Appendable sb,
		GetBy1<String, V> nodeText
	)
		throws IOException
	{
		toGraphviz(sb, nodeText, null);
	}
	
	public void toGraphviz
	(
		Appendable sb,
		GetBy1<String, V> nodeText,
		GetBy1<String, E> edgeText
	)
		throws IOException
	{
		toGraphvizRenderer(nodeText, edgeText).render(sb);
	}
	
	public static <V, E> void addFromSearch(SimpleGraphStructure<V, E> dst, GraphSearcher<V, E> search, boolean multiPath)
	{
		List<GraphSearchTraveledPath<V, E>> br = search.getAllBranches();
		for(GraphSearchTraveledPath<V, E> ob:br)
		{
			if(multiPath || !ob.hasFork())
			{
				for(GraphSearchVertexTransition<V, E> t:ob.getPath())
				{
					dst.addEdge(t.getFrom(), t.getEdge(), t.getTo());
				}
			}
		}
	}
	
	public static <V, E> SimpleGraphStructure<V, E> fromSearch(GraphSearcher<V, E> search, boolean multiPath)
	{
		SimpleGraphStructure<V, E> ret = new SimpleGraphStructure<V, E>();
		addFromSearch(ret, search, multiPath);
		return ret;
	}
	
}
