/**
 * 
 */
package org.misha.webclient.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.JOptionPane;

/**
 * @author mikhailf
 *
 */
public class ListFileGetter extends DataGetter {
	private ListFileAdder adder;
	public ListFileGetter(String anUrlSt, ListFileAdder anAdder) {
		super(anUrlSt);
		adder = anAdder;
	}
	@Override
	protected Void doInBackground() throws Exception {
		try { 
			String st;
			BufferedReader br = getBufferedReader();
			for (st = br.readLine();st != null;) {	
				System.out.println("doInBackGround st="+st);
				publish(st);
				st = br.readLine();
			}		
		} catch (MalformedURLException ex) {
			JOptionPane.showMessageDialog(null, "Некорректный адрес, странно...", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ошибка сети", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	@Override
	protected void done() {
		adder.enableOtherOperations();
	}
	@Override
	protected void process(List<String> chunks) {
		String st = chunks.get(0);
		System.out.println("process st="+st+" size="+chunks.size());
		chunks.forEach((String str)->{
			adder.addLineToCombo(str);
		});
	}
}
