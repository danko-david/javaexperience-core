package eu.javaexperience.document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.javaexperience.interfaces.simple.getBy.GetBy1;
import eu.javaexperience.io.AppendableLocklessWriter;
import eu.javaexperience.reflect.Mirror;

public class DocumentTools
{
	private DocumentTools() {}
	
	public static final Node[] emptyNodeArrayInstance = new Node[0];

	public static Document getOwnerDocument(Node node)
	{
		if (node.getNodeType() == Node.DOCUMENT_NODE)
		{
			return (Document)node;
		}
		else
		{
			return node.getOwnerDocument();	
		}
	}
	
	protected static final DocumentBuilder DEFAULT_BUILDER;
	
	static
	{
		DocumentBuilder BUILDER = null;
		try
		{
			BUILDER = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		}
		catch(Exception e)
		{
			Mirror.propagateAnyway(e);
			BUILDER = null;
		}
		DEFAULT_BUILDER = BUILDER;
	}
	
	public static Document createEmptyDocument()
	{
		return DEFAULT_BUILDER.newDocument();
	}
	
	public static void toString(Node node, Appendable app) throws TransformerException
	{
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(node), new StreamResult(new AppendableLocklessWriter(app)));
	}
	
	public static String toString(Node node) throws TransformerException
	{
		StringBuilder sb = new StringBuilder();
		toString(node, sb);
		return sb.toString();
	}
	
	public static Document parseDocument(String doc) throws ParserConfigurationException, SAXException, IOException
	{
		return parseDocument(new ByteArrayInputStream(doc.getBytes()));
	}
	
	public static Document parseDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		dbf.setSchema(null);
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		return db.parse(is);
	}
	
	public static void selectAll(Node elem, Collection<Node> selected, GetBy1<Boolean, Node> selector)
	{
		if(Boolean.TRUE.equals(selector.getBy(elem)))
		{
			selected.add(elem);
		}
		
		recursiveSelect(elem.getChildNodes(), selected, selector);
	}
	
	public static void recursiveSelect(NodeList elements, Collection<Node> selected, GetBy1<Boolean, Node> selector)
	{
		int len = elements.getLength();
		for(int i=0;i<len;++i)
		{
			Node e = elements.item(i);
			selectAll(e, selected, selector);
		}
	}
	
	public static Node findFirst(Node element, GetBy1<Boolean, Node> selector)
	{
		if(selector.getBy(element) == Boolean.TRUE)
		{
			return element;
		}
		
		NodeList nl = element.getChildNodes();
		int size = nl.getLength();
		for(int i=0;i<size;++i)
		{
			Node ret = findFirst(nl.item(i), selector);
			if(null != ret)
			{
				return ret;
			}
		}
		
		return null;
	}
	
	
	
	
	public static final GetBy1<Boolean, Node> selectAwithHREFattr = new GetBy1<Boolean, Node>()
	{
		@Override
		public Boolean getBy(Node a)
		{
			return "a".equals(a.getNodeName()) && null != a.getAttributes() &&null != a.getAttributes().getNamedItem("href");
		}
	};




	public static GetBy1<Boolean, Node> selectNodesByTagName(final String name)
	{
		return new GetBy1<Boolean, Node>()
		{
			@Override
			public Boolean getBy(Node a)
			{
				return name.equals(a.getNodeName());
			}
		};
	}

	public static GetBy1<Boolean, Node> selectNodesByAttributeValue(final String attr, final String value)
	{
		return new GetBy1<Boolean, Node>()
		{
			@Override
			public Boolean getBy(Node a)
			{
				NamedNodeMap nnm = a.getAttributes();
				if(null == nnm)
				{
					return false;
				}
				
				Node n = nnm.getNamedItem(attr);
				if(null == n)
				{
					return false;
				}
				
				return value.equals(n.getTextContent());
			}
		};
	}
	
	public static String txtOf(Node doc, GetBy1<Boolean, Node> selector)
	{
		if(null == doc)
		{
			return "";
		}
		
		ArrayList<Node> nds = new ArrayList<>();
		
		selectAll(doc, nds, selector);
	
		if(nds.isEmpty())
		{
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		for(Node n:nds)
		{
			String s = n.getTextContent();
			if(null != s)
			{
				sb.append(s);
			}
		}
		return sb.toString();
	}
	
	public static String txtOf(Node node)
	{
		if(null == node)
		{
			return "";
		}
		
		return node.getTextContent();
	}

	
	public static Node getElementById(Node n, String id)
	{
		return findFirst(n, selectId(id));
	}

	public static GetBy1<Boolean, Node> selectId(String id)
	{
		return selectNodesByAttributeValue("id", id);
	}

	public static Node getAttr(Node n, String attr)
	{
		if(null == n || null == attr)
		{
			return null;
		}
		
		NamedNodeMap nnm = n.getAttributes();
		if(null == nnm)
		{
			return null;
		}
		
		return nnm.getNamedItem(attr);
	}
	
	public static String getAttrString(Node node, String name)
	{
		Node n = getAttr(node, name);
		if(null == n)
		{
			return null;
		}
		
		return n.getTextContent();
	}
	
	public static boolean hasAttribute(Node n, String attr)
	{
		return null != getAttr(n, attr);
	}
	
	public static boolean isAttribute(Node n, String attr, String value)
	{
		if(null == value)
		{
			return hasAttribute(n, attr);
		}
		
		Node re = getAttr(n, attr);
		if(null == re)
		{
			return false;
		}
		return value.equals(re.getTextContent());
	}

	public static GetBy1<Boolean, Node> selectNodesByAttributeValueContains(final String attr, final String value)
	{
		return new GetBy1<Boolean, Node>()
		{
			@Override
			public Boolean getBy(Node a)
			{
				a = getAttr(a, attr);
				if(null == a)
				{
					return false;
				}
				
				String str = a.getTextContent();
				if(null == str)
				{
					return false;
				}
				return str.indexOf(value) >= 0;
			}
		};
	}

	public static boolean isTagName(Node node, String name)
	{
		return node.getNodeName().equalsIgnoreCase(name);
	}

	public static Iterable<Node> iterableForChilds(Node n)
	{
		final NodeList nl = n.getChildNodes();
		return new Iterable<Node>()
		{
			@Override
			public Iterator<Node> iterator()
			{
				return new Iterator<Node>()
				{
					int i = 0;
					@Override
					public boolean hasNext()
					{
						return i < nl.getLength();
					}

					@Override
					public Node next()
					{
						return nl.item(i++);
					}

					@Override
					public void remove()
					{
						throw new IllegalAccessError("This iterator for Node's child is read only");
					}
				};
			}
		};
	}
	
	public static Node getOrCreateNamedNode(Document owner, NamedNodeMap map, String key)
	{
		Node ret = map.getNamedItem(key);
		if(null == ret)
		{
			ret = owner.createAttribute(key);
			map.setNamedItem(ret);
		}
		return ret;
	}
	
	
	public static Node copyAttrAndAdopChilds(Document doc, Node dst, Node src)
	{
		adoptAttributes(doc, dst, src);
		
		NodeList nl = src.getChildNodes();
		for(int i=0;i<nl.getLength();++i)
		{
			dst.appendChild(doc.adoptNode(nl.item(i).cloneNode(true)));
		}
		
		//interesting, but this works as expected (getting the soure node value but setting the destination with text content)
		String val = src.getNodeValue();
		if(null != val)
		{
			dst.setTextContent(val);
		}
		
		return dst;
	}

	public static void adoptAttributes(Document doc, Node dst, Node src)
    {
		NamedNodeMap dattr = dst.getAttributes();
		NamedNodeMap attr = src.getAttributes();
		if(null != attr)
		{
			for(int i=0;i < attr.getLength();++i)
			{
				Node a = attr.item(i);
				getOrCreateNamedNode(doc, dattr, a.getNodeName()).setNodeValue(a.getNodeValue());
			}
		}
    }

	public static boolean isTextNode(Node node)
	{
		return Document.TEXT_NODE == node.getNodeType();
	}

	
	public static void toStringPretty(Node node, Appendable app) throws TransformerException
	{
		TransformerFactory tf = TransformerFactory.newInstance();
		tf.setAttribute("indent-number", 4);

		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(node), new StreamResult(new AppendableLocklessWriter(app)));
	}
	
	public static String toStringPretty(Node node) throws TransformerException
	{
		StringBuilder sb = new StringBuilder();
		toStringPretty(node, sb);
		return sb.toString();
	}
}