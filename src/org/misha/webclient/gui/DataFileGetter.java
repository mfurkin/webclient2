/**
 * 
 */
package org.misha.webclient.gui;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

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
		// TODO Auto-generated constructor stub
	}
	@Override
	protected Void doInBackground() throws Exception {
			String lines[];
			BufferedReader br = getBufferedReader();		
			lines = br.lines().toArray(String[]::new);
			receiver.setHeader(Arrays.stream(lines).filter((String st)->{
					return st.startsWith("#");
				}).findFirst().orElse("no header"));
			receiver.setData(Arrays.stream(lines).filter((Object obj)->{
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
