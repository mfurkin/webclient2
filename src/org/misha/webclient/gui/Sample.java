package org.misha.webclient.gui;

import java.util.Arrays;
import java.util.Comparator;

public class Sample {
	private double time;
	double [] values;
		public Sample(double aTime, double [] aValues) {
			time = aTime;
			values = aValues;
		}
		private int calc(double x, double minX, double maxX, int minCoord, int maxCoord) {
			double result = (double)minCoord + ((x - minX)/(maxX-minX))*((double)(maxCoord-minCoord));
			return (int)result;
		}
		public boolean betweenTimes(double minTime, double maxTime) {
			return time >= minTime && time <= maxTime;
		}
		public GrSample  getGrPoint(int minWidth, int maxWidth, int minHeight, int maxHeight, 
									double minTime, double maxTime, double minValue, 
									double maxValue, int grIndex) {
			int res_x = calc(time,minTime,maxTime,minWidth,maxWidth);
			int res_y = maxHeight - calc(values[grIndex],minValue,maxValue,minHeight,maxHeight) + minHeight;
			return new GrSample(res_x,res_y);
		}
		public static double minTime(Sample[] samples) {
			return samples[0].time;
		}
		public static double maxTime(Sample[] samples) {
			return samples[samples.length-1].time;
		}
		public static GrSample[] convert(Sample [] samples, int minWidth, int maxWidth, int minHeight, int maxHeight, int grIndex) {
			double minTime = minTime(samples), maxTime = maxTime(samples);
			Comparator<Sample> comparator =  (Sample s1, Sample s2)->{
				return (s1.values[grIndex] < s2.values[grIndex]) ? -1 : (s1.values[grIndex] == s2.values[grIndex]) ? 0 : 1;
			};
			
			double  minValue = Arrays.stream(samples).min(comparator).get().values[grIndex],
					maxValue = Arrays.stream(samples).max(comparator).get().values[grIndex];
			return Arrays.stream(samples).map((Sample sample)-> {
				return sample.getGrPoint(minWidth, maxWidth, minHeight, maxHeight, minTime, maxTime, minValue, maxValue, grIndex);
			}).toArray(GrSample[]::new);
		}
}
