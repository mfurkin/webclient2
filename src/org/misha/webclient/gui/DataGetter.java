/**
 * 
 */
package org.misha.webclient.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * @author mikhailf
 *
 */
public abstract class DataGetter extends SwingWorker<Void, String> {
	private String fname;
	
	public DataGetter(String aFname) {
		super();
		fname = aFname;
	}

	protected BufferedReader getBufferedReader() {
		
		BufferedReader result = null;
		try {
			File file = new File(fname);
			result = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ошибка ввода", "Ошибка", JOptionPane.ERROR_MESSAGE);
			result = null;
		} 
		return result;
	}

}
