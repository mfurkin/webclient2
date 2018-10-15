package org.misha.webclient.gui;

public class LastTimeButtonListener extends ButtonListener {
	private LastTimeChecker checker;
	public LastTimeButtonListener(String aText, LastTimeChecker aChecker) {
		super(aText);
		checker = aChecker;
	}

	@Override
	protected void makeCallback() {
		checker.checkAndInputLastTimeLimit();
	}

}
