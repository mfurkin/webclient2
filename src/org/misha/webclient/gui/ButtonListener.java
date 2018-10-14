package org.misha.webclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ButtonListener implements ActionListener {
	private String text;
	public ButtonListener(String aText) {
		text = aText;
	}
	protected abstract void makeCallback();
	@Override
	public void actionPerformed(ActionEvent ev) {
		// TODO Auto-generated method stub
		String st = ev.getActionCommand();
		if (st.equals(text))
			makeCallback();
	}

}
