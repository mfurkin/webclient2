/**
 * 
 */
package org.misha.webclient.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * @author mikhailf
 *
 */
public abstract class DataGetter extends SwingWorker<Void, String> {
	private String url_st;
	
	public DataGetter(String anUrlSt) {
		super();
		url_st = anUrlSt;
	}

	protected BufferedReader getBufferedReader() {
		
		BufferedReader result = null;
		try {
			URL url =  new URL(url_st);
			result = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (MalformedURLException ex) {
			JOptionPane.showMessageDialog(null, "Некорректный адрес, странно...", "Ошибка", JOptionPane.ERROR_MESSAGE);
			result = null;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ошибка сети", "Ошибка", JOptionPane.ERROR_MESSAGE);
			result = null;
		} 
		return result;
	}

}
