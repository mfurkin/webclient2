package org.misha.webclient.gui;

import java.awt.Graphics;

public class GrSample {
	private int x, y;
	public GrSample(int aX, int anY) {
		x = aX;
		y = anY;
	}
	public void drawTo(Graphics g, GrSample nextPt) {
		nextPt.drawFrom(g,x,y);
	}
	public void drawFrom(Graphics g, int prevX, int  prevY) {
		g.drawLine(prevX, prevY, x, y);
	}
}
