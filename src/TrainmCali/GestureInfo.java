package TrainmCali;

import java.util.ArrayList;

public class GestureInfo {

	
	private String typename;
	private ArrayList<Integer> Ypoints;
	private ArrayList<Integer> Xpoints;
	private Integer f;
	
	public GestureInfo(String typeName, Integer s){
		typename = typeName; 
		Ypoints = new ArrayList<>();
		Xpoints = new ArrayList<>();
		f = s;
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
	
	public Integer getONE(){
		return f;
	}
}
