package org.misha.webclient.gui;

import java.awt.Graphics;

public class GrSample {
	private int x, y;
	public GrSample(int aX, int anY) {
		x = aX;
		y = anY;
	}
	public void drawTo(Graphics g, GrSample nextPt) {
		System.out.println("drawTo: x="+Integer.toString(x)+" y="+Integer.toString(y));
		nextPt.drawFrom(g,x,y);
	}
	public void drawFrom(Graphics g, int prevX, int  prevY) {
		System.out.println("drawFrom prevX="+Integer.toString(prevX)+" prevY="+Integer.toString(prevY)+" x="+Integer.toString(x)+" y="+Integer.toString(y));
		g.drawLine(prevX, prevY, x, y);
	}
}
