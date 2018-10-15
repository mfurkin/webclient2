package org.misha.webclient.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
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
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class ClientDialog extends JFrame implements FileListGetter, FileDeleter, FileLoader, FileGetter, Drawer {
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
	private JProgressBar progressBar;
	private JDrawPanel viewport;
	private Sample data[];
	private String header;
	private double firstTime;
	private double lastTime;
	private GrSample grSamples[];
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
		header ="No data";
		viewport = new JDrawPanel(this);
		filesCombo = new JComboBox<String>();
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		container.add(progressBar);
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
		getFileButton.addActionListener(new FileButtonListener(GET_FILE_BUTTON_TEXT,this));
		progressBar.setVisible(false);
		layout.putConstraint(SpringLayout.NORTH, deleteButton, 20, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.WEST, deleteButton, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, progressBar, 20, SpringLayout.EAST, getFileButton);
		layout.putConstraint(SpringLayout.EAST, progressBar, -20, SpringLayout.WEST, filesCombo);
		layout.putConstraint(SpringLayout.NORTH, progressBar, 20, SpringLayout.NORTH, container);
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
		setSize(1000,700);
	}
	@Override
	public void getFileList() {
		removeAllFilesFromCombo();
		URL url = null;
		try { 
			String st;
			url = new URL(URL_STRING);
//			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			BufferedReader br = getBufferedReader(url);
			for (st = br.readLine();st != null;) {	
				addLineToCombo(st);
				st = br.readLine();
			}		
			enableCombo();
			enableDeleteButton();
			enableGetFileButton();
		} catch (MalformedURLException ex) {
			JOptionPane.showMessageDialog(null, "Некорректный адрес, странно...", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ошибка сети", "Ошибка", JOptionPane.ERROR_MESSAGE);
		}
	}
	private BufferedReader getBufferedReader(URL url) {
		BufferedReader result = null;
		try {
			result = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ошибка сети", "Ошибка", JOptionPane.ERROR_MESSAGE);
			result = null;
		};
		return result;
	}
	private void removeAllFilesFromCombo() {
		filesCombo.removeAllItems();
		filesCombo.addItem(SELECT_FILE);
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
						msg = "file " + fname + " was successful deleted.";
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
				JOptionPane.showMessageDialog(null, "Incorrect URL", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Some IO error have been detected", "Error", JOptionPane.ERROR_MESSAGE);
			}	
		}
	}
	private void disableCombo() {
		filesCombo.setEnabled(false);
		removeAllFilesFromCombo();

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
					int bytes_read,bytes_written = 0,len, avail;
					byte buf[] = new byte [200];
					String title,msg;
					int type;
					HttpURLConnection conn2  = (HttpURLConnection) conn;
					conn2.setRequestMethod(POST_METHOD);
					conn2.setDoOutput(true);
					OutputStream os = conn2.getOutputStream();
					FileInputStream fis = new FileInputStream(file);
					len = buf.length;
					avail = fis.available();
					setupProgressBar(avail);
					for (;avail >0;) {
						
						bytes_read = fis.read(buf, 0, avail > len ? len : avail);
						avail = fis.available();						
						os.write(buf, 0, bytes_read);
						bytes_written += bytes_read;
						updateProgressBar(bytes_written);
					}
					hideProgressBar();
					fis.close();
					int rsp_code = conn2.getResponseCode();
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
				}
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(null, "Как странно, неправильный адрес...", "Ошибка", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Ошибка сети", "Ошибка",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	private void hideProgressBar() {
		progressBar.setVisible(false);
	}
	private void updateProgressBar(int bytes_written) {
		progressBar.setValue(bytes_written);
	}
	private void setupProgressBar(int avail) {
		progressBar.setMinimum(0);
		progressBar.setMaximum(avail);
		progressBar.setVisible(true);
	}
	@Override
	public void getFile() {
		String fname = getFileName();
		System.out.println("getFile enter fname="+fname);
		if (fname == null)
			JOptionPane.showMessageDialog(null, "File have not been selected", "Error", JOptionPane.ERROR_MESSAGE);
		else {
			try {
				String lines[];
				System.out.println("getFile try block enter");
				URL url = new URL(URL_STRING+"/"+fname);
				System.out.println("getFIle after url getting");
				BufferedReader br = getBufferedReader(url);		
				System.out.println("getFile after BuffereReader creation");
				lines = br.lines().toArray(String[]::new);
				System.out.println("getFile after lines getting");
				header = Arrays.stream(lines).filter((String st)->{return st.startsWith("#");}).findFirst().orElse("no header");
				System.out.println("getFile after header getting");
				data = Arrays.stream(lines).filter((Object obj)->{
					boolean result = false;
					if (obj instanceof String) {
						String st = (String) obj;
						String nums[] = st.split(" ");
						result = Arrays.stream(nums).allMatch((Object ob)-> {
							boolean res = false;
							if (ob instanceof String) {
								String s = (String) ob;
								try {
									Double.parseDouble(s);
									res = true;
								} catch (NumberFormatException ex) {
									res = false;
								}
							}
							return res;
						});
					}
					return result;
				}).map((String st)-> {
					int i;
					String nums[] = st.split(" ");
					System.out.println("----------------------------------------------------------");
					System.out.println("lambda st="+st);
					for (i=0;i<nums.length;)
						System.out.println("cur st="+nums[i++]);
					System.out.println("----------------------------------------------------------");
					return new Sample(Double.parseDouble(nums[0]),Double.parseDouble(nums[1]));
				}).toArray(Sample[]::new);
				System.out.println("getFile after data getting");
				convertToGraphics();
				System.out.println("getFile after conversion data");
				repaintViewport();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void repaintViewport() {
		Rectangle rect = viewport.getVisibleRect();
		viewport.repaint(rect);		
	}
	private void convertToGraphics() {
		int minWidth,maxWidth,minHeight,maxHeight;
		int delta = 20;
		minWidth = delta;
		minHeight = delta;
		maxWidth = viewport.getWidth()- delta;
		maxHeight = viewport.getHeight() - delta;
		firstTime = Sample.minTime(data);
		lastTime = Sample.maxTime(data);
		grSamples = Sample.convert(Arrays.stream(data).filter((Sample s)->{
			return s.betweenTimes(firstTime, lastTime);
		}).toArray(Sample[]::new), minWidth, maxWidth, minHeight, maxHeight);
	}
	@Override
	public void draw(Graphics g) {
		char chars[] = header.toCharArray();
		g.drawChars(header.toCharArray(), 0, chars.length, 15, 15);
		if (grSamples == null)
			System.out.println("draw - grSamples is null!");
		else {
			int i,len = grSamples.length - 1;
			System.out.println("draw len="+len);
			for (i=0;i<len;i++)
				grSamples[i].drawTo(g, grSamples[i+1]);
		}
	}
}
