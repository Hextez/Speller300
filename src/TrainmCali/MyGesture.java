package TrainmCali;

import java.util.ArrayList;

public class MyGesture {

	
	private String typename;
	private ArrayList<Integer> Ypoints;
	private ArrayList<Integer> Xpoints;
	private Integer electrode;
	private Integer countTarget;
	private Integer countNTarget;
	
	public MyGesture(String typeName, Integer elec){
		typename = typeName; 
		Ypoints = new ArrayList<>();
		Xpoints = new ArrayList<>();
		electrode = elec;
		countTarget = 0;
		countNTarget = 0;
	}
	
	public void addYpoints(Integer point ){
		Ypoints.add(point);
	}
	
	public ArrayList<Integer> getYpoints(){
		return Ypoints;
	}
	
	public void addXpoints(Integer point ){
		Xpoints.add(point);
	}
	
	public ArrayList<Integer> getXpoints(){
		return Xpoints;
	}
	
	public String getTypename(){
		return typename;
	}
	
	public Integer getElec(){
		return electrode;
	}
	
	public void setTarget(){
		countTarget++;
	}
	public void setNTarget(){
		countNTarget++;
	}
	public void putZero(){
		countNTarget =0;
		countTarget = 0;
	}
	
	public Integer getTarget(){
		return countTarget;
	}
	public Integer getNTarget(){
		return countNTarget;
	}
}
