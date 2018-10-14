package org.misha.webclient.gui;

public class LoadButtonListener extends ButtonListener {
	private FileLoader loader;

	public LoadButtonListener(String aText, FileLoader aLoader) {
		super(aText);
		loader = aLoader;
	}

	@Override
	protected void makeCallback() {
		loader.fileLoad();
	}

}
