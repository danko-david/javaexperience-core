package eu.javaexperience.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SwingTools
{
	
	public static void okWindow(final String s)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				JOptionPane.showMessageDialog(null, s);
			}
		}.start();
	}
	
	public static void okWindowBlock(String s)
	{
		JOptionPane.showMessageDialog(null, s);
	}
	
	
	public static void centerWindow(Window frame)
	{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
	

	public static JLabel sizedLabel(String s,int w, int h)
	{
		JLabel j = new JLabel(s);
		j.setPreferredSize(new Dimension(w,h));
		return j;
	}
	
	public static String promptWindow(String title, String msg, int field_char_size, boolean password) throws InterruptedException
	{
		final JFrame f = new JFrame(title);
		f.setLayout(new GridLayout(3,1));
		JPanel label = new JPanel();
		JPanel bep = new JPanel() ;
		JPanel but= new JPanel();
		but.setLayout(new FlowLayout());
		JButton b = new JButton("OK");
		but.add(b);
		final JTextField be = password?new JPasswordField(field_char_size):new JTextField(field_char_size);

		label.add(new JLabel(msg));
		label.setLayout(new FlowLayout());
		bep.add(be);
		bep.setLayout(new FlowLayout());
		f.add(label);
		f.add(bep);
		f.add(but);
		f.pack();
		centerWindow(f);
		
		final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		
		f.addWindowListener
		(
			new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					queue.add("");
					e.getWindow().dispose();
				}
			}
		);
		
		f.setVisible(true);
		
		b.addActionListener
		(
			new ActionListener()
			{	@Override
				public void actionPerformed(ActionEvent arg0)
				{
					queue.add(be.getText());
					f.dispose();
				}
			}
		);
		
		return queue.take();
	}
	
	public static int options(String title,String label, Object... components)
	{
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		return JOptionPane.showOptionDialog
		(
			new JFrame(),
			label,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,
			components,
			null
		);
	}
	
	public static void realRepaint(Component c)
	{
		c.invalidate();
		c.validate();
		c.repaint();
	}
	
	public static Rectangle getScreenSize()
	{
		Dimension d =Toolkit.getDefaultToolkit().getScreenSize();
		return new Rectangle((int)d.getWidth(),(int)d.getHeight());
	}
	
	public static File fileSelector(File init, int jfilechooser_mode)
	{
		JFileChooser fch = new JFileChooser();
		if(null != init)
		{
			fch.setSelectedFile(init);
		}

		if(jfilechooser_mode>-1)
		{
			fch.setFileSelectionMode(jfilechooser_mode);
		}

		fch.showOpenDialog(null);
		return fch.getSelectedFile();
	}
	
	public static void main(String[] args) throws InterruptedException
	{
		/*
			String ret = promptWindow("Get String", "Enter text", 15, true);
			System.out.println(ret);
		*/
		
		//System.out.println(options("title", "question", "df", 20, "áűá"));
		//System.out.println(getScreenSize());
		
		//System.out.println(fileSelector(null, JFileChooser.DIRECTORIES_ONLY));
		
	}
	
}
