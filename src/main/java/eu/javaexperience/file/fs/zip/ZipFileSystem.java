package eu.javaexperience.file.fs.zip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import eu.javaexperience.arrays.ArrayTools;
import eu.javaexperience.collection.iterator.IteratorTools;
import eu.javaexperience.collection.tree.TreeNode;
import eu.javaexperience.collection.tree.TreeNodeTools;
import eu.javaexperience.file.AbstractFile;
import eu.javaexperience.file.AbstractFileSystem;
import eu.javaexperience.file.FileSystemTools;
import eu.javaexperience.interfaces.simple.getBy.GetBy1;

public class ZipFileSystem implements AbstractFileSystem
{
	protected String file;
	protected ZipFile zip;
	
	protected TreeNode root;
	
	public ZipFileSystem(String file) throws IOException
	{
		this.file = file;
		this.zip = new ZipFile(file);
		this.root = processZipFiles(zip);
	}
	
	protected static final GetBy1<String, TreeNode> getNodeId = new GetBy1<String, TreeNode>()
	{
		@Override
		public String getBy(TreeNode a)
		{
			return (String) a.getEtc("file");
		}
	};
	
	protected static final GetBy1<TreeNode, String> createNode = new GetBy1<TreeNode, String>()
	{
		@Override
		public TreeNode getBy(String a)
		{
			TreeNode ret = new TreeNode<>();
			ret.putEtc("file", a);
			return ret;
		}
	};
	
	protected TreeNode getNodeByPath(String path)
	{
		return TreeNodeTools.getByPath(root, FileSystemTools.decomposePath(path), getNodeId);
	}
	
	protected String toPath(TreeNode node)
	{
		StringBuilder sb = new StringBuilder();
		ArrayList<String> path = new ArrayList<>();
		TreeNodeTools.getPathToRoot(path, node, getNodeId);
		Collections.reverse(path);
		for(String s:path)
		{
			if(sb.length() > 0)
			{
				sb.append("/");
			}
			if(null != s)
			{
				sb.append(s);
			}
		}
		return sb.toString();
	}
	
	protected static TreeNode processZipFiles(ZipFile zip)
	{
		TreeNode ret = new TreeNode();
		for(ZipEntry e: IteratorTools.fromEnumeration(zip.entries()))
		{
			String[] path = FileSystemTools.decomposePath(e.getName());
			TreeNodeTools.getOrCreatePath(ret, path, getNodeId, createNode).putEtc("ent", e);
		}
		ret.putEtc("ent", new ZipEntry("/"));
		return ret;
	}
	
	@Override
	public AbstractFile fromUri(String uri)
	{
		return new ZipFsFile(this, uri);
	}

	@Override
	public String getFileSeparator()
	{
		return File.separator;
	}
}
