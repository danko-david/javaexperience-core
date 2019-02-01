package eu.javaexperience.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextToImageRender
{
	protected final int lineHeight;
	protected final int charWidth;
	
	protected int fontSize;
	
	protected Font getFont()
	{
		return new Font(Font.MONOSPACED, Font.PLAIN, this.fontSize);
	}
	
	public TextToImageRender(int fontSize)
	{
		this.fontSize = fontSize;
		BufferedImage IM = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);
		IM.getGraphics().setFont(getFont());
		lineHeight = IM.getGraphics().getFontMetrics().getHeight();
		
		/*
		int w = 0;
		int[] ws = IM.getGraphics().getFontMetrics().getWidths();
		for(int i=0;i<ws.length;++i)
		{
			w = Math.max(w, ws[i]);
		}
		*/
		
		charWidth = (int)
			//w
			(fontSize*0.5);
		;
	}
	
	public void renderText(String dstFile, String format, String text) throws IOException
	{
		String[] lines = text.split("\n");
		
		int maxWidth = 0;
		int maxHeight = (lines.length+1)*lineHeight;
		for(String s:lines)
		{
			maxWidth = Math.max(maxWidth, s.length());
		}
		
		maxWidth *= charWidth+1;
		
		BufferedImage image = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_BYTE_GRAY);
		
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, maxWidth, maxHeight);
		g.setColor(Color.BLACK);
		
		image.getGraphics().setFont(getFont());
		
		for(int lineCount = 0; lineCount < lines.length; lineCount ++)
		{
			int lineOffset = (lineCount+1) * lineHeight;
			//g.drawString(lines[lineCount], 0, lineOffset);
			String line = lines[lineCount];
			for(int i = 0;i<line.length();++i)
			{
				g.drawString(String.valueOf(line.charAt(i)), charWidth*i, lineOffset);
			}
		}
		g.dispose();
		
		
		int maxX = 0;
		int maxY = 0;
		
		for(int w=0;w<maxWidth;w++)
		{
			for(int h=0;h<maxHeight;h++)
			{
				int px = image.getRGB(w, h);
				if(255 != (px & 0xff))
				{
					maxX = Math.max(maxX, w);
					maxY = Math.max(maxY, h);
				}
			}
		}
		
		BufferedImage img =
			//image;
			image.getSubimage(0, 0, Math.min(maxX+charWidth, image.getWidth()), Math.min(maxY+lineHeight, image.getHeight()));
		ImageIO.write(img, format, new File(dstFile));
	}
}
