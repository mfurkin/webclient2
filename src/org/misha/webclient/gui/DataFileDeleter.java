package org.misha.webclient.gui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class DataFileDeleter extends SwingWorker<Void, String> {
	private String url_st,fname;
	private FileRemover fdeleter;
	int rsp_code;
	private static final String DELETE_METHOD = "DELETE";
	public DataFileDeleter(String anUrlSt, String aFname, FileRemover aFdeleter) {
		super();
		url_st = anUrlSt;
		fname = aFname;
		fdeleter = aFdeleter;
		rsp_code = 0;
	}
	@Override
	protected Void doInBackground() throws Exception {
		URL url;
		try {
			url = new URL(url_st + "/" + fname);
			URLConnection conn = url.openConnection();
			if (!(conn instanceof HttpURLConnection)) {
				JOptionPane.showMessageDialog(null, "Not http connection - странно!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				HttpURLConnection conn2 = (HttpURLConnection) conn;
				conn2.setRequestMethod(DELETE_METHOD);
				rsp_code = conn2.getResponseCode();
			}
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "Incorrect URL", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Some IO error have been detected", "Error", JOptionPane.ERROR_MESSAGE);
		}	
		return null;
	}
	@Override
	protected void done() {
		String title,msg;
		int type;
		if (rsp_code == HttpURLConnection.HTTP_OK) {
			title = "Message";
			msg = "file " + fname + " was successful deleted.";
			type = JOptionPane.INFORMATION_MESSAGE;
			fdeleter.disableGetFileButton();
			fdeleter.disableDeleteButton();
			fdeleter.disableCombo();
		} else {
			title = "Error";
			msg = (rsp_code == HttpURLConnection.HTTP_NOT_FOUND) ? "File have not been found" : "FIle could not be deleted";
			type = JOptionPane.ERROR_MESSAGE;
		}
		JOptionPane.showMessageDialog(null, msg, title, type);
	}
	
}
