package org.ploxie.bezier;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Window extends JFrame{

	private static final long serialVersionUID = 1867247229546350551L;
	
	private Canvas canvas;
	
	public Window(int width, int height, String title) {		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		
		setLocation(x,y);
		setSize(width, height);
		setTitle(title);
		
		canvas = new Canvas();
		
		add(canvas);
		
	}
	
}
