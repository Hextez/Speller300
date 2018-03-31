package Stuff;

import java.util.HashMap;

public class CentralMoments {

	private HashMap<Integer, Double> points;

	
	public CentralMoments(HashMap<Integer, Double> points){
		this.points = points;
	}
	
	public Double pointsMean(){
		double sum = 0;
		for (double value : points.values()) {
			sum+=value;
		}
		return sum/points.size();
	}
	/*
	public Double firstMoment(){
		double sum = 0;
		double mean = pointsMean();
		for (double value : points.values()) {
			double y = Math.abs(value - mean);
			sum+= y;
		}
		return sum/points.size();
	}*/
	
	public Double standardDeviation(){
		double sum = 0;
		double mean = pointsMean();
		for (double value : points.values()) {
			double y = value - mean;
			sum+= Math.pow(y, 2);
		}
		return Math.sqrt(sum/points.size());
	}
	
	public Double skewness(){
		double sum = 0;
		double mean = pointsMean();
		for (double value : points.values()) {
			double y = value - mean;
			sum+= Math.pow(y, 3);
		}
		return Math.cbrt(sum/points.size());
	}
}
