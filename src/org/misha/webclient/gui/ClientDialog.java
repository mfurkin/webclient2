package org.misha.webclient.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

public class ClientDialog extends JFrame implements FileListGetter, FileDeleter, FileLoader {
	private static final String SELECT_FILE = "Select file";
	private final static String TITLE = "WebClient",
								DELETE_BUTTON_TEXT = "Delete",
								LOAD_BUTTON_TEXT = "Load",
								GET_FILE_BUTTON_TEXT = "Get File",
								GET_LIST_BUTTON_TEXT = "Get Files list";
	private final static String URL_STRING = "http://localhost:8080";
	private static final String DELETE_METHOD = "DELETE";
	private static final String POST_METHOD = "POST";
	private JButton deleteButton,loadButton,getFileButton,getListButton;
	private JComboBox<String> filesCombo;
//	private JViewport viewport;
	private JDrawPanel viewport;
	public ClientDialog() throws HeadlessException {
		super(TITLE);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container  = getContentPane();
		SpringLayout layout = new SpringLayout();
		container.setLayout(layout);
		deleteButton = new JButton(DELETE_BUTTON_TEXT);
		loadButton = new JButton(LOAD_BUTTON_TEXT);
		getFileButton = new JButton(GET_FILE_BUTTON_TEXT);
		
		getListButton = new JButton(GET_LIST_BUTTON_TEXT);

		viewport = new JDrawPanel();
		filesCombo = new JComboBox<String>();
		
		container.add(deleteButton);
		container.add(getFileButton);
		container.add(viewport);
		container.add(filesCombo);
		container.add(getListButton);
		container.add(loadButton);
		filesCombo.setEnabled(false);
		filesCombo.addItem(SELECT_FILE);
		deleteButton.setEnabled(false);
		getFileButton.setEnabled(false);
		
		viewport.setBackground(Color.GREEN);
		
		getListButton.addActionListener(new ListButtonListener(GET_LIST_BUTTON_TEXT,this));
		deleteButton.addActionListener(new DeleteButtonListener(DELETE_BUTTON_TEXT,this));
		loadButton.addActionListener(new LoadButtonListener(LOAD_BUTTON_TEXT,this));
		layout.putConstraint(SpringLayout.NORTH, deleteButton, 20, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.WEST, deleteButton, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.NORTH, getFileButton, 20, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.WEST, getFileButton, 20, SpringLayout.EAST, deleteButton);	
		layout.putConstraint(SpringLayout.NORTH, filesCombo, 20, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.EAST, filesCombo, -20, SpringLayout.EAST, container);
		layout.putConstraint(SpringLayout.NORTH, viewport, 20 , SpringLayout.SOUTH, deleteButton);
		layout.putConstraint(SpringLayout.WEST, viewport, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.SOUTH, viewport, -20 , SpringLayout.NORTH, loadButton);
		layout.putConstraint(SpringLayout.EAST, viewport, -20, SpringLayout.EAST, container);
		layout.putConstraint(SpringLayout.NORTH, getListButton, 20, SpringLayout.SOUTH, viewport);
		layout.putConstraint(SpringLayout.WEST, getListButton, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.SOUTH, loadButton, -20, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.EAST, loadButton, -20, SpringLayout.EAST, container);
		viewport.setSize(300, 300);
//		viewport.setExtentSize(d);
		setSize(1000,700);
	}
	@Override
	public void getFileList() {
		// TODO Auto-generated method stub
		
		URL url = null;
		try { 
			String st;
			url = new URL(URL_STRING);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			for (st = br.readLine();st != null;) {	
				System.out.println("Got line: "+st);
				addLineToCombo(st);
				st = br.readLine();
			}		
			System.out.println("Receive is complete");
			enableCombo();
			enableDeleteButton();
			enableGetFileButton();
		} catch (MalformedURLException ex) {
			System.out.println("Malformed localhost url - как странно :-)...");
		} catch (IOException e) {
			System.out.println("IOException caught in file list getting");
		}
	}
	private void enableGetFileButton() {
		getFileButton.setEnabled(true);
	}
	private void enableDeleteButton() {
		
		deleteButton.setEnabled(true);
	}
	private void enableCombo() {
		if (filesCombo.getItemCount() > 1)
			filesCombo.setEnabled(true);
	}
	private void addLineToCombo(String st) {
		filesCombo.addItem(st);
	}
	@Override
	public void deleteFile() {
		String fname = getFileName();
		if (fname == null)
			JOptionPane.showMessageDialog(null, "File have not been selected", "Error", JOptionPane.ERROR_MESSAGE);
		else {
			
			URL url;
			try {
				url = new URL(URL_STRING + "/" + fname);
				URLConnection conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection)) {
					JOptionPane.showMessageDialog(null, "Not http connection - странно!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					HttpURLConnection conn2 = (HttpURLConnection) conn;
					conn2.setRequestMethod(DELETE_METHOD);
					String title,msg;
					int type;
					int rsp_code = conn2.getResponseCode();
					if (rsp_code == HttpURLConnection.HTTP_OK) {
						title = "Message";
						msg = "file" + fname + " was successful deleted.";
						type = JOptionPane.INFORMATION_MESSAGE;
						disableGetFileButton();
						disableDeleteButton();
						disableCombo();
					} else {
						title = "Error";
						msg = (rsp_code == HttpURLConnection.HTTP_NOT_FOUND) ? "File have not been found" : "FIle could not be deleted";
						type = JOptionPane.ERROR_MESSAGE;
					}
					JOptionPane.showMessageDialog(null, msg, title, type);
				}
			} catch (MalformedURLException e) {
				System.out.println("MalformedURL exception - как странно :-)...");
			} catch (IOException e) {
				System.out.println("IOException has been caught");
			}	
		}
	}
	private void disableCombo() {
		filesCombo.setEnabled(false);
		filesCombo.removeAllItems();
		filesCombo.addItem(SELECT_FILE);
	}
	private void disableDeleteButton() {
		deleteButton.setEnabled(false);
	}
	private void disableGetFileButton() {
		getFileButton.setEnabled(false);
	}
	private String getFileName() {
		String res = null;
		if (filesCombo.isEnabled()) {
			int index = filesCombo.getSelectedIndex();
			if (index > 0)
				res = (String)filesCombo.getSelectedItem();
		}
		return res;
	}
	@Override
	public void fileLoad() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				URL url = new URL(URL_STRING+"/"+file.getName());
				URLConnection conn = url.openConnection();
				if (!(conn instanceof HttpURLConnection))
					JOptionPane.showMessageDialog(null, "Как странно, оно не http", "Error", JOptionPane.ERROR_MESSAGE);
				else {
					int bytes_read,bytes_written = 0,bytes_total = 0, len, avail;
					byte buf[] = new byte [200];
					HttpURLConnection conn2  = (HttpURLConnection) conn;
					conn2.setRequestMethod(POST_METHOD);
					conn2.setDoOutput(true);
					OutputStream os = conn2.getOutputStream();
					FileInputStream fis = new FileInputStream(file);
					len = buf.length;
					for (avail = fis.available(); avail >0;) {
						System.out.println("read off="+bytes_total+" len="+len+" avail="+avail);
						bytes_read = fis.read(buf, 0, avail > len ? len : avail);
						avail = fis.available();
						bytes_total += bytes_read;
						System.out.println("write off="+bytes_total+" len="+bytes_read);
						os.write(buf, 0, bytes_read);
						bytes_written += bytes_read;
					}
					fis.close();
				}
			} catch (MalformedURLException e) {
				System.out.println("Как странно, неправильный адрес...");
			} catch (IOException e) {
				System.out.println("IOException caught");
				e.printStackTrace();
			}
		}
	}
}
