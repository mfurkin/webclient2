package org.misha.webclient.gui;

public class DeleteButtonListener extends ButtonListener {
	private FileDeleter deleter;
	public DeleteButtonListener(String aText, FileDeleter aDeleter) {
		super(aText);
		deleter = aDeleter;
	}
	@Override
	protected void makeCallback() {
		deleter.deleteFile();
	}

}
