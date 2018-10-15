package org.misha.webclient.gui;

import java.util.Arrays;
import java.util.Comparator;

public class Sample {
	private double time, value;
		public Sample(double aTime, double aValue) {
			time = aTime;
			value = aValue;
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
									double maxValue) {
			int res_x = calc(time,minTime,maxTime,minWidth,maxWidth);
			int res_y = maxHeight - calc(value,minValue,maxValue,minHeight,maxHeight) + minHeight;
			return new GrSample(res_x,res_y);
		}
		public static double minTime(Sample[] samples) {
			return samples[0].time;
		}
		public static double maxTime(Sample[] samples) {
			return samples[samples.length-1].time;
		}
		public static GrSample[] convert(Sample [] samples, int minWidth, int maxWidth, int minHeight, int maxHeight) {
			double minTime = minTime(samples), maxTime = maxTime(samples);
			System.out.println("Sample.convert enter len= "+Integer.toString(samples.length)+" minTime="+Double.toString(minTime)+" maxTime="+Double.toString(maxTime));
			Comparator<Sample> comparator =  (Sample s1, Sample s2)->{
				return (s1.value < s2.value) ? -1 : (s1.value == s2.value) ? 0 : 1;
			};
			
			double  minValue = Arrays.stream(samples).min(comparator).get().value,
					maxValue = Arrays.stream(samples).max(comparator).get().value;
			System.out.println("Sample.convert minValue="+Double.toString(minValue)+" maxValue="+Double.toString(maxValue));
			return Arrays.stream(samples).map((Sample sample)-> {
				return sample.getGrPoint(minWidth, maxWidth, minHeight, maxHeight, minTime, maxTime, minValue, maxValue);
			}).toArray(GrSample[]::new);
		}
}
