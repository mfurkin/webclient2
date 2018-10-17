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
	private DataFileReceiver receiver;
	/**
	 * @param anUrlSt
	 */
	public DataFileGetter(String anUrlSt, DataFileReceiver aReceiver) {
		super(anUrlSt);
		receiver = aReceiver;
	}
	@Override
	protected Void doInBackground() throws Exception {
			String lines[];
			BufferedReader br = getBufferedReader();		
			lines = br.lines().toArray(String[]::new);
			receiver.setHeader(Arrays.stream(lines).filter((String st)->{
					return st.startsWith("#");
				}).findFirst().orElse("no header"));
			receiver.setData(Arrays.stream(lines).filter(new StringFilterAsObject((String st)->{
					String nums[] = st.split(" ");
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
		return null;
	}
	@Override
	protected void done() {
		receiver.setTimeLimits();
	}
}
