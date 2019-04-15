package org.misha.webclient.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class ClientDialog extends JFrame implements Drawer, GraphicShower, GraphicsAdder, 
													FirstTimeChecker,LastTimeChecker, DataFileReceiver {
	private static final String SELECT_FILE = "Select graph";
	private final static String TITLE = "GraphShower",
								SHOW_GRAPHIC_BUTTON_TEXT = "Show graphic";

	private static final String FIRST_TIME_BUTTON_TEXT = "Set begin time";
	private static final String LAST_TIME_BUTTON_TEXT = "Set end time";
	private JButton showGraphicButton,firstTimeButton,lastTimeButton;
	private JTextField firstTimeField,lastTimeField;
	private JComboBox<String> graphsCombo;
	private JProgressBar progressBar;
	private JDrawPanel viewport;
	private Sample data[];
	private String header;
	private int grIndex;
	private double firstTime;
	private double lastTime;
	private GrSample grSamples[];
	private DataFileGetter dfgetter;
	public ClientDialog(String aFname) throws HeadlessException {
		super(TITLE);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container  = getContentPane();
		SpringLayout layout = new SpringLayout();
		container.setLayout(layout);
		showGraphicButton = new JButton(SHOW_GRAPHIC_BUTTON_TEXT);
		firstTimeButton = new JButton(FIRST_TIME_BUTTON_TEXT);
		firstTimeField = new JTextField(15);
		lastTimeField = new JTextField(15);
		lastTimeButton = new JButton(LAST_TIME_BUTTON_TEXT);
		header ="No data";
		viewport = new JDrawPanel(this);
		graphsCombo = new JComboBox<String>();
		progressBar = new JProgressBar(SwingConstants.HORIZONTAL);
		container.add(progressBar);
		container.add(showGraphicButton);
		container.add(viewport);
		container.add(graphsCombo);
		container.add(firstTimeButton);
		container.add(lastTimeButton);
		container.add(firstTimeField);
		container.add(lastTimeField);
		graphsCombo.setEnabled(false);
		graphsCombo.addItem(SELECT_FILE);
		showGraphicButton.setEnabled(false);
		
		viewport.setBackground(Color.GREEN);
		
		showGraphicButton.addActionListener(new ShowGraphicButtonListener(SHOW_GRAPHIC_BUTTON_TEXT,this));
		firstTimeButton.addActionListener(new FirstTimeButtonListener(FIRST_TIME_BUTTON_TEXT,this));
		lastTimeButton.addActionListener(new LastTimeButtonListener(LAST_TIME_BUTTON_TEXT,this));
		progressBar.setVisible(false);
		layout.putConstraint(SpringLayout.NORTH, showGraphicButton, 20, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.WEST, showGraphicButton, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.WEST, progressBar, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.EAST, progressBar, -20, SpringLayout.WEST, graphsCombo);
		layout.putConstraint(SpringLayout.NORTH, progressBar, 20, SpringLayout.NORTH, container);
		
		layout.putConstraint(SpringLayout.NORTH, graphsCombo, 20, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.EAST, graphsCombo, -20, SpringLayout.EAST, container);

		layout.putConstraint(SpringLayout.WEST, firstTimeButton, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.SOUTH, firstTimeButton, -20, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.SOUTH, firstTimeField, -20, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.WEST, firstTimeField, 20, SpringLayout.EAST, firstTimeButton);
		layout.putConstraint(SpringLayout.NORTH, viewport, 20 , SpringLayout.SOUTH, showGraphicButton);
		layout.putConstraint(SpringLayout.WEST, viewport, 20, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.SOUTH, viewport, -20 , SpringLayout.NORTH, firstTimeButton);
		layout.putConstraint(SpringLayout.EAST, viewport, -20, SpringLayout.EAST, container);
		layout.putConstraint(SpringLayout.NORTH, lastTimeField, 20, SpringLayout.SOUTH, viewport);
		layout.putConstraint(SpringLayout.EAST, lastTimeField, -20, SpringLayout.WEST, lastTimeButton);
		layout.putConstraint(SpringLayout.NORTH, lastTimeButton, 20, SpringLayout.SOUTH, viewport);
		layout.putConstraint(SpringLayout.EAST, lastTimeButton, -20, SpringLayout.EAST, container);
		grIndex = -1;
		viewport.setSize(300, 300);
		setSize(1000,700);
		getFile(aFname);
	}

	public void enableOtherOperations() {
		enableCombo();
		enableShowGraphicButton();
	}
/*		
	private void removeAllFilesFromCombo() {
		graphsCombo.removeAllItems();
		graphsCombo.addItem(SELECT_FILE);
	}
*/	
	private void enableShowGraphicButton() {
		showGraphicButton.setEnabled(true);
	}
	
	private void enableCombo() {
		if (graphsCombo.getItemCount() > 1)
			graphsCombo.setEnabled(true);
	}
	
	@Override
	public void addLineToCombo(String st) {
		graphsCombo.addItem(st);
	}
/*	
	private void disableCombo() {
		graphsCombo.setEnabled(false);
		removeAllFilesFromCombo();

	}
	
	private String getFileName() {
		String res = null;
		if (graphsCombo.isEnabled()) {
			int index = graphsCombo.getSelectedIndex();
			if (index > 0)
				res = (String)graphsCombo.getSelectedItem();
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
*/	
/*	
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
*/	
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
	
	private void getFile(String fname) {
//		String fname = getFileName();
/*		
		if (fname == null)
			JOptionPane.showMessageDialog(null, "File have not been selected", "Error", JOptionPane.ERROR_MESSAGE);
		else {
*/		
		dfgetter = new DataFileGetter(fname,this,this);
		dfgetter.execute();
		try {
			dfgetter.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
//		}
	}
	private void repaintViewport() {
		Rectangle rect = viewport.getVisibleRect();
		viewport.repaint(rect);		
	}
	
	private int getGraphicIndex() {
		int result = graphsCombo.getSelectedIndex();
		return result - 1;
	}
	private void convertToGraphics() {
		int minWidth,maxWidth,minHeight,maxHeight;
		int delta = 20;
		minWidth = delta;
		minHeight = delta;
		maxWidth = viewport.getWidth()- delta;
		maxHeight = viewport.getHeight() - delta;	
		grIndex = getGraphicIndex();
		if (grIndex >=0) {
			grSamples = Sample.convert(Arrays.stream(data).filter((Sample s)->{
				return s.betweenTimes(firstTime, lastTime);
			}).toArray(Sample[]::new), minWidth, maxWidth, minHeight, maxHeight,grIndex);
			repaintViewport();
		}
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
	@Override
	public void showGraphic() {
		setTimeLimits();
	}
}
