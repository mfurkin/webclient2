package org.misha.webclient.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class DataFileLoader extends SwingWorker<Void, Integer> {
	private String url_st;
	private File file;
	private DataFileSender sender;
	private static final String POST_METHOD = "POST";
	private FileInputStream fis;
	private int rsp_code;
	public DataFileLoader(String anUrlSt, File aFile, DataFileSender aSender) {
		url_st = anUrlSt;
		file = aFile;
		sender = aSender;
		fis = null;
		rsp_code = 0;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
		try {
			String url2 = url_st+"/"+file.getName();
			
			URL url = new URL(url2);
			
			URLConnection conn = url.openConnection();
			
			if (!(conn instanceof HttpURLConnection))
				JOptionPane.showMessageDialog(null, "Как странно, оно не http", "Error", JOptionPane.ERROR_MESSAGE);
			else {
				
				int bytes_read,bytes_written = 0,len, avail;
				byte buf[] = new byte [200];
				HttpURLConnection conn2  = (HttpURLConnection) conn;
				conn2.setRequestMethod(POST_METHOD);
				
				conn2.setDoOutput(true);
		
				OutputStream os = conn2.getOutputStream();
				fis = new FileInputStream(file);
				len = buf.length;
				avail = fis.available();
				publish(avail);
				sender.setupProgressBar(avail);
				for (;avail >0;) {
					bytes_read = fis.read(buf, 0, avail > len ? len : avail);
					avail = fis.available();						
					os.write(buf, 0, bytes_read);
					bytes_written += bytes_read;					
					publish(bytes_written);
				}
				rsp_code = conn2.getResponseCode();
			}
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "Как странно, неправильный адрес...", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ошибка сети", "Ошибка",JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	@Override
	protected void done() {
		sender.hideProgressBar();
		String title,msg;
		int type;
		if (rsp_code == HttpURLConnection.HTTP_CREATED) {
			title = "Message";
			msg = "File "+ file.getName() + " was successfully transferred.";
			type = JOptionPane.INFORMATION_MESSAGE;
		} else {
			title = "Error";
			msg = "File could not be created";
			type = JOptionPane.ERROR_MESSAGE;
		}
		JOptionPane.showMessageDialog(null, msg, title, type);
		try {
			if (fis != null)
				fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void process(List<Integer> chunks) {
		sender.updateProgressBar(chunks.get(chunks.size()-1));
	}

	

}
