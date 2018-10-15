package org.misha.webclient.gui;

public class FileButtonListener extends ButtonListener {
	private FileGetter getter;
	public FileButtonListener(String aText, FileGetter aGetter) {
		super(aText);
		getter = aGetter;
	}

	@Override
	protected void makeCallback() {
		getter.getFile();
	}

}
