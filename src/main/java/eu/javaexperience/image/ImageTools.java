package eu.javaexperience.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import eu.javaexperience.math.MathTools;

public class ImageTools
{
	public static BufferedImage resize(BufferedImage img, int newW, int newH)
	{
		if(img.getWidth()==newW&&img.getHeight()==newH)
		{
			return img;
		}
		
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage  dimg = new BufferedImage(newW, newH, img.getType());
		
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint
		(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR
		);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}
	
	public static int calculateResizeKeepRatio(int width, int height, boolean isGivenWidth, int newSize)
	{
		double ratio = width/((double) height);
		if(isGivenWidth)
		{
			return (int) (newSize/ratio);
		}
		else
		{
			return (int) (newSize*ratio);
		}
	}
	
	public static Dimension calculateResizeKeepRatioFitIn(int width, int height, int newWidth, int newHeight)
	{
		int nw = newWidth;
		int nh = calculateResizeKeepRatio(width, height, true, newWidth);
		
		if(nh > newHeight)
		{
			nh = newHeight;
			nw = calculateResizeKeepRatio(width, height, false, newHeight);
		}
		
		return new Dimension(nw, nh);
	}
	
	protected static Dimension calculateResizeKeepRatioFitInCheck(int width, int height, int newWidth, int newHeight)
	{
		Dimension ret = calculateResizeKeepRatioFitIn(width, height, newWidth, newHeight);
		if
		(
			newWidth < ret.getWidth()
		||
			newHeight < ret.getHeight()
		||
			!MathTools.inRange(width/((double)height), ret.getWidth()/((double)ret.getHeight()), 0.01)
		)
		{
			System.out.println("Resize mismatch: "+newWidth+" "+newHeight+" "+ret);
		}
		
		return ret;
	}
	
	public static void main(String[] args)
	{
		calculateResizeKeepRatioFitInCheck(1024, 768, 100, 1000);
		calculateResizeKeepRatioFitInCheck(1024, 768, 1000, 100);
		
		calculateResizeKeepRatioFitInCheck(768, 1024, 100, 1000);
		calculateResizeKeepRatioFitInCheck(768, 1024, 1000, 100);
		
		calculateResizeKeepRatioFitInCheck(1000, 100, 100, 1000);
		calculateResizeKeepRatioFitInCheck(1000, 100, 1000, 100);
		
		calculateResizeKeepRatioFitInCheck(100, 1000, 100, 1000);
		calculateResizeKeepRatioFitInCheck(100, 1000, 1000, 100);
	}
	
	public static JFrame guiShowimage
	(
		final String title,
		final BufferedImage br,
		final boolean exitOnClose,
		boolean scaleImage
	)
	{
		JFrame ret = new JFrame()
		{
			public static final long serialVersionUID = 1L;

			public void paint(Graphics g)
			{
				if(scaleImage)
				{
					Dimension dim = calculateResizeKeepRatioFitIn(br.getWidth(), br.getHeight(), this.getWidth(), this.getHeight());
					g.setColor(Color.WHITE);
					g.fillRect(0, 0, this.getWidth(), this.getHeight());
					g.drawImage(br, 0, 30, (int) dim.getWidth(), (int) dim.getHeight()+30, null);
				}
				else
				{
					g.drawImage(br, 0, 30, null);
				}
			}

		};
		
		ret.setTitle(title);
		ret.setSize(br.getWidth(), br.getHeight() + 30);

		if(exitOnClose)
			ret.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			ret.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		ret.setVisible(true);
		return ret;
	}

	public static BufferedImage imageToGrey(BufferedImage bi)
	{
		return imageToType(bi, BufferedImage.TYPE_BYTE_GRAY);
	}
	
	public static BufferedImage imageToType(BufferedImage bi, int BufferedImage_dot_Type)
	{
		BufferedImage image = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage_dot_Type);
		image.getGraphics().drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);
		return image;
	}

	public static BufferedImage copy(BufferedImage bi)
	{
		BufferedImage ret = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		ret.getGraphics().drawImage(bi, 0, 0, null);
		return ret;
	}
}
