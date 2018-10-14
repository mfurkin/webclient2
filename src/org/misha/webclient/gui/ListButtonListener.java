package org.misha.webclient.gui;

import java.awt.event.ActionListener;
public class ListButtonListener extends ButtonListener implements ActionListener {
	String text;
	FileListGetter getter;
	
	public ListButtonListener(String aText, FileListGetter aGetter) {
		super(aText);
		getter = aGetter;
	}

	@Override
	protected void makeCallback() {
		getter.getFileList();
	}

	

	
}
