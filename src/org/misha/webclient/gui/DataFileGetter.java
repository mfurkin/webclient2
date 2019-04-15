/**
 * 
 */
package org.misha.webclient.gui;

import java.io.BufferedReader;
import java.util.Arrays;

/**
 * @author mikhailf
 *
 */
public class DataFileGetter extends DataGetter {
	private static double DTIME = 9.3749707032165524482735991450027E-3; // in ms;
	private DataFileReceiver receiver;
	private GraphicsAdder adder;
	/**
	 * @param anUrlSt
	 */
	public DataFileGetter(String anUrlSt, DataFileReceiver aReceiver, GraphicsAdder anAdder) {
		super(anUrlSt);
		receiver = aReceiver;
		adder = anAdder;
	}
	private void addAllCols(int num) {
		int i;
		String st = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (i=0;i<num;i++)
			adder.addLineToCombo(st.substring(i, i+1));
	}
	@Override
	protected Void doInBackground() throws Exception {
			String lines[];
			int i;
			BufferedReader br = getBufferedReader();		
			lines = br.lines().toArray(String[]::new);
			String [] cols = Arrays.stream(lines).findFirst().get().split(";");
			addAllCols(cols.length);
			Sample [] samples = new Sample [lines.length];
			for (i=0;i<lines.length;) {
				double [] values = Arrays.stream(lines[i].split(";")).mapToDouble((String st) -> {
					double result;
					try {
						result = Double.parseDouble(st);
					} catch (NumberFormatException ex) {
						result = 0;
					}		
					return result;
				}).toArray();
				double time = i*DTIME;
				samples[i++] = new Sample(time,values);
			}
			receiver.setData(samples);
/*			
			receiver.setData(Arrays.stream(lines).filter(new StringFilterAsObject((String st)->{
					String nums[] = st.split(";");
					return Arrays.stream(nums).allMatch(new StringFilterAsObject((String s2)-> {
						boolean res;
						try {
							Double.parseDouble(s2);
							res = true;
						} catch (NumberFormatException ex) {
							res = false;
						}
						return res;
					}));	
				})).map((String st)-> {
					String nums[] = st.split(" ");
					return new Sample(Double.parseDouble(nums[0]),Double.parseDouble(nums[1]));
				}).toArray(Sample[]::new));		
			receiver.setTimeLimits();
*/			
		return null;
	}
	@Override
	protected void done() {
		receiver.setTimeLimits();
		adder.enableOtherOperations();
	}
}
