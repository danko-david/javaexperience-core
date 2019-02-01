package eu.javaexperience.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import eu.javaexperience.swing.SwingTools;

public class ImageDisplayWindow
{
	protected JFrame frame;
	protected BufferedImage image;
	protected boolean autoscaleImage = true;
	
	public ImageDisplayWindow()
	{
		frame = new JFrame()
		{
			public static final long serialVersionUID = 1L;

			public void paint(Graphics g)
			{
				if(null == image)
				{
					return;
				}
				
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				
				if(autoscaleImage)
				{
					Dimension dim = ImageTools.calculateResizeKeepRatioFitIn(image.getWidth(), image.getHeight(), this.getWidth(), this.getHeight());
					g.drawImage(image, 0, 30, (int) dim.getWidth(), (int) dim.getHeight()+30, null);
				}
				else
				{
					g.drawImage(image, 0, 30, null);
				}
			}
		};
		frame.setSize(300, 300);
	}
	
	public void show()
	{
		/*if(null != image)
		{
			frame.setSize(image.getWidth(), image.getHeight() + 30);
		}*/
		frame.setVisible(true);
	}
	
	
	public void setBufferedImage(BufferedImage image)
	{
		this.image = image;
		SwingTools.realRepaint(frame);
		/*if(null != image)
		{
			frame.setSize(image.getWidth(), image.getHeight() + 30);
		}*/
	}
}
