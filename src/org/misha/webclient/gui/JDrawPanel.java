package org.misha.webclient.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

public class JDrawPanel extends JPanel {
	private Drawer drawer;
	public JDrawPanel(Drawer aDrawer) {
		drawer = aDrawer;
	}

	@Override
	public void paint(Graphics gr) {
		super.paint(gr);
		drawer.draw(gr);
//		gr.drawLine(30, 40, 100, 150);
	}
	
	
}
