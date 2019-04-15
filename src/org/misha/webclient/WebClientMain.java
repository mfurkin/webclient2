package org.misha.webclient;

import org.misha.webclient.gui.ClientDialog;

public class WebClientMain {

	public static void main(String[] args) {
		for (String st : args)
			System.out.println("arg="+st);
		ClientDialog dlg = new ClientDialog(args[0]);
		dlg.setVisible(true);
	}

}
