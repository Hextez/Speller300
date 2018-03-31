package Stuff;

import java.util.HashMap;

public class Inflexion {

	private HashMap<Integer, Double> Ypoints;

	public Inflexion (HashMap<Integer, Double> vals){
		Ypoints = vals;
	}

	public HashMap<Integer, Double> getNpoint(){
		HashMap<Integer, Double> points = new HashMap<>();
		double pointF = Ypoints.get(0);
		for (int i = 1; i < Ypoints.size(); i++){
			if (i != 255){
				if (pointF > Ypoints.get(i) && Ypoints.get(i+1) > Ypoints.get(i)){
					points.put(i, Ypoints.get(i));
				}
				pointF = Ypoints.get(i);
			}
		}
		return points;

	}
	
	public HashMap<Integer, Double> getUpoint(){
		HashMap<Integer, Double> points = new HashMap<>();
		double pointF = Ypoints.get(0);
		for (int i = 1; i < Ypoints.size(); i++){
			if (i != 255){
				if (pointF < Ypoints.get(i) && Ypoints.get(i+1) < Ypoints.get(i)){
					points.put(i, Ypoints.get(i));
				}
				pointF = Ypoints.get(i);
			}
		}
		return points;

	}

}
