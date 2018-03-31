package TrainmCali;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainData {
	private HashMap<Integer, ArrayList<Double>> part3; //0--1000
	
	
	public TrainData(){

		part3 = new HashMap<>();
		part3.put(0, new ArrayList<>());
		part3.put(1, new ArrayList<>());
		part3.put(2, new ArrayList<>());
		part3.put(3, new ArrayList<>());
		part3.put(4, new ArrayList<>());
		part3.put(5, new ArrayList<>());
		part3.put(6, new ArrayList<>());
		part3.put(7, new ArrayList<>());
		}
	
	
	//ter as listas
	
	public ArrayList<Double> getPart3(int i){
		return part3.get(i);
	}

	
	public void addPart3(int elec, Double val){
		part3.get(elec).add(val);
	}
	
}
