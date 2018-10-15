package org.misha.webclient.gui;

public class FirstTimeButtonListener extends ButtonListener {
	private FirstTimeChecker checker;
	public FirstTimeButtonListener(String aText, FirstTimeChecker aChecker) {
		super(aText);
		checker = aChecker;
	}

	@Override
	protected void makeCallback() {
		checker.checkAndInputFirstTimeLimit();
	}

}
