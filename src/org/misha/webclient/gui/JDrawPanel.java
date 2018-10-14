package org.misha.webclient.gui;

import java.awt.Graphics;

import javax.swing.JPanel;

public class JDrawPanel extends JPanel {

	public JDrawPanel() {
		
	}

	@Override
	public void paint(Graphics gr) {
		// TODO Auto-generated method stub
		super.paint(gr);
		gr.drawLine(30, 40, 100, 150);
	}
	
	
}
