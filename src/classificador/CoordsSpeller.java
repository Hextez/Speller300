package classificador;

import java.util.ArrayList;

public class CoordsSpeller {

	private int x;
	private int y;
	private String letter;
	private ArrayList<Double> myBox = new ArrayList<Double>();
	
	public CoordsSpeller(int x, int y, String letter) {
		this.x = x;
		this.y = y;
		this.letter = letter;
	}
	
	public String getLetter() {
		return letter;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void clearBox() {
		myBox = new ArrayList<Double>();
	}
	public void addFirst(ArrayList<Double> m) {
		myBox = m;
	}
	
	public void addSignalBox(ArrayList<Double> m, int epoch) {
		ArrayList<Double> temp = new ArrayList<>();
		for (int x = 0; x < epoch; x++) {
			temp.add(myBox.get(x) + m.get(x));
		}
		myBox = temp;
	}
	
	public ArrayList<Double> getBox(){
		return myBox;
	}

}
