package org.misha.webclient.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class ClientDialog extends JFrame implements FileListGetter, FileDeleter, FileLoader, FileGetter, Drawer, 
													FirstTimeChecker,LastTimeChecker, ListFileAdder,DataFileReceiver, DataFileSender, FileRemover {
	private static final String SELECT_FILE = "Select file";
	private final static String TITLE = "WebClient",
								DELETE_BUTTON_TEXT = "Delete",
								LOAD_BUTTON_TEXT = "Load",
								GET_FILE_BUTTON_TEXT = "Get File",
								GET_LIST_BUTTON_TEXT = "Get Files list";
	private final static String URL_STRING = "http://localhost:8080";
	private static final String FIRST_TIME_BUTTON_TEXT = "Set begin time";
	private static final String LAST_TIME_BUTTON_TEXT = "Set end time";
	private JButton deleteButton,loadButton,getFileButton,getListButton,firstTimeButton,lastTimeButton;
	private JTextField firstTimeField,lastTimeField;
	private JComboBox<String> filesCombo;
	private JProgressBar progressBar;
	private JDrawPanel viewport;
	private Sample data[];
	private String header;
	private double firstTime;
	private double lastTime;
	private GrSample grSamples[];
	private ListFileGetter getter;
	private DataFileGetter dfgetter;
	private DataFileLoader fileLoader;
	private DataFileDeleter fdeleter;
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
		firstTimeButton = new JButton(FIRST_TIME_BUTTON_TEXT);
		firstTimeField = new JTextField(15);
		lastTimeField = new JTextField(15);
		lastTimeButton = new JButton(LAST_TIME_BUTTON_TEXT);
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
		container.add(firstTimeButton);
		container.add(lastTimeButton);
		container.add(firstTimeField);
		container.add(lastTimeField);
		filesCombo.setEnabled(false);
		filesCombo.addItem(SELECT_FILE);
		deleteButton.setEnabled(false);
		getFileButton.setEnabled(false);
		
		viewport.setBackground(Color.GREEN);
		
		getListButton.addActionListener(new ListButtonListener(GET_LIST_BUTTON_TEXT,this));
		deleteButton.addActionListener(new DeleteButtonListener(DELETE_BUTTON_TEXT,this));
		loadButton.addActionListener(new LoadButtonListener(LOAD_BUTTON_TEXT,this));
		getFileButton.addActionListener(new FileButtonListener(GET_FILE_BUTTON_TEXT,this));
		firstTimeButton.addActionListener(new FirstTimeButtonListener(FIRST_TIME_BUTTON_TEXT,this));
		lastTimeButton.addActionListener(new LastTimeButtonListener(LAST_TIME_BUTTON_TEXT,this));
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
		layout.putConstraint(SpringLayout.NORTH, firstTimeButton, 20, SpringLayout.SOUTH, viewport);
		layout.putConstraint(SpringLayout.WEST, firstTimeButton, 20, SpringLayout.EAST, getListButton);
		layout.putConstraint(SpringLayout.NORTH, firstTimeField, 20, SpringLayout.SOUTH, viewport);
		layout.putConstraint(SpringLayout.WEST, firstTimeField, 20, SpringLayout.EAST, firstTimeButton);
		layout.putConstraint(SpringLayout.SOUTH, loadButton, -20, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.EAST, loadButton, -20, SpringLayout.EAST, container);
		layout.putConstraint(SpringLayout.NORTH, lastTimeField, 20, SpringLayout.SOUTH, viewport);
		layout.putConstraint(SpringLayout.EAST, lastTimeField, -20, SpringLayout.WEST, lastTimeButton);
		layout.putConstraint(SpringLayout.NORTH, lastTimeButton, 20, SpringLayout.SOUTH, viewport);
		layout.putConstraint(SpringLayout.EAST, lastTimeButton, -20, SpringLayout.WEST, loadButton);
		viewport.setSize(300, 300);
		setSize(1000,700);
	}
	@Override
	public void enableOtherOperations() {
		enableCombo();
		enableDeleteButton();
		enableGetFileButton();	
	}
	@Override
	public void getFileList() {
		removeAllFilesFromCombo();
		getter = new ListFileGetter(URL_STRING,this);
		getter.execute();
		try {
			getter.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
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
	@Override
	public void addLineToCombo(String st) {
		filesCombo.addItem(st);
	}
	@Override
	public void deleteFile() {
		String fname = getFileName();
		if (fname == null)
			JOptionPane.showMessageDialog(null, "File have not been selected", "Error", JOptionPane.ERROR_MESSAGE);
		else {
			fdeleter = new DataFileDeleter(URL_STRING,fname,this);
			fdeleter.execute();
			try {
				fdeleter.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void disableCombo() {
		filesCombo.setEnabled(false);
		removeAllFilesFromCombo();

	}
	@Override
	public void disableDeleteButton() {
		deleteButton.setEnabled(false);
	}
	@Override
	public void disableGetFileButton() {
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
			fileLoader = new DataFileLoader(URL_STRING,file,this);
			fileLoader.execute();
			try {
				fileLoader.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void hideProgressBar() {
		progressBar.setVisible(false);
	}
	@Override
	public void updateProgressBar(int bytes_written) {
		progressBar.setValue(bytes_written);
	}
	@Override
	public void setupProgressBar(int avail) {
		progressBar.setMinimum(0);
		progressBar.setMaximum(avail);
		progressBar.setVisible(true);
	}
	@Override
	public void setHeader(String aHeader) {
		header = aHeader;
	}
	@Override
	public void setData(Sample[] aData) {
		data = aData;
	}
	@Override 
	public void setTimeLimits() {
		firstTime = Sample.minTime(data);
		lastTime = Sample.maxTime(data);
		setTimeToField(firstTimeField,firstTime);
		setTimeToField(lastTimeField,lastTime);
		convertToGraphics();
	}
	@Override
	public void getFile() {
		String fname = getFileName();
		if (fname == null)
			JOptionPane.showMessageDialog(null, "File have not been selected", "Error", JOptionPane.ERROR_MESSAGE);
		else {
				dfgetter = new DataFileGetter(URL_STRING+"/"+fname,this);
				dfgetter.execute();
				try {
					dfgetter.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
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
		grSamples = Sample.convert(Arrays.stream(data).filter((Sample s)->{
			return s.betweenTimes(firstTime, lastTime);
		}).toArray(Sample[]::new), minWidth, maxWidth, minHeight, maxHeight);
		repaintViewport();
	}
	
	private void setTimeToField(JTextField textField, double firstTime) {
		textField.setText(Double.toString(firstTime));
	}
	@Override
	public void draw(Graphics g) {
		char chars[] = header.toCharArray();
		g.drawChars(header.toCharArray(), 0, chars.length, 15, 15);
		if (grSamples != null)
		{
			int i,len = grSamples.length - 1;
			for (i=0;i<len;i++)
				grSamples[i].drawTo(g, grSamples[i+1]);
		}
	}
	@Override
	public void checkAndInputFirstTimeLimit() {
		firstTime = timeInput(firstTime,firstTimeField.getText());
		setTimeToField(firstTimeField, firstTime);
		convertToGraphics();
	}
	private double timeInput(double prevValue, String st) {
		double result;
		try {
			result = Double.parseDouble(st);
		} catch (NumberFormatException ex) {
			result = prevValue;
		}
		return result;
	}
	@Override
	public void checkAndInputLastTimeLimit() {
		lastTime = timeInput(lastTime,lastTimeField.getText());
		setTimeToField(lastTimeField,lastTime);
		convertToGraphics();
	}
}
